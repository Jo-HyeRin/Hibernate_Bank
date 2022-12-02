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
import shop.mtcoding.bank.dto.TransactionReqDto.TransferReqDto;
import shop.mtcoding.bank.dto.TransactionReqDto.WithdrawReqDto;
import shop.mtcoding.bank.dto.TransactionRespDto.DepositRespDto;
import shop.mtcoding.bank.dto.TransactionRespDto.TransferRespDto;
import shop.mtcoding.bank.dto.TransactionRespDto.WithdrawRespDto;

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
        depositAccountPS.deposit(depositReqDto.getAmount()); // 더티체킹 (update)
        Transaction transaction = depositReqDto.toEntity(depositAccountPS);
        Transaction transactionPS = transactionRepository.save(transaction);

        // DTO 응답
        return new DepositRespDto(transactionPS);
    }

    // 출금하기 : /api/account/{number}/withdraw
    @Transactional
    public WithdrawRespDto 출금하기(WithdrawReqDto withdrawReqDto, Long number, Long userId) {
        // 검증 1. 구분 확인 : 출금 아니면 kick
        if (TransactionEnum.valueOf(withdrawReqDto.getGubun()) != TransactionEnum.WITHDRAW) {
            throw new CustomApiException("구분값 검증 실패", HttpStatus.BAD_REQUEST);
        }

        // 검증 2. 출금 계좌 확인 : 계좌 없으면 kick
        Account withdrawAccountPS = accountRepository.findByNumber(number)
                .orElseThrow(() -> new CustomApiException("해당 계좌 없음", HttpStatus.BAD_REQUEST));

        // 검증 3. 출금 금액이 0원인 지 확인 : 0원이면 kick
        if (withdrawReqDto.getAmount() <= 0) {
            throw new CustomApiException("0원이 출금될 수 없습니다", HttpStatus.BAD_REQUEST);
        }

        // 검증 4. 출금계좌 소유자 확인(number 계좌가 로그인 한 사람의 계좌가 맞는 지)
        withdrawAccountPS.isOwner(userId);

        // 검증 5. 출금계좌 비밀번호 확인
        withdrawAccountPS.checkPassword(withdrawReqDto.getPassword());

        // 출금하기 (계좌잔액수정, 트랜잭션 히스토리 인서트)
        withdrawAccountPS.withdraw(withdrawReqDto.getAmount()); // 더티체킹(영속화된것을수정했기때문)
        Transaction transaction = withdrawReqDto.toEntity(withdrawAccountPS);
        Transaction transactionPS = transactionRepository.save(transaction);

        // DTO 응답
        return new WithdrawRespDto(transactionPS);
    }

    @Transactional
    public TransferRespDto 이체하기(TransferReqDto transferReqDto, Long withDrawNumber, Long depositNumber, Long userId) {
        // 검증 1. 구분 확인 : 이체 아니면 kick
        if (TransactionEnum.valueOf(transferReqDto.getGubun()) != TransactionEnum.TRANSFER) {
            throw new CustomApiException("구분값 검증 실패", HttpStatus.BAD_REQUEST);
        }

        // 검증 2. 출금 계좌와 입금 계좌가 동일하면 kick
        if (withDrawNumber == depositNumber) {
            throw new CustomApiException("입출금 계좌가 동일할 수 없습니다", HttpStatus.BAD_REQUEST);
        }

        // 검증 3. 입금, 출금 계좌가 존재하는 지 확인
        Account withdrawAccountPS = accountRepository.findByNumber(withDrawNumber)
                .orElseThrow(() -> new CustomApiException("출금 계좌 없음", HttpStatus.BAD_REQUEST));

        Account depositAccountPS = accountRepository.findByNumber(depositNumber)
                .orElseThrow(() -> new CustomApiException("입금 계좌 없음", HttpStatus.BAD_REQUEST));

        // 검증 4. 0원 체크
        if (transferReqDto.getAmount() <= 0) {
            throw new CustomApiException("0원이 이체될 수 없습니다", HttpStatus.BAD_REQUEST);
        }

        // 검증 5. 출금계좌 소유자 확인(로그인 유저의 계좌가 맞는 지)
        withdrawAccountPS.isOwner(userId);

        // 검증 6. 출금계좌 비밀번호 확인
        withdrawAccountPS.checkPassword(transferReqDto.getPassword());

        // 이체하기 (출금계좌 금액변경, 입금계좌 금액변경, 트랜잭션 히스토리 생성)
        withdrawAccountPS.withdraw(transferReqDto.getAmount()); // 더티체킹
        depositAccountPS.deposit(transferReqDto.getAmount()); // 더티체킹
        Transaction transaction = transferReqDto.toEntity(withdrawAccountPS, depositAccountPS);
        Transaction transactionPS = transactionRepository.save(transaction);

        // DTO 응답
        return new TransferRespDto(transactionPS);
    }

}
