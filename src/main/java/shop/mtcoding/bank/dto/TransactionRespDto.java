package shop.mtcoding.bank.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.bank.domain.transaction.Transaction;
import shop.mtcoding.bank.dto.TransactionRespDto.TransactionListRespDto.TransactionDto;
import shop.mtcoding.bank.util.CustomDateUtil;

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

    @Setter
    @Getter
    public static class WithdrawRespDto {
        private Long id;
        private Long amount;
        private String gubun;
        private String from;
        private String to;
        private Long withdrawAccountBalance;

        public WithdrawRespDto(Transaction transaction) {
            this.id = transaction.getId();
            this.amount = transaction.getAmount();
            this.gubun = transaction.getGubun().getValue();
            this.from = transaction.getWithdrawAccount().getNumber() + "";
            this.to = "ATM";
            this.withdrawAccountBalance = transaction.getWithdrawAccountBalance();
        }
    }

    @Setter
    @Getter
    public static class TransferRespDto {
        private Long id;
        private Long amount;
        private String gubun;
        private String from;
        private String to;
        private Long withdrawAccountBalance;

        public TransferRespDto(Transaction transaction) {
            this.id = transaction.getId();
            this.amount = transaction.getAmount();
            this.gubun = transaction.getGubun().getValue(); // 이체
            this.from = transaction.getWithdrawAccount().getNumber() + "";
            this.to = transaction.getDepositAccount().getNumber() + "";
            this.withdrawAccountBalance = transaction.getWithdrawAccountBalance();
        }
    }

    @Setter
    @Getter
    public static class TransactionListRespDto {
        // DTO를 리스트에 넣어서 응답하지 말고 DTO를 응답하도록 하자.
        private List<TransactionDto> transactions = new ArrayList<>();

        public TransactionListRespDto(List<Transaction> transactions) {
            this.transactions = transactions.stream().map((transaction) -> new TransactionDto(transaction))
                    .collect(Collectors.toList());
        }

        @Setter
        @Getter
        public class TransactionDto {
            private Long id;
            private Long amount;
            private Long balance;
            private String gubun;
            private String createdAt;
            private String from;
            private String to;

            public TransactionDto(Transaction transaction) {
                this.id = transaction.getId();
                this.amount = transaction.getAmount();
                this.balance = transaction.getWithdrawAccountBalance();
                this.gubun = transaction.getGubun().getValue();
                this.createdAt = CustomDateUtil.toStringFormat(transaction.getCreatedAt());

                if (gubun.equals("WITHDRAW")) {
                    this.from = transaction.getWithdrawAccount().getNumber() + "";
                    this.to = "ATM";
                } else if (gubun.equals("DEPOSIT")) {
                    this.from = "ATM";
                    this.to = transaction.getDepositAccount().getNumber() + "";
                } else {
                    this.from = transaction.getWithdrawAccount().getNumber() + "";
                    this.to = transaction.getDepositAccount().getNumber() + "";
                }
            }
        }
    }

}
