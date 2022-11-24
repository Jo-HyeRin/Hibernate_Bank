package shop.mtcoding.bank.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 변수가 변경될 일이 있나? -> 없다.
// 그럼 상수 -> final 

@RequiredArgsConstructor
@Getter
public class ResponseDto<T> {
    private final String msg;
    private final T data;
}

// http code = 200(get, delete, put, login은 post라도 200에서 확인), 201(post) 만 성공.
// 나머지는 다 실패.
