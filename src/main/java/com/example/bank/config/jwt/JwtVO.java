package com.example.bank.config.jwt;

/**
 * SECRET 노출되면 안된다. (클라우드 AWS - 환경변수, 파일에 있는 것을 읽을 수도 있음)
 * Refresh Token
 */
public interface JwtVO {

  public static final String SECRET = "블라블라"; // HS256(대칭키)
  public static final int EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7; // 일주일
  public static final String TOKEN_PREFIX = "Bearer ";
  private static final String HEADER = "Authorization";
}
