package shop.mtcoding.bank.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import shop.mtcoding.bank.config.exception.CustomApiException;
import shop.mtcoding.bank.dto.ResponseDto;

@RestControllerAdvice // advice 붙으면 exception 처리하는 것.
public class CustomExceptionHandler {

    @ExceptionHandler(CustomApiException.class) // CustomApiException 에서 throw 되는 것을 매개변수로 받아온다.
    public ResponseEntity<?> apiException(CustomApiException e) {
        System.out.println("에러의 제어권을 잡음");
        return new ResponseEntity<>(new ResponseDto<>(e.getMessage(), null), HttpStatus.BAD_REQUEST);

    }

}
