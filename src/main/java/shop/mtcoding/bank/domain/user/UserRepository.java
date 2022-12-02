package shop.mtcoding.bank.domain.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    // JpaRepository는 기본 CRUD를 다 제공해준다.
    // findById, findByAll, save(id 없으면 insert, id값이 있으면 update), deleteById
    // 기본 제공되지 않는 메서드는 아래에 생성한다.

    // Named Query : findBySomething 이라고 적으면 where절에 Something을 자동으로 걸어준다.
    // 하지만 헷갈리고 단점도 있으니까 우리는 그냥 쿼리를 작성하자.

    @Query("select u from User u where username = :username")
    Optional<User> findByUsername(@Param("username") String username);

    // userId를 fk로 들고있는 조건 충족 account 다 땡겨온다.
    @Query("select u from User u left join u.accounts ac on ac.isActive = true where u.id = :userId")
    User findByActiveUserIdv3(@Param("userId") Long userId);
}
