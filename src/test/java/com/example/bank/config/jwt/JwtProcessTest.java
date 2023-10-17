package com.example.bank.config.jwt;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.bank.config.auth.LoginUser;
import com.example.bank.domain.user.User;
import com.example.bank.domain.user.UserEnum;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class JwtProcessTest {

  private String createToken() {
    // given
    User user = User.builder()
        .id(1L)
        .role(UserEnum.CUSTOMER)
        .build();
    LoginUser loginUser = new LoginUser(user);

    // when
    return JwtProcess.create(loginUser);
  }

  @Test
  void create_test() throws Exception {
    // when
    String jwtToken = createToken();
    System.out.println("테스트: " + jwtToken);

    // then
    assertTrue(jwtToken.startsWith(JwtVO.TOKEN_PREFIX));
  }

  @Test
  void verify_test() throws Exception {
    // given
    String token = createToken(); // Bearer 제거해서 처리하기
    String jwtToken = token.replace(JwtVO.TOKEN_PREFIX, "");

    // when
    LoginUser loginUser = JwtProcess.verify(jwtToken);
    System.out.println("테스트: " + loginUser.getUser().getId());
    System.out.println("테스트: " + loginUser.getUser().getRole().name());

    // then
    Assertions.assertThat(loginUser.getUser().getId()).isEqualTo(1L);
  }
}