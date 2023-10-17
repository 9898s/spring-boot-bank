package com.example.bank.dto.account;

import com.example.bank.domain.account.Account;
import com.example.bank.domain.user.User;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;

public class AccountResDto {

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
}
