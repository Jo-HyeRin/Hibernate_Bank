package shop.mtcoding.bank.config.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

public class CustomApiExceptionTest {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Test
    public void customApi_test() throws Exception {
        // given
        String msg = "해당 id가 없습니다.";
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        // when
        CustomApiException ex = new CustomApiException(msg, httpStatus);
        // log.debug("디버그 : " + ex.getMessage());
        // log.debug("디버그 : " + ex.getHttpStatus());
        // log.debug("디버그 : " + ex.getHttpStatus().value());

        // then
        assertThat(ex.getMessage()).isEqualTo("해당 id가 없습니다.");
        assertThat(ex.getHttpStatus().value()).isEqualTo(400);
    }
}
