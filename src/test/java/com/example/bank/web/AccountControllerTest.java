package com.example.bank.web;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.bank.config.dummy.DummyObject;
import com.example.bank.domain.account.Account;
import com.example.bank.domain.account.AccountRepository;
import com.example.bank.domain.user.User;
import com.example.bank.domain.user.UserRepository;
import com.example.bank.dto.account.AccountReqDto.AccountSaveReqDto;
import com.example.bank.handler.ex.CustomApiException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
class AccountControllerTest extends DummyObject {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private ObjectMapper om;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private EntityManager em;

  @BeforeEach
  void setUp() {
    User ssar = userRepository.save(newUser("ssar", "쌀"));
    User cos = userRepository.save(newUser("cos", "코스"));

    Account ssarAccount1 = accountRepository.save(newAccount(1111L, ssar));
    Account cosAccount1 = accountRepository.save(newAccount(2222L, cos));

    em.clear();
  }

  // jwt token -> 인증필터 -> 시큐리티 세션생성
  // setupBefore = TEST_METHOD (setUp 메서드 실행전에 수행)
  // setupBefore = TEST_EXECUTION (saveAccount_test 메서드 실행전에 수행)

  // 디비에서 username=ssar 조회를 해서 세션에 담아주는 어노테이션
  @WithUserDetails(value = "ssar", setupBefore = TestExecutionEvent.TEST_EXECUTION)
  @Test
  void saveAccount_test() throws Exception {
    // given
    AccountSaveReqDto accountSaveReqDto = new AccountSaveReqDto();
    accountSaveReqDto.setNumber(9999L);
    accountSaveReqDto.setPassword(1234L);

    String requestBody = om.writeValueAsString(accountSaveReqDto);
    System.out.println("테스트: " + requestBody);

    // when
    ResultActions resultActions = mvc.perform(post("/api/s/account")
        .content(requestBody)
        .contentType(MediaType.APPLICATION_JSON)
    );

    String responseBody = resultActions.andReturn().getResponse().getContentAsString();
    System.out.println("테스트: " + responseBody);

    // then
    resultActions.andExpect(status().isCreated());
  }

  /**
   * 테스트 시에는 insert 한 것들이 전부 PC에 올라감 (영속화) 영속화 된 것들을 초기화 해주는 것이 개발 모드와 동일한 환경으로 테스트를 할 수 있게 해준다. 최초
   * select는 쿼리가 발생하지만! - PC에 있으면 1차 캐시를 함. Lazy 로딩은 쿼리도 발생 안함 - PC에 있다면! Lazy 로딩할 때 PC 없다면 쿼리가
   * 발생함.
   */
  @WithUserDetails(value = "ssar", setupBefore = TestExecutionEvent.TEST_EXECUTION)
  @Test
  void deleteAccount_test() throws Exception {
    // given
    Long number = 1111L;

    // when
    ResultActions resultActions = mvc.perform(delete("/api/s/account/" + number));

    String responseBody = resultActions.andReturn().getResponse().getContentAsString();
    System.out.println("테스트: " + responseBody);

    // then
    // JUnit 테스트에서 delete 쿼리는 DB관련(DML)으로 가장 마지막에 실행되면 발동 안됨.
    assertThrows(CustomApiException.class,
        () -> accountRepository.findByNumber(number)
            .orElseThrow(() -> new CustomApiException("계좌를 찾을 수 없습니다.")));
  }
}