package com.example.bank.config.jwt;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.bank.config.auth.LoginUser;
import com.example.bank.domain.user.User;
import com.example.bank.domain.user.UserEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
class JwtAuthorizationFilterTest {

  @Autowired
  private MockMvc mvc;

  @Test
  void authorization_success_test() throws Exception {
    // given
    User user = User.builder()
        .id(1L)
        .role(UserEnum.CUSTOMER)
        .build();

    LoginUser loginUser = new LoginUser(user);
    String jwtToken = JwtProcess.create(loginUser);
    System.out.println("테스트:" + jwtToken);

    // when
    ResultActions resultActions = mvc.perform(get("/api/s/hello/test")
        .header(JwtVO.HEADER, jwtToken)
    );

    // then
    resultActions.andExpect(status().isNotFound());
  }

  @Test
  void authorization_fail_test() throws Exception {
    // given

    // when
    ResultActions resultActions = mvc.perform(get("/api/s/hello/test"));

    // then
    resultActions.andExpect(status().isUnauthorized());
  }

  @Test
  void authorization_admin_test() throws Exception {
    // given
    User user = User.builder()
        .id(1L)
        .role(UserEnum.ADMIN)
        .build();

    LoginUser loginUser = new LoginUser(user);
    String jwtToken = JwtProcess.create(loginUser);
    System.out.println("테스트:" + jwtToken);

    // when
    ResultActions resultActions = mvc.perform(get("/api/admin/hello/test")
        .header(JwtVO.HEADER, jwtToken)
    );

    // then
    resultActions.andExpect(status().isNotFound());
  }
}