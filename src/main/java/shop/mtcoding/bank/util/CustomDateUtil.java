package shop.mtcoding.bank.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// 날짜 타입을 string으로 변경해서 사용하는 유틸

public class CustomDateUtil {

    public static String toStringFormat(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    }
}
