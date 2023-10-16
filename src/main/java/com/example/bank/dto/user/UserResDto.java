package com.example.bank.dto.user;

import com.example.bank.domain.user.User;
import lombok.Getter;
import lombok.Setter;

public class UserResDto {

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
