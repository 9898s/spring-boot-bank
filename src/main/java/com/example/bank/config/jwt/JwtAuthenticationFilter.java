package com.example.bank.config.jwt;

import com.example.bank.config.auth.LoginUser;
import com.example.bank.dto.user.UserReqDto.LoginReqDto;
import com.example.bank.dto.user.UserResDto.LoginResDto;
import com.example.bank.util.CustomResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;

  public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
    super(authenticationManager);
    setFilterProcessesUrl("/api/login");
    this.authenticationManager = authenticationManager;
  }

  // Post: /api/login
  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException {

    try {
      ObjectMapper om = new ObjectMapper();
      LoginReqDto loginReqDto = om.readValue(request.getInputStream(), LoginReqDto.class);

      // 강제 로그인
      UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
          loginReqDto.getUsername(), loginReqDto.getPassword());

      // userDetailsService의 loadUserByUsername 호출
      // JWT를 쓴다 하더라도, 컨트롤러 진입하면 시큐리티의 권한체크, 인증체크의 도움을 받을 수 있게 세션을 만든다.
      // 이 세션이 유효기간은 request하고, response하면 끝
      return authenticationManager.authenticate(authenticationToken);
    } catch (Exception e) {
      // authenticationEntryPoint에 걸린다.
      throw new InternalAuthenticationServiceException(e.getMessage());
    }
  }

  // return authentication 잘 작동하면 successfulAuthentication 메서드 호출
  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain, Authentication authResult) throws IOException, ServletException {
    LoginUser loginUser = (LoginUser) authResult.getPrincipal();
    String jwtToken = JwtProcess.create(loginUser);
    response.addHeader(JwtVO.HEADER, jwtToken);

    LoginResDto loginResDto = new LoginResDto(loginUser.getUser());
    CustomResponseUtil.success(response, loginResDto);
  }
}
