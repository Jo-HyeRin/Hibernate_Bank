package shop.mtcoding.bank.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.bank.config.auth.LoginUser;
import shop.mtcoding.bank.config.exception.CustomApiException;
import shop.mtcoding.bank.dto.AccountReqDto.AccountSaveReqDto;
import shop.mtcoding.bank.dto.AccountRespDto.AccountListRespDto;
import shop.mtcoding.bank.dto.AccountRespDto.AccountSaveRespDto;
import shop.mtcoding.bank.dto.ResponseDto;
import shop.mtcoding.bank.service.AccountService;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class AccountApiController {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final AccountService accountService;

    // /api/account 는 인증 필요한 주소.
    @PostMapping("/account")
    public ResponseEntity<?> save(@RequestBody AccountSaveReqDto accountSaveReqDto,
            @AuthenticationPrincipal LoginUser loginUser) {
        // loginUser : securitycontextholder에 넣어둔 유저객체
        log.debug("디버그 : AccountApiController save 호출됨");
        AccountSaveRespDto accountSaveRespDto = accountService.계좌생성(accountSaveReqDto, loginUser.getUser().getId());
        return new ResponseEntity<>(new ResponseDto<>("계좌 생성 완료", accountSaveRespDto), HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}/account")
    public ResponseEntity<?> list(@PathVariable Long userId, @AuthenticationPrincipal LoginUser loginUser) {
        // 권한 체크
        if (userId != loginUser.getUser().getId()) {
            throw new CustomApiException("권한이 없습니다", HttpStatus.FORBIDDEN);
        }
        AccountListRespDto accountListRespDto = accountService.본인_계좌목록보기(userId);
        return new ResponseEntity<>(new ResponseDto<>("본인계좌목록보기 성공", accountListRespDto), HttpStatus.OK);
    }

}
