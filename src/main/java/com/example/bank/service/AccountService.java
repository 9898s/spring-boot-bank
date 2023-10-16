package com.example.bank.service;

import com.example.bank.domain.account.Account;
import com.example.bank.domain.account.AccountRepository;
import com.example.bank.domain.user.User;
import com.example.bank.domain.user.UserRepository;
import com.example.bank.dto.account.AccountReqDto.AccountSaveReqDto;
import com.example.bank.dto.account.AccountResDto.AccountSaveResDto;
import com.example.bank.handler.ex.CustomApiException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
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
}
