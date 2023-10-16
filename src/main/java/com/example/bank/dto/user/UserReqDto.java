package com.example.bank.dto.user;

import com.example.bank.domain.user.User;
import com.example.bank.domain.user.UserEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserReqDto {

  @Setter
  @Getter
  public static class JoinReqDto {

    // 유효성 검사
    private String username;
    private String password;
    private String email;
    private String fullname;

    public User toEntity(BCryptPasswordEncoder passwordEncoder) {
      return User.builder()
          .username(username)
          .password(passwordEncoder.encode(password))
          .email(email)
          .role(UserEnum.CUSTOMER)
          .build();
    }
  }
}
