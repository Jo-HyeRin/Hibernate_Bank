package shop.mtcoding.bank.config.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import shop.mtcoding.bank.domain.user.User;

@Getter
@RequiredArgsConstructor
public class LoginUser implements UserDetails {

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> authorities = new ArrayList<>(); // Role이 여러 개 일 수 있으니까 List지만 우린 권한 하나만 이용할 것.
        authorities.add(() -> "ROLE_" + user.getRole());
        return authorities;
        // GrantedAuthority 는 메서드를 하나만 가지고 있으니 람다식 사용 가능.
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
