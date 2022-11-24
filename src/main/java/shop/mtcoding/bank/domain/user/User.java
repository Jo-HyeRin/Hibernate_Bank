package shop.mtcoding.bank.domain.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.mtcoding.bank.config.enums.UserEnum;
import shop.mtcoding.bank.domain.AudingTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE) // 하이버네이트만 뉴 할 수 있게 만들기
@Getter
@Table(name = "users")
@Entity
public class User extends AudingTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    private String username;

    @Column(nullable = false, length = 60)
    private String password;

    @Column(nullable = false, length = 50)
    private String email;

    @Enumerated(EnumType.STRING) // enum 을 스트링 타입으로 !
    @Column(unique = true, nullable = false)
    private UserEnum role; // 권한 : ADMIN, CUSTOMER

    @Builder
    public User(Long id, String username, String password, String email, UserEnum role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

}
