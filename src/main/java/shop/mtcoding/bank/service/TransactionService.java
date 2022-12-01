package shop.mtcoding.bank.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.bank.config.enums.TransactionEnum;
import shop.mtcoding.bank.config.exception.CustomApiException;
import shop.mtcoding.bank.domain.account.Account;
import shop.mtcoding.bank.domain.account.AccountRepository;
import shop.mtcoding.bank.domain.transaction.Transaction;
import shop.mtcoding.bank.domain.transaction.TransactionRepository;
import shop.mtcoding.bank.dto.TransactionReqDto.DepositReqDto;
import shop.mtcoding.bank.dto.TransactionRespDto.DepositRespDto;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TransactionService {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public DepositRespDto 입금하기(DepositReqDto depositReqDto) {
        // 검증 1. 구분이 입금인 지 확인 : 입금 아니면 kick
        if (TransactionEnum.valueOf(depositReqDto.getGubun()) != TransactionEnum.DEPOSIT) {
            throw new CustomApiException("구분값 검증 실패", HttpStatus.BAD_REQUEST);
        }
        // 검증 2. 입금 계좌가 있는 지 확인 : 계좌 없으면 kick
        Account depositAccountPS = accountRepository.findByNumber(depositReqDto.getNumber())
                .orElseThrow(() -> new CustomApiException("해당 계좌 없음", HttpStatus.BAD_REQUEST));

        // 검증 3. 입금 금액이 0원인 지 확인 : 0원이면 kick
        if (depositReqDto.getAmount() <= 0) {
            throw new CustomApiException("0원이 입금될 수 없습니다", HttpStatus.BAD_REQUEST);
        }

        // 실행 : 입금하기, 계좌잔액수정, 히스토리수정
        depositAccountPS.입금하기(depositReqDto.getAmount()); // 더티체킹 (update)
        Transaction transaction = depositReqDto.toEntity(depositAccountPS);
        Transaction transactionPS = transactionRepository.save(transaction);

        // DTO 응답
        return new DepositRespDto(transactionPS);
    }

}
