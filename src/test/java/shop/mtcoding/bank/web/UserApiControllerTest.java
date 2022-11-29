package shop.mtcoding.bank.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.mtcoding.bank.dto.UserReqDto.JoinReqDto;

// @Transactional // 메서드실행할때마다 롤백되는 springframework 어노테이션 
// -> 우리는 PK 자동생성때문에 autoincrement 자동 초기화가 필요하다. truncate 사용할것.

@ActiveProfiles("test")
@AutoConfigureMockMvc // MockMvc 주입
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class UserApiControllerTest {

    private static final String APPLICATION_JSON_UTF8 = "application/json; charset=utf-8";
    private static final String APPLICATION_FORM_URLENCODED = "application/x-www-form-urlencoded; charset=utf-8";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @Test
    public void join_test() throws Exception {
        // given
        JoinReqDto joinReqDto = new JoinReqDto();
        joinReqDto.setUsername("ssar");
        joinReqDto.setPassword("1234");
        joinReqDto.setEmail("ssar@nate.com");
        joinReqDto.setFullName("쌀");
        // JSON으로 변환
        String requestBody = om.writeValueAsString(joinReqDto);

        // when
        ResultActions resultActions = mvc
                .perform(MockMvcRequestBuilders.post("/api/join").content(requestBody)
                        .contentType(APPLICATION_JSON_UTF8));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("디버그: " + responseBody);

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isCreated());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.data.username").value("ssar"));

    }

}
