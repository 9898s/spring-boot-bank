package com.example.bank.dto.user;

import com.example.bank.domain.user.User;
import com.example.bank.util.CustomDateUtil;
import lombok.Getter;
import lombok.Setter;

public class UserResDto {

  @Setter
  @Getter
  public static class LoginResDto {

    private Long id;
    private String username;
    private String createdAt;

    public LoginResDto(User user) {
      this.id = user.getId();
      this.username = user.getUsername();
      this.createdAt = CustomDateUtil.toStringForamt(user.getCreateAt());
    }
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
}
