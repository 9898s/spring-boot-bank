package com.example.bank.dto.account;

import com.example.bank.domain.account.Account;
import com.example.bank.domain.user.User;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public class AccountReqDto {

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
