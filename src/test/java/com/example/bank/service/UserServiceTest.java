package com.example.bank.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.bank.config.dummy.DummyObject;
import com.example.bank.domain.user.User;
import com.example.bank.domain.user.UserRepository;
import com.example.bank.dto.user.UserReqDto.JoinReqDto;
import com.example.bank.dto.user.UserResDto.JoinResDto;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest extends DummyObject {

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
    User ssar = newMockUser(1L, "ssar", "쌀");
    when(userRepository.save(any())).thenReturn(ssar);

    // when
    JoinResDto joinResDto = userService.회원가입(joinReqDto);

    // then
    System.out.println("테스트: " + joinResDto);
    assertThat(joinResDto.getId()).isEqualTo(1L);
    assertThat(joinResDto.getUsername()).isEqualTo("ssar");
  }
}