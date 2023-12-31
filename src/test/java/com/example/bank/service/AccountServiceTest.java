package com.example.bank.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.bank.config.dummy.DummyObject;
import com.example.bank.domain.account.Account;
import com.example.bank.domain.account.AccountRepository;
import com.example.bank.domain.user.User;
import com.example.bank.domain.user.UserRepository;
import com.example.bank.dto.account.AccountReqDto.AccountSaveReqDto;
import com.example.bank.dto.account.AccountResDto.AccountSaveResDto;
import com.example.bank.handler.ex.CustomApiException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest extends DummyObject {

  @InjectMocks // 모든 Mock들이 InjectMocks로 주입됨
  private AccountService accountService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private AccountRepository accountRepository;

  @Spy // 진짜 객체를 InjectMocks로 주입됨
  private ObjectMapper om;

  @Test
  void 계좌등록_test() throws Exception {
    // given
    Long userId = 1L;

    AccountSaveReqDto accountSaveReqDto = new AccountSaveReqDto();
    accountSaveReqDto.setNumber(1111L);
    accountSaveReqDto.setPassword(1234L);

    // stub 1
    User ssar = newMockUser(userId, "ssal", "쌀");
    when(userRepository.findById(any())).thenReturn(Optional.of(ssar));

    // stub 2
    when(accountRepository.findByNumber(any())).thenReturn(Optional.empty());

    // stub 3
    Account ssarAccount = newMockAccount(1L, 1111L, 1000L, ssar);
    when(accountRepository.save(any())).thenReturn(ssarAccount);

    // when
    AccountSaveResDto accountSaveResDto = accountService.계좌등록(accountSaveReqDto, userId);
    String responseBody = om.writeValueAsString(accountSaveResDto);
    System.out.println("테스트: " + responseBody);

    // then
    Assertions.assertThat(accountSaveResDto.getNumber()).isEqualTo(1111L);
  }

  @Test
  void 계좌삭제_test() throws Exception {
    // given
    Long number = 1111L;
    Long userId = 2L;

    // stub
    User ssar = newMockUser(1L, "ssar", "쌀");
    Account ssarAccount = newMockAccount(1L, 1111L, 1000L, ssar);
    when(accountRepository.findByNumber(any())).thenReturn(Optional.of(ssarAccount));

    // when
    assertThrows(CustomApiException.class, () -> accountService.계좌삭제(number, userId));
  }
}