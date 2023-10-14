package com.example.bank.util;

import com.example.bank.dto.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomResponseUtil {

  private static final Logger log = LoggerFactory.getLogger(CustomResponseUtil.class);

  public static void unAuthentication(HttpServletResponse response, String msg) {
    try {
      ObjectMapper om = new ObjectMapper();
      ResponseDto<?> responseDto = new ResponseDto<>(-1, msg, null);
      String responseBody = om.writeValueAsString(responseDto);

      response.setContentType("application/json; charset=utf-8");
      response.setStatus(401);
      response.getWriter().print(responseBody);
    } catch (Exception e) {
      log.error("서버 파싱 에러");
    }
  }
}
