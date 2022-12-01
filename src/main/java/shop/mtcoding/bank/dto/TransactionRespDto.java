package shop.mtcoding.bank.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

        @JsonIgnore // Json 파싱 막는 어노테이션 = 컨트롤러에선 안보이고 서비스에선 보임
        // @JsonIgnore on 상태면 : 입금후잔액 데이터 컨트롤러에선 안보이고 서비스에선 보임 = 테스트 시 안 보임
        private Long depositAccountBalance; // 입금후잔액 : 사용자에게 주면 안 되는 값. 확인용

        public DepositRespDto(Transaction transaction) {
            this.id = transaction.getId();
            this.amount = transaction.getAmount();
            this.gubun = transaction.getGubun().getValue();
            this.from = "ATM";
            this.to = transaction.getDepositAccount().getNumber() + "";
            this.depositAccountBalance = transaction.getDepositAccountBalance();
        }
    }
}
