package com.example.bank.handler.ex;

public class CustomForbiddenException extends RuntimeException {

  public CustomForbiddenException(String message) {
    super(message);
  }
}
