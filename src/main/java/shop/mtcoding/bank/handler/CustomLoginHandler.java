package shop.mtcoding.bank.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.mtcoding.bank.dto.ResponseDto;

// 필터에서 터진 건 핸들러가 낚아챌 수 없다.
// throw new CustomApiException("로그인 실패", HttpStatus.BAD_REQUEST); 불가능 !

@Component
public class CustomLoginHandler implements AuthenticationSuccessHandler, AuthenticationFailureHandler {

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                        Authentication authentication) throws IOException, ServletException {

                System.out.println("로그인이 성공하였습니다.");

        }

        // ControllerAdvice가 제어하지 못하는 것을 내가 직접 제어하는 것.
        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                        AuthenticationException exception) throws IOException, ServletException {
                System.out.println("로그인이 실패하였습니다.");
                ObjectMapper om = new ObjectMapper();
                ResponseDto<?> responseDto = new ResponseDto<>("로그인 실패", null);
                String responseBody = om.writeValueAsString(responseDto);
                response.setContentType("application/json; charset=utf-8");
                response.setStatus(400);
                response.getWriter().println(responseBody);
        }

}
