package com.example.bank.service;

import com.example.bank.domain.account.Account;
import com.example.bank.domain.account.AccountRepository;
import com.example.bank.domain.transaction.Transaction;
import com.example.bank.domain.transaction.TransactionEnum;
import com.example.bank.domain.transaction.TransactionRepository;
import com.example.bank.domain.user.User;
import com.example.bank.domain.user.UserRepository;
import com.example.bank.dto.account.AccountReqDto.AccountSaveReqDto;
import com.example.bank.dto.account.AccountResDto.AccountListResDto;
import com.example.bank.dto.account.AccountResDto.AccountSaveResDto;
import com.example.bank.handler.ex.CustomApiException;
import com.example.bank.util.CustomDateUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import java.util.Optional;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AccountService {

  private final AccountRepository accountRepository;
  private final UserRepository userRepository;
  private final TransactionRepository transactionRepository;

  public AccountListResDto 계좌목록보기_유저별(Long userId) {
    User userPS = userRepository.findById(userId)
        .orElseThrow(() -> new CustomApiException("유저를 찾을 수 없습니다."));

    // 유저의 모든 계좌 목록
    List<Account> accountListPS = accountRepository.findByUser_Id(userId);
    return new AccountListResDto(userPS, accountListPS);
  }

  @Transactional
  public AccountSaveResDto 계좌등록(AccountSaveReqDto accountSaveReqDto, Long userId) {
    User userPS = userRepository.findById(userId)
        .orElseThrow(() -> new CustomApiException("유저를 찾을 수 없습니다."));

    Optional<Account> accountOP = accountRepository.findByNumber(accountSaveReqDto.getNumber());
    if (accountOP.isPresent()) {
      throw new CustomApiException("해당 계좌가 이미 존재합니다.");
    }

    Account accountPS = accountRepository.save(accountSaveReqDto.toEntity(userPS));
    return new AccountSaveResDto(accountPS);
  }

  @Transactional
  public void 계좌삭제(Long number, Long userId) {
    Account accountPS = accountRepository.findByNumber(number)
        .orElseThrow(() -> new CustomApiException("계좌를 찾을 수 없습니다."));

    accountPS.checkOwner(userId);

    accountRepository.deleteById(accountPS.getId());
  }

  @Transactional
  public AccountDepositResDto 계좌입금(AccountDepositReqDto accountDepositReqDto) { // ATM -> 누군가의 계좌
    // 0원 체크
    if (accountDepositReqDto.getAmount() <= 0L) {
      throw new CustomApiException("0원 이하의 금액을 입금할 수 없습니다.");
    }

    // 입금 계좌 확인
    Account depositAccountPS = accountRepository.findByNumber(accountDepositReqDto.getNumber())
        .orElseThrow(() -> new CustomApiException("계좌를 찾을 수 없습니다."));

    // 입금 (해당 계좌 balance 조정 - update문 - 더티체킹)
    depositAccountPS.deposit(accountDepositReqDto.getAmount());

    // 거래 내역 남기기
    Transaction transaction = Transaction.builder()
        .depositAccount(depositAccountPS)
        .withdrawAccount(null)
        .depositAccountBalance(depositAccountPS.getBalance())
        .withdrawAccountBalance(null)
        .amount(accountDepositReqDto.getAmount())
        .gubun(TransactionEnum.DEPOSIT)
        .sender("ATM")
        .receiver(depositAccountPS.getNumber().toString())
        .tel(accountDepositReqDto.getTel())
        .build();

    Transaction transactionPS = transactionRepository.save(transaction);
    return new AccountDepositResDto(depositAccountPS, transactionPS);
  }

  @Setter
  @Getter
  public static class AccountDepositResDto {

    private Long id; // 계좌ID
    private Long number; // 계좌 번호
    private TransactionDto transaction;

    public AccountDepositResDto(Account account, Transaction transaction) {
      this.id = account.getId();
      this.number = account.getNumber();
      this.transaction = new TransactionDto(transaction);
    }

    @Setter
    @Getter
    public class TransactionDto {

      private Long id;
      private String gubun;
      private String sender;
      private String receiver;
      private Long amount;

      @JsonIgnore
      private Long depositAccountBalance; // 클라이언트에게 전달 X -> 서비스단에서 테스트 용도

      private String tel;
      private String createAt;

      public TransactionDto(Transaction transaction) {
        this.id = transaction.getId();
        this.gubun = transaction.getGubun().getValue();
        this.sender = transaction.getSender();
        this.receiver = transaction.getReceiver();
        this.amount = transaction.getAmount();
        this.depositAccountBalance = transaction.getDepositAccountBalance();
        this.tel = transaction.getTel();
        this.createAt = CustomDateUtil.toStringForamt(transaction.getCreateAt());
      }
    }
  }

  @Setter
  @Getter
  public static class AccountDepositReqDto {

    @NotNull
    @Digits(integer = 4, fraction = 4)
    private Long number;

    @NotNull
    private Long amount; // 0원 유효성 검사

    @NotEmpty
    @Pattern(regexp = "^(DEPOSIT)$")
    private String gubun; // DEPOSIT

    @NotEmpty
    @Pattern(regexp = "^[0-9]{3}[0-9]{4}[0-9]{4}")
    private String tel;
  }
}
