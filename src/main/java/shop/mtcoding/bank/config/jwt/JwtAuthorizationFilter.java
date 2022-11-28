package shop.mtcoding.bank.config.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import shop.mtcoding.bank.config.auth.LoginUser;

/*
 * JwtAuthorizationFilter는 모든 주소에서 동작한다.
 */

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final Logger log = LoggerFactory.getLogger(getClass());

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        log.debug("디버그 : doFilterInternal 요청됨");

        // 1 헤더 검증
        if (isHeaderVerify(request, response)) { // 헤더가 있으면
            // log.debug("디버그 : 헤더 있음");

            // 2 토큰 파싱 (Bearer 없애기) : Bearer는 JWT 토큰이라는 것을 알려주는 프로토콜이라 사용하는데 파싱에는 필요없으니 지워준다
            String token = request.getHeader(JwtProperties.HEADER_STRING)
                    .replace(JwtProperties.TOKEN_PREFIX, "");
            // log.debug("디버그-error : token : " + token);

            // 3 토큰 검증
            LoginUser loginUser = JwtProcess.verify(token);
            // log.debug("디버그 : loginUser : " + loginUser.getUser());

            // 4 임시 세션 생성
            Authentication authentication = new UsernamePasswordAuthenticationToken(loginUser,
                    null, loginUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("디버그 : 토큰 검증 완료, 필터 탐");
        }

        // 헤더가 없으면 그냥 넘어감.
        log.debug("디버그 : 토큰 검증 실패, 필터 탐. 세션 없어서 시큐리티 필터가 컨트롤러 진입 막음");
        chain.doFilter(request, response);

    }

    // 헤더 검증
    private boolean isHeaderVerify(HttpServletRequest request, HttpServletResponse response) {

        String header = request.getHeader(JwtProperties.HEADER_STRING);

        if (header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
            return false;
        } else {
            return true;
        }
    }

}

/*
 * BasicAuthenticationFilter는 인가필터.
 * 원래는 헤더에서 JSESSIONID를 받아서 세션에서 체크하는 개념인데
 * 우리는 토큰을 만들어서 넘겨주기 때문에 토큰을 체크하고
 * 토큰을 가진 객체를 세션에 넣어주는 코드로 바꿔야한다.
 */

/*
 * 어차피 안으로 들어가도 팅겨나올거니까 . 여기서 팅궈내도 상관없음
 * 아래코드가 더이상 실행되지 않게 return 해줘야한다.
 * 
 * 강제로 세션을 만들고 토큰을 세션에 저장하는 개념. 왜 세션을 만드나?
 * 시큐리티는 세션을 확인해서 작동.
 * 세션은 시큐리티 작동시켜 권한처리도움을 받기 위해 만들었다가 시큐리티 작동이 끝나면 세션을 했다고 생각하면 됨.
 * 
 * 토큰에 비밀번호가 그대로 들어가면 안되기 때문에
 * 디비에서 유저네임으로 찾아서 정보를 가져오고 그 정보로 만들어야 한다.
 */
