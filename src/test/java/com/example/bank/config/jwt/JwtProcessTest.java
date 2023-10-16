package com.example.bank.config.jwt;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.bank.config.auth.LoginUser;
import com.example.bank.domain.user.User;
import com.example.bank.domain.user.UserEnum;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class JwtProcessTest {

  @Test
  void create_test() throws Exception {
    // given
    User user = User.builder()
        .id(1L)
        .role(UserEnum.CUSTOMER)
        .build();
    LoginUser loginUser = new LoginUser(user);

    // when
    String jwtToken = JwtProcess.create(loginUser);
    System.out.println("테스트: " + jwtToken);

    // then
    assertTrue(jwtToken.startsWith(JwtVO.TOKEN_PREFIX));
  }

  @Test
  void verify_test() throws Exception {
    // given
    String jwtToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJiYW5rIiwicm9sZSI6IkNVU1RPTUVSIiwiaWQiOjEsImV4cCI6MTY5ODA1ODk4Mn0.jAQ19Ta8xYZm-4e8qVVFwpPczzV0ohD0OkfeW3iRUZXs4NnbYX8xNoffAqZ8FlZXz9wtZLVum1L9IU8C8-r6JA";

    // when
    LoginUser loginUser = JwtProcess.verify(jwtToken);
    System.out.println("테스트: " + loginUser.getUser().getId());
    System.out.println("테스트: " + loginUser.getUser().getRole().name());

    // then
    Assertions.assertThat(loginUser.getUser().getId()).isEqualTo(1L);
  }
}