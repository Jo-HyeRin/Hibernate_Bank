package shop.mtcoding.bank.dto;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.bank.config.enums.TransactionEnum;
import shop.mtcoding.bank.domain.account.Account;
import shop.mtcoding.bank.domain.transaction.Transaction;

public class TransactionReqDto {

    @Setter
    @Getter
    public static class DepositReqDto {
        private Long number;
        private Long amount;
        private String gubun;

        public Transaction toEntity(Account depositAccount) {
            Transaction transaction = Transaction.builder()
                    .withdrawAccount(null)
                    .depositAccount(depositAccount)
                    .depositAccountBalance(depositAccount.getBalance())
                    .amount(amount)
                    .gubun(TransactionEnum.valueOf(gubun))
                    .build();
            return transaction;
        }
    }

}
