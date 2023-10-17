package com.example.bank.web;

import com.example.bank.config.auth.LoginUser;
import com.example.bank.dto.ResponseDto;
import com.example.bank.dto.account.AccountReqDto;
import com.example.bank.dto.account.AccountResDto.AccountListResDto;
import com.example.bank.dto.account.AccountResDto.AccountSaveResDto;
import com.example.bank.service.AccountService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class AccountController {

  private final AccountService accountService;

  @PostMapping("/s/account")
  public ResponseEntity<?> saveAccount(
      @RequestBody @Valid AccountReqDto.AccountSaveReqDto accountSaveReqDto,
      BindingResult bindingResult, @AuthenticationPrincipal LoginUser loginUser) {
    AccountSaveResDto accountSaveResDto = accountService.계좌등록(accountSaveReqDto,
        loginUser.getUser().getId());
    return new ResponseEntity<>(new ResponseDto<>(1, "계좌등록 성공", accountSaveResDto),
        HttpStatus.CREATED);
  }

  // 인증이 필요하고, account 테이블에 1번 row를 주세요
  // 권한 처리 해야함
  @GetMapping("/s/account/login-user")
  public ResponseEntity<?> findUserAccount(@AuthenticationPrincipal LoginUser loginUser) {
    AccountListResDto accountListResDto = accountService.계좌목록보기_유저별(loginUser.getUser().getId());
    return new ResponseEntity<>(new ResponseDto<>(1, "계좌목록보기_유저별 성공", accountListResDto),
        HttpStatus.OK);
  }

  @DeleteMapping("/s/account/{number}")
  public ResponseEntity<?> deleteAccount(
      @PathVariable Long number,
      @AuthenticationPrincipal LoginUser loginUser) {
    accountService.계좌삭제(number, loginUser.getUser().getId());
    return new ResponseEntity<>(new ResponseDto<>(1, "계좌 삭제 완료", null), HttpStatus.OK);
  }
}
