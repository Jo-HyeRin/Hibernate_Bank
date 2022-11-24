package shop.mtcoding.bank.config.exception;

import java.util.Map;

import lombok.Getter;

@Getter
public class CustomValidationApiException extends RuntimeException {

    // Validation Exception 상태코드는 모두 400번대
    // 굳이 받을 필요 없겠지만 400으로 설정해준다.
    private final int httpStatusCode = 400;

    // 에러 메시지를 함께 전닭.
    private Map<String, String> errorMap;

    public CustomValidationApiException(Map<String, String> errorMap) {
        super("유효성 검사 실패");
        this.errorMap = errorMap;
    }

}
