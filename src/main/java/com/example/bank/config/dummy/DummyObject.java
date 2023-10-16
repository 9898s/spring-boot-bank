package com.example.bank.config.dummy;

import com.example.bank.domain.account.Account;
import com.example.bank.domain.user.User;
import com.example.bank.domain.user.UserEnum;
import java.time.LocalDateTime;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class DummyObject {

  protected static User newUser(String username, String fullname) {
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    String encPassword = passwordEncoder.encode("1234");

    return User.builder()
        .username(username)
        .password(encPassword)
        .email(username + "@nate.com")
        .fullname(fullname)
        .role(UserEnum.CUSTOMER)
        .build();
  }

  protected static User newMockUser(Long id, String username, String fullname) {
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    String encPassword = passwordEncoder.encode("1234");

    return User.builder()
        .id(id)
        .username(username)
        .password(encPassword)
        .email(username + "@nate.com")
        .fullname(fullname)
        .role(UserEnum.CUSTOMER)
        .createAt(LocalDateTime.now())
        .updateAt(LocalDateTime.now())
        .build();
  }

  protected static Account newAccount(Long number, User user) {
    return Account.builder()
        .number(number)
        .password(1234L)
        .balance(1000L)
        .user(user)
        .build();
  }

  protected static Account newMockAccount(Long id, Long number, Long balance, User user) {
    return Account.builder()
        .id(id)
        .number(number)
        .password(1234L)
        .balance(balance)
        .user(user)
        .createAt(LocalDateTime.now())
        .updateAt(LocalDateTime.now())
        .build();
  }
}
