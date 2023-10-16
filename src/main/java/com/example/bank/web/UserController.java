package com.example.bank.web;

import com.example.bank.dto.ResponseDto;
import com.example.bank.dto.user.UserReqDto.JoinReqDto;
import com.example.bank.dto.user.UserResDto.JoinResDto;
import com.example.bank.service.UserService;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class UserController {

  private final UserService userService;

  @PostMapping("/join")
  public ResponseEntity<?> join(
      @RequestBody @Valid JoinReqDto joinReqDto, BindingResult bindingResult) {

    if (bindingResult.hasErrors()) {
      Map<String, String> errorMap = new HashMap<>();

      for (FieldError error : bindingResult.getFieldErrors()) {
        errorMap.put(error.getField(), error.getDefaultMessage());
      }
      return new ResponseEntity<>(
          new ResponseDto<>(-1, "유효성 검사 실패", errorMap), HttpStatus.BAD_REQUEST);
    }

    JoinResDto joinResDto = userService.회원가입(joinReqDto);
    return new ResponseEntity<>(new ResponseDto<>(1, "회원가입 성공", joinResDto), HttpStatus.CREATED);
  }
}
