package shop.mtcoding.bank.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> { // JpaRepository는 기본 CRUD를 다 제공해준다.

}
