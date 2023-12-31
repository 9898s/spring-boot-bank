package com.example.bank.config;

import com.example.bank.config.jwt.JwtAuthenticationFilter;
import com.example.bank.config.jwt.JwtAuthorizationFilter;
import com.example.bank.domain.user.UserEnum;
import com.example.bank.util.CustomResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    log.debug("디버그: BCryptPasswordEncoder 빈 등록됨");
    return new BCryptPasswordEncoder();
  }

  // JWT 필터 등록이 필요함
  public class CustomerSecurityFileterManager extends
      AbstractHttpConfigurer<CustomerSecurityFileterManager, HttpSecurity> {

    @Override
    public void configure(HttpSecurity builder) throws Exception {
      AuthenticationManager authenticationManager =
          builder.getSharedObject(AuthenticationManager.class);
      builder.addFilter(new JwtAuthenticationFilter(authenticationManager));
      builder.addFilter(new JwtAuthorizationFilter(authenticationManager));
      super.configure(builder);
    }
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    log.debug("디버그: filterChain 빈 등록됨");

    http.headers().frameOptions().disable(); // iframe 허용 안함
    http.csrf().disable(); // enable이면 포스트맨 작동 안함
    http.cors().configurationSource(configurationSource());

    // jSessionId를 서버쪽에서 관리 X
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    // react, 앱으로 요청할 예정
    http.formLogin().disable();

    // httpBasic은 브라우저가 팝업창을 이용해서 사용자 인증을 진행한다.
    http.httpBasic().disable();

    // 필터 적용
    http.apply(new CustomerSecurityFileterManager());

    // 인증 실패
    http.exceptionHandling()
        .authenticationEntryPoint(
            (request, response, authenticationException) ->
                CustomResponseUtil.fail(response, "로그인을 진행해 주세요", HttpStatus.UNAUTHORIZED));

    http.exceptionHandling()
        .accessDeniedHandler(
            (request, response, e) -> CustomResponseUtil.fail(response, "권한이 없습니다.",
                HttpStatus.FORBIDDEN));

    http.authorizeHttpRequests()
        .antMatchers("/api/s/**").authenticated()
        .antMatchers("/api/admin/**").hasRole(String.valueOf(UserEnum.ADMIN))
        .anyRequest().permitAll();

    return http.build();
  }

  public CorsConfigurationSource configurationSource() {
    log.debug("디버그: configurationSource cors 설정이 SecurityFilterChain에 등록됨");

    CorsConfiguration configuration = new CorsConfiguration();
    configuration.addAllowedHeader("*");
    configuration.addAllowedMethod("*"); // GET, POST, PUT, DELETE (자바스크립트 요청 허용)
    configuration.addAllowedOriginPattern("*"); // 모든 IP 주소 허용 (프론트엔드 아이피만 허용)
    configuration.setAllowCredentials(true); // 클라이언트에서 쿠키 요청 허용

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
