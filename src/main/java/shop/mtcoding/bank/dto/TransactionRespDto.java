package shop.mtcoding.bank.dto;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.bank.domain.transaction.Transaction;

public class TransactionRespDto {

    @Setter
    @Getter
    public static class DepositRespDto {
        private Long id;
        private Long amount;
        private String gubun;
        private String from;
        private String to;

        public DepositRespDto(Transaction transaction) {
            this.id = transaction.getId();
            this.amount = transaction.getAmount();
            this.gubun = transaction.getGubun().getValue();
            this.from = "ATM";
            this.to = transaction.getDepositAccount().getNumber() + "";
        }
    }
}
