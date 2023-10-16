package com.example.bank.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomDateUtil {

  public static String toStringForamt(LocalDateTime localDateTime) {
    return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
  }
}
