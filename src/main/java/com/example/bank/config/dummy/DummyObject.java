package com.example.bank.config.dummy;

import com.example.bank.domain.user.User;
import com.example.bank.domain.user.UserEnum;
import java.time.LocalDateTime;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class DummyObject {

  protected User newUser(String username, String fullname) {
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

  protected User newMockUser(Long id, String username, String fullname) {
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
}
