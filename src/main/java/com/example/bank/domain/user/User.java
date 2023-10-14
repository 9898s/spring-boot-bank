package com.example.bank.domain.user;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user_tb")
@Entity
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false, length = 20)
  private String username;

  @Column(nullable = false, length = 60)
  private String password;

  @Column(nullable = false, length = 20)
  private String email;

  @Column(nullable = false, length = 20)
  private String fullname;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private UserEnum role; // ADMIN, CUSTOMER

  @CreatedDate
  @Column(nullable = false)
  private LocalDateTime createAt;

  @LastModifiedDate
  @Column(nullable = false)
  private LocalDateTime updateAt;

  @Builder
  public User(Long id, String username, String password, String email, String fullname,
      UserEnum role,
      LocalDateTime createAt, LocalDateTime updateAt) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.email = email;
    this.fullname = fullname;
    this.role = role;
    this.createAt = createAt;
    this.updateAt = updateAt;
  }
}
