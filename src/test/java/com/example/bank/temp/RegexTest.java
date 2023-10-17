package com.example.bank.temp;

import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

// java.util.regex.Pattern
public class RegexTest {

  @Test
  void 한글만된다_test() throws Exception {
    String value = "한글";
    boolean result = Pattern.matches("^[가-힣]+$", value);
    System.out.println("테스트: " + result);
  }

  @Test
  void 한글은안된다_test() throws Exception {
    String value = "abc";
    boolean result = Pattern.matches("^[^ㄱ-ㅎ가-힣]*$", value);
    System.out.println("테스트: " + result);
  }

  @Test
  void 영어만된다_test() throws Exception {
    String value = "abc";
    boolean result = Pattern.matches("^[a-zA-Z]+$", value);
    System.out.println("테스트: " + result);
  }

  @Test
  void 영어만안된다_test() throws Exception {
    String value = "가22";
    boolean result = Pattern.matches("^[^a-zA-Z]*$", value);
    System.out.println("테스트: " + result);
  }

  @Test
  void 영어와숫자만된다_test() throws Exception {
    String value = "asd123!";
    boolean result = Pattern.matches("^[a-zA-Z0-9]+$", value);
    System.out.println("테스트: " + result);
  }

  @Test
  void 영어만되고_길이는최소2최대4이다_test() throws Exception {
    String value = "abc";
    boolean result = Pattern.matches("^[a-zA-Z]{2,4}$", value);
    System.out.println("테스트: " + result);
  }

  @Test
  void user_username_test() throws Exception {
    String username = "ssar";
    boolean result = Pattern.matches("^[a-zA-Z0-9]{2,20}$", username);
    System.out.println("테스트: " + result);
  }

  @Test
  void user_fullname_test() throws Exception {
    String username = "쌀";
    boolean result = Pattern.matches("^[a-zA-Z가-힣]{1,20}$", username);
    System.out.println("테스트: " + result);
  }

  @Test
  void user_email_test() throws Exception {
    String username = "ssar@nate.com";
    boolean result =
        Pattern.matches("^[a-zA-Z0-9]{2,6}@[a-zA-Z0-9]{2,6}\\.[a-zA-Z]{2,3}$", username);
    System.out.println("테스트: " + result);
  }

  @Test
  void account_gubun_test1() throws Exception {
    String gubun = "DEPOSIT";
    boolean result =
        Pattern.matches("^(DEPOSIT)$", gubun);
    System.out.println("테스트: " + result);
  }

  @Test
  void account_gubun_test2() throws Exception {
    String gubun = "TRANSFER";
    boolean result =
        Pattern.matches("^(DEPOSIT|TRANSFER)$", gubun);
    System.out.println("테스트: " + result);
  }

  @Test
  void account_tel_test1() throws Exception {
    String tel = "010-1111-2222";
    boolean result =
        Pattern.matches("^[0-9]{3}-[0-9]{4}-[0-9]{4}", tel);
    System.out.println("테스트: " + result);
  }

  @Test
  void account_tel_test2() throws Exception {
    String tel = "01011112222";
    boolean result =
        Pattern.matches("^[0-9]{11}", tel);
    System.out.println("테스트: " + result);
  }
}
