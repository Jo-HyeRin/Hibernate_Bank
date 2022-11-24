package shop.mtcoding.bank.config.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

// 실행 시 발생하는 오류들은 미리 다 잡을 수 없다. 
// Runtime 시 오류가 발생할 때마다 내가 직접 잡아줄 수 있도록 해야 한다. 
// 그래서 CustomException을 만든다. 

@Getter
public class CustomApiException extends RuntimeException {

    private final HttpStatus httpStatus;

    public CustomApiException(String msg, HttpStatus httpStatus) {
        super(msg);
        this.httpStatus = httpStatus;
    }

}
