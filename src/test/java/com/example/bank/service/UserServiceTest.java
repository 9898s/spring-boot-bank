package com.example.bank.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.bank.domain.user.User;
import com.example.bank.domain.user.UserEnum;
import com.example.bank.domain.user.UserRepository;
import com.example.bank.service.UserService.JoinReqDto;
import com.example.bank.service.UserService.JoinResDto;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @InjectMocks
  private UserService userService;

  @Mock
  private UserRepository userRepository;

  @Spy
  private BCryptPasswordEncoder passwordEncoder;

  @Test
  void 회원가입_test() throws Exception {
    // given
    JoinReqDto joinReqDto = new JoinReqDto();
    joinReqDto.setUsername("ssar");
    joinReqDto.setPassword("1234");
    joinReqDto.setEmail("ssar@nate.com");
    joinReqDto.setFullname("쌀");

    // stub 1
    when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
//    when(userRepository.findByUsername(any())).thenReturn(Optional.of(new User()));

    // stub 2
    User ssar = User.builder()
        .id(1L)
        .username("ssar")
        .password("2345")
        .email("ssar@nate.com")
        .fullname("쌀")
        .role(UserEnum.CUSTOMER)
        .createAt(LocalDateTime.now())
        .updateAt(LocalDateTime.now())
        .build();
    when(userRepository.save(any())).thenReturn(ssar);

    // when
    JoinResDto joinResDto = userService.회원가입(joinReqDto);

    // then
    System.out.println("테스트: " + joinResDto);
    assertThat(joinResDto.getId()).isEqualTo(1L);
    assertThat(joinResDto.getUsername()).isEqualTo("ssar");
  }
}