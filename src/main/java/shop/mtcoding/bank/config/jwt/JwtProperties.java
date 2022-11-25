package shop.mtcoding.bank.config.jwt;

// 노출되면 안 되는 값들이니 실제 이용 시 변수 이용하도록.

public interface JwtProperties {
    String SECRET = "메타코딩"; // 우리 서버만 알고 있는 비밀값
    int EXPIRATION_TIME = 864000000; // 10일 (1/1000초)
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}