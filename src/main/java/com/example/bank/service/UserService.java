package com.example.bank.service;

import com.example.bank.domain.user.User;
import com.example.bank.domain.user.UserEnum;
import com.example.bank.domain.user.UserRepository;
import com.example.bank.handler.ex.CustomApiException;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

  private final Logger log = LoggerFactory.getLogger(getClass());
  private final UserRepository userRepository;
  private final BCryptPasswordEncoder passwordEncoder;

  @Transactional // 트랜잭션이 메서드 시작할 때 시작되고, 종료될 때 함께 종료
  public JoinResDto 회원가입(JoinReqDto joinReqDto) {
    // 1. 동일 유저 네임 존재 검사
    Optional<User> userOP = userRepository.findByUsername(joinReqDto.getUsername());
    if (userOP.isPresent()) {
      // 유저네임 중복
      throw new CustomApiException("동일한 username이 존재합니다.");
    }

    // 2. 패스워드 인코딩 + 회원가입
    User userPS = userRepository.save(joinReqDto.toEntity(passwordEncoder));

    // 3. dto 응답
    return new JoinResDto(userPS);
  }

  @Setter
  @Getter
  public static class JoinResDto {

    private Long id;
    private String username;
    private String fullname;

    public JoinResDto(User user) {
      this.id = user.getId();
      this.username = user.getUsername();
      this.fullname = user.getFullname();
    }
  }

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
