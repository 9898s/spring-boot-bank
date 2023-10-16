package com.example.bank.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.bank.config.dummy.DummyObject;
import com.example.bank.domain.user.UserRepository;
import com.example.bank.dto.user.UserReqDto.JoinReqDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
class UserControllerTest extends DummyObject {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private ObjectMapper om;

  @Autowired
  private UserRepository userRepository;

  @BeforeEach
  void setUp() {
    userRepository.save(newUser("ssar", "쌀"));
  }

  @Test
  void join_success_test() throws Exception {
    // given
    JoinReqDto joinReqDto = new JoinReqDto();
    joinReqDto.setUsername("love");
    joinReqDto.setPassword("1234");
    joinReqDto.setEmail("love@nate.com");
    joinReqDto.setFullname("러브");

    String requestBody = om.writeValueAsString(joinReqDto);
//    System.out.println("테스트: " + requestBody);

    // when
    ResultActions resultActions = mvc.perform(post("/api/join")
        .content(requestBody)
        .contentType(MediaType.APPLICATION_JSON)
    );

//    String responseBody = resultActions.andReturn().getResponse().getContentAsString();
//    System.out.println("테스트: " + responseBody);

    // then
    resultActions.andExpect(status().isCreated());
  }

  @Test
  void join_fail_test() throws Exception {
    // given
    JoinReqDto joinReqDto = new JoinReqDto();
    joinReqDto.setUsername("ssar");
    joinReqDto.setPassword("1234");
    joinReqDto.setEmail("ssar@nate.com");
    joinReqDto.setFullname("쌀");

    String requestBody = om.writeValueAsString(joinReqDto);
//    System.out.println("테스트: " + requestBody);

    // when
    ResultActions resultActions = mvc.perform(post("/api/join")
        .content(requestBody)
        .contentType(MediaType.APPLICATION_JSON)
    );

//    String responseBody = resultActions.andReturn().getResponse().getContentAsString();
//    System.out.println("테스트: " + responseBody);

    // then
    resultActions.andExpect(status().isBadRequest());
  }
}