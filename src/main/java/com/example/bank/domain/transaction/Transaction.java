package com.example.bank.domain.transaction;

import com.example.bank.domain.account.Account;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "transaction_tb")
@Entity
public class Transaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  private Account withdrawAccount;

  @ManyToOne(fetch = FetchType.LAZY)
  private Account depositAccount;

  @Column(nullable = false)
  private Long amount;

  private Long withdrawAccountBalance;
  private Long depositAccountBalance;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private TransactionEnum gubun; // WITHDRAW, DEPOSIT, TRANSFER, ALL

  private String sender;
  private String receiver;
  private String tel;

  @CreatedDate
  @Column(nullable = false)
  private LocalDateTime createAt;

  @LastModifiedDate
  @Column(nullable = false)
  private LocalDateTime updateAt;

  @Builder
  public Transaction(Long id, Account withdrawAccount, Account depositAccount, Long amount,
      Long withdrawAccountBalance, Long depositAccountBalance, TransactionEnum gubun, String sender,
      String receiver, String tel, LocalDateTime createAt, LocalDateTime updateAt) {
    this.id = id;
    this.withdrawAccount = withdrawAccount;
    this.depositAccount = depositAccount;
    this.amount = amount;
    this.withdrawAccountBalance = withdrawAccountBalance;
    this.depositAccountBalance = depositAccountBalance;
    this.gubun = gubun;
    this.sender = sender;
    this.receiver = receiver;
    this.tel = tel;
    this.createAt = createAt;
    this.updateAt = updateAt;
  }
}
