package com.example.bank.dto.user;

import com.example.bank.domain.user.User;
import com.example.bank.domain.user.UserEnum;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserReqDto {

  @Setter
  @Getter
  public static class JoinReqDto {

    @NotEmpty // null이거나, 공백일 수 없다.
    private String username;

    @NotEmpty
    private String password;

    @NotEmpty
    private String email;

    @NotEmpty
    private String fullname;

    public User toEntity(BCryptPasswordEncoder passwordEncoder) {
      return User.builder()
          .username(username)
          .password(passwordEncoder.encode(password))
          .email(email)
          .fullname(fullname)
          .role(UserEnum.CUSTOMER)
          .build();
    }
  }
}
