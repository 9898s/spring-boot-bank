package com.example.bank.service;

import com.example.bank.domain.account.Account;
import com.example.bank.domain.account.AccountRepository;
import com.example.bank.domain.user.User;
import com.example.bank.domain.user.UserRepository;
import com.example.bank.handler.ex.CustomApiException;
import java.util.Optional;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AccountService {

  private final AccountRepository accountRepository;
  private final UserRepository userRepository;

  @Transactional
  public AccountSaveResDto 계좌등록(AccountSaveReqDto accountSaveReqDto, Long userId) {
    User userPS = userRepository.findById(userId)
        .orElseThrow(() -> new CustomApiException("유저를 찾을 수 없습니다."));

    Optional<Account> accountOP = accountRepository.findByNumber(accountSaveReqDto.getNumber());
    if (accountOP.isPresent()) {
      throw new CustomApiException("해당 계좌가 이미 존재합니다.");
    }

    Account accountPS = accountRepository.save(accountSaveReqDto.toEntity(userPS));
    return new AccountSaveResDto(accountPS);
  }

  @Setter
  @Getter
  public static class AccountSaveResDto {

    private Long id;
    private Long number;
    private Long balance;

    public AccountSaveResDto(Account account) {
      this.id = account.getId();
      this.number = account.getNumber();
      this.balance = account.getBalance();
    }
  }

  @Setter
  @Getter
  public static class AccountSaveReqDto {

    @NotNull
    @Digits(integer = 4, fraction = 4)
    private Long number;

    @NotNull
    @Digits(integer = 4, fraction = 4)
    private Long password;

    public Account toEntity(User user) {
      return Account.builder()
          .number(number)
          .password(password)
          .balance(1000L)
          .user(user)
          .build();
    }
  }
}
