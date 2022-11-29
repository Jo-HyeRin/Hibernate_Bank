package shop.mtcoding.bank.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.bank.config.exception.CustomApiException;
import shop.mtcoding.bank.domain.account.Account;
import shop.mtcoding.bank.domain.account.AccountRepository;
import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.domain.user.UserRepository;
import shop.mtcoding.bank.dto.AccountReqDto.AccountSaveReqDto;
import shop.mtcoding.bank.dto.AccountRespDto.AccountListRespDto;
import shop.mtcoding.bank.dto.AccountRespDto.AccountSaveRespDto;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AccountService {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Transactional
    public AccountSaveRespDto 계좌생성(AccountSaveReqDto accountSaveReqDto, Long userId) {
        log.debug("디버그 : 계좌생성 서비스 호출됨");
        User userPS = userRepository.findById(userId)
                .orElseThrow(
                        () -> new CustomApiException("탈퇴된 유저로 계좌를 생성할 수 없습니다", HttpStatus.FORBIDDEN));
        Account account = accountSaveReqDto.toEntity(userPS);
        Account accountPS = accountRepository.save(account);
        return new AccountSaveRespDto(accountPS);
    }

    public AccountListRespDto 본인_계좌목록보기(Long userId) {
        List<Account> accountListPS = accountRepository.findByActiveUserId(userId);
        return new AccountListRespDto(accountListPS);
    }
}
