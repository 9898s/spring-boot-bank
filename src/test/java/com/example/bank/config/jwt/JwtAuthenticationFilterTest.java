package com.example.bank.config.jwt;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.bank.config.dummy.DummyObject;
import com.example.bank.domain.user.UserRepository;
import com.example.bank.dto.user.UserReqDto.LoginReqDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
class JwtAuthenticationFilterTest extends DummyObject {

  @Autowired
  private ObjectMapper om;

  @Autowired
  private MockMvc mvc;

  @Autowired
  private UserRepository userRepository;

  @BeforeEach
  void setUp() throws Exception {
    userRepository.save(newUser("ssar", "쌀"));
  }

  @Test
  void successfulAuthenticationtest() throws Exception {
    // given
    LoginReqDto loginReqDto = new LoginReqDto();
    loginReqDto.setUsername("ssar");
    loginReqDto.setPassword("1234");

    String requestBody = om.writeValueAsString(loginReqDto);
    System.out.println("테스트: " + requestBody);

    // when
    ResultActions resultActions = mvc.perform(post("/api/login")
        .content(requestBody)
        .contentType(MediaType.APPLICATION_JSON)
    );

    String responseBody = resultActions.andReturn().getResponse().getContentAsString();
    String jwtToken = resultActions.andReturn().getResponse().getHeader(JwtVO.HEADER);
    System.out.println("테스트: " + responseBody);
    System.out.println("테스트:" + jwtToken);

    // then
    resultActions.andExpect(status().isOk());
    assertNotNull(jwtToken);
    assertTrue(jwtToken.startsWith(JwtVO.TOKEN_PREFIX));
    resultActions.andExpect(jsonPath("$.data.username").value("ssar"));
  }

  @Test
  void unsuccessfulAuthentication_test() throws Exception {
    // given
    LoginReqDto loginReqDto = new LoginReqDto();
    loginReqDto.setUsername("ssar");
    loginReqDto.setPassword("12345");

    String requestBody = om.writeValueAsString(loginReqDto);
    System.out.println("테스트: " + requestBody);

    // when
    ResultActions resultActions = mvc.perform(post("/api/login")
        .content(requestBody)
        .contentType(MediaType.APPLICATION_JSON)
    );

    String responseBody = resultActions.andReturn().getResponse().getContentAsString();
    String jwtToken = resultActions.andReturn().getResponse().getHeader(JwtVO.HEADER);
    System.out.println("테스트: " + responseBody);
    System.out.println("테스트:" + jwtToken);

    // then
    resultActions.andExpect(status().isUnauthorized());
  }
}