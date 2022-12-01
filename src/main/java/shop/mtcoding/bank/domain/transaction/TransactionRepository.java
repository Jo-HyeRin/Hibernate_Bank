package shop.mtcoding.bank.domain.transaction;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long>, Dao {
    // 인터페이스가 다른 인터페이스를 상속하기 위해서는 extends를 사용.

}
