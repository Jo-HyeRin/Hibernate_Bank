package shop.mtcoding.bank.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import shop.mtcoding.bank.config.enums.UserEnum;
import shop.mtcoding.bank.handler.CustomLoginHandler;

// SecurityFilterChain : 만들어져 있는 기본 필터 (약 14개)
// 해당 필터들을 커스터마이징해서 이용해보자.

@Configuration // 생성자 주입하지 말고 autowired 사용하도록.
public class SecurityConfig {

    @Autowired
    private CustomLoginHandler customLoginHandler;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() { // Security 라이브러리가 가지고 있는 비밀번호 보안 설정 코드(SHA 대체)
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { // IOC 컨테이너에 재등록

        http.headers().frameOptions().disable(); // iframe 으로 내 페이지 접근 금지
        http.csrf().disable(); // 로그인 페이지에 히든 값 심기 해제

        http.authorizeHttpRequests()
                .antMatchers("/api/transaction/**").authenticated() // authenticated : 로그인유저만 허용
                .antMatchers("/api/user/**").authenticated()
                .antMatchers("/api/account/**").authenticated()
                .antMatchers("/api/admin/**").hasRole("ROLE_" + UserEnum.ADMIN)
                // hasRole이 스트링타입이니까 앞에 문자열을 붙이고 뒤에 불러오면묵시적 형변환이 됨.
                // UserEnum.ADMIN.toString() 으로 넣어도 됨.
                .anyRequest().permitAll() // 위 주소를 제외한 나머지 주소는 모두에게 접근 허용.
                .and()
                .formLogin() // 로그인 시 디폴트 : x-www-form-urlencoded (post)
                // .usernameParameter("username") // 디폴트 값이 username, password
                // .passwordParameter("password") // 다르게 설정해도 됨.
                .loginProcessingUrl("/api/login") // login url 설정
                .successHandler(customLoginHandler) // login 성공 시
                .failureHandler(customLoginHandler); // login 실패 시

        // successHandler, failureHandler : 로그인 후 성공하든 실패하든 행위를 하고 싶은데 메서드를 받을 수 없다.
        // 둘의 타입을 임플먼트하는 클래스(LoginHandler)를 생성하고 주입 받음. 그리고 그 클래스의 메서드를 소환하여 설정.

        return http.build();
    }

}
