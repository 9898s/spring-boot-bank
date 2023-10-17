package com.example.bank.domain.account;

import com.example.bank.domain.user.User;
import com.example.bank.handler.ex.CustomApiException;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@NoArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
@Table(name = "account_tb")
@Entity
public class Account {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false, length = 4)
  private Long number;

  @Column(nullable = false, length = 4)
  private Long password;

  @Column(nullable = false)
  private Long balance;

  @ManyToOne(fetch = FetchType.LAZY)
  private User user;

  @CreatedDate
  @Column(nullable = false)
  private LocalDateTime createAt;

  @LastModifiedDate
  @Column(nullable = false)
  private LocalDateTime updateAt;

  @Builder
  public Account(Long id, Long number, Long password, Long balance, User user,
      LocalDateTime createAt,
      LocalDateTime updateAt) {
    this.id = id;
    this.number = number;
    this.password = password;
    this.balance = balance;
    this.user = user;
    this.createAt = createAt;
    this.updateAt = updateAt;
  }

  public void checkOwner(Long userId) { // Lazy 로딩이어도 id를 조회할 때는 select 쿼리가 날라가지 않는다.
    if (!Objects.equals(user.getId(), userId)) {
      throw new CustomApiException("계좌 소유자가 아닙니다.");
    }
  }
}
