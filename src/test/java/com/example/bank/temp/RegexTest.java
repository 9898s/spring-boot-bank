package com.example.bank.temp;

import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

// java.util.regex.Pattern
public class RegexTest {

  @Test
  void 한글만된다_test() throws Exception {
    String value = "한글";
    boolean result = Pattern.matches("^[가-힣]+$", value);
    System.out.println("테스트: "+ result);
  }

  @Test
  void 한글은안된다_test() throws Exception {
    String value = "abc";
    boolean result = Pattern.matches("^[^ㄱ-ㅎ가-힣]*$", value);
    System.out.println("테스트: "+ result);
  }

  @Test
  void 영어만된다_test() throws Exception {
    String value = "abc";
    boolean result = Pattern.matches("^[a-zA-Z]+$", value);
    System.out.println("테스트: "+ result);
  }

  @Test
  void 영어만안된다_test() throws Exception {
    String value = "가22";
    boolean result = Pattern.matches("^[^a-zA-Z]*$", value);
    System.out.println("테스트: "+ result);
  }

  @Test
  void 영어와숫자만된다_test() throws Exception {
    String value = "asd123!";
    boolean result = Pattern.matches("^[a-zA-Z0-9]+$", value);
    System.out.println("테스트: "+ result);
  }

  @Test
  void 영어만되고_길이는최소2최대4이다_test() throws Exception {
    String value = "abc";
    boolean result = Pattern.matches("^[a-zA-Z]{2,4}$", value);
    System.out.println("테스트: "+ result);
  }
}
