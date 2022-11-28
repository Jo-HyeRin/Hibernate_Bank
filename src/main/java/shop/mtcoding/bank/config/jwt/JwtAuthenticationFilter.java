package shop.mtcoding.bank.config.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.mtcoding.bank.config.auth.LoginUser;
import shop.mtcoding.bank.dto.UserReqDto.LoginReqDto;
import shop.mtcoding.bank.dto.UserRespDto.LoginRespDto;
import shop.mtcoding.bank.util.CustomResponseUtil;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        this.authenticationManager = authenticationManager;
    }

    // post의 /login
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        log.debug("디버그 : attemptAuthentication 요청됨");
        try {
            ObjectMapper om = new ObjectMapper();
            LoginReqDto loginReqDto = om.readValue(request.getInputStream(), LoginReqDto.class);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    loginReqDto.getUsername(),
                    loginReqDto.getPassword());

            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            return authentication;
        } catch (Exception e) {
            throw new InternalAuthenticationServiceException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {

        log.debug("디버그 : successfulAuthentication 요청됨");

        // 세션에 있는 UserDetails 가져오기
        LoginUser loginUser = (LoginUser) authResult.getPrincipal();
        // log.debug("디버그 : 세션UserDetails확인 : " + loginUser.getUsername());

        // 토큰 생성
        String jwtToken = JwtProcess.create(loginUser);

        // 토큰을 헤더에 추가
        response.addHeader(JwtProperties.HEADER_STRING, jwtToken);

        // 토큰 담아서 로그인 성공 응답하기
        LoginRespDto loginRespDto = new LoginRespDto(loginUser.getUser());
        CustomResponseUtil.success(response, "로그인 성공", loginRespDto);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {

        log.debug("디버그 : unsuccessfulAuthentication 요청됨");

        // 로그인 실패 응답하기
        CustomResponseUtil.fail(response, "로그인 실패");
    }

}

/*
 * 들어오는 JSON 값 읽고
 * 토큰을 생성
 * authenticationManager 호출하고 토큰 넣어준다
 * (UsernamePasswordAuthenticationFilter는 userdetailservice 실행함. )
 * authentication 객체 리턴 / 널 리턴
 * 
 * 로그인유저에서 값을 꺼낼 수 있다 = 로그인이 됐다 = 세션에 값이 있다
 * 토큰을 만들어준다
 * 헤더에 넣어준다
 * 
 * 로그인실패도 여기에서 제어해주어야 한다.
 */