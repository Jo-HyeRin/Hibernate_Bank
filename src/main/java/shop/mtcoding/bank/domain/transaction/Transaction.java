package shop.mtcoding.bank.domain.transaction;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.mtcoding.bank.config.enums.TransactionEnum;
import shop.mtcoding.bank.domain.AudingTime;
import shop.mtcoding.bank.domain.account.Account;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "transaction")
@Entity
public class Transaction extends AudingTime { // 거래내역

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)) // FK제약조건없애고 null 허용하기 위해서
    @ManyToOne(fetch = FetchType.LAZY)
    private Account withdrawAccount; // 출금계좌

    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)) // FK제약조건없애고 null 허용하기 위해서
    @ManyToOne(fetch = FetchType.LAZY)
    private Account depositAccount; // 입금계좌

    @Column(nullable = false)
    private Long amount; // 금액

    private Long withdrawAccountBalance; // 출금 후 잔액(null일수있다)
    private Long depositAccountBalance; // 입금 후 잔액(null일수있다)

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionEnum gubun; // 구분 : 출금(ATM으로 부터), 입금(ATM으로), 이체(다른계좌로)

    @Builder
    public Transaction(Long id, Account withdrawAccount, Account depositAccount, Long amount,
            Long withdrawAccountBalance, Long depositAccountBalance, TransactionEnum gubun) {
        this.id = id;
        this.withdrawAccount = withdrawAccount;
        this.depositAccount = depositAccount;
        this.amount = amount;
        this.withdrawAccountBalance = withdrawAccountBalance;
        this.depositAccountBalance = depositAccountBalance;
        this.gubun = gubun;
    }

}
