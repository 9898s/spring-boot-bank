package com.example.bank.service;

import com.example.bank.domain.account.Account;
import com.example.bank.domain.account.AccountRepository;
import com.example.bank.domain.user.User;
import com.example.bank.domain.user.UserRepository;
import com.example.bank.dto.account.AccountReqDto.AccountSaveReqDto;
import com.example.bank.dto.account.AccountResDto.AccountSaveResDto;
import com.example.bank.handler.ex.CustomApiException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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

  public AccountListResDto 계좌목록보기_유저별(Long userId) {
    User userPS = userRepository.findById(userId)
        .orElseThrow(() -> new CustomApiException("유저를 찾을 수 없습니다."));

    // 유저의 모든 계좌 목록
    List<Account> accountListPS = accountRepository.findByUser_Id(userId);
    return new AccountListResDto(userPS, accountListPS);
  }

  @Setter
  @Getter
  public static class AccountListResDto {

    private final String fullname;
    private List<AccountDto> accounts = new ArrayList<>();

    public AccountListResDto(User user, List<Account> accounts) {
      this.fullname = user.getFullname();
      this.accounts = accounts.stream()
          .map(AccountDto::new)
          .collect(Collectors.toList());
    }

    @Setter
    @Getter
    public class AccountDto {

      private final Long id;
      private final Long number;
      private final Long balance;

      public AccountDto(Account account) {
        this.id = account.getId();
        this.number = account.getNumber();
        this.balance = account.getBalance();
      }
    }
  }

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
}
