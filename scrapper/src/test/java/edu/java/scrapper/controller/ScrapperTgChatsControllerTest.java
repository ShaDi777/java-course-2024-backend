package edu.java.scrapper.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.controllers.ScrapperTgChatsController;
import edu.java.exceptions.ChatAlreadyExistsException;
import edu.java.exceptions.ResourceNotFoundException;
import edu.java.services.TgChatService;
import edu.java.services.ratelimit.FixedRateLimitService;
import edu.java.services.ratelimit.RateLimitService;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
    value = ScrapperTgChatsController.class,
    properties = {
        "app.rate-limiter.enable=true",
        "app.rate-limiter.limit=5",
        "app.rate-limiter.refill-per-minute=5",
    }
)
@Import({FixedRateLimitService.class})
public class ScrapperTgChatsControllerTest {
    private static final long TG_CHAT_ID = 1L;

    private static final int LIMIT = 5;
    private static final String REMOTE_ADDR = "192.168.0.1";

    @Autowired private RateLimitService rateLimitService;
    @MockBean private TgChatService chatService;
    @Autowired private MockMvc mockMvc;

    @AfterEach
    public void clearBucket() {
        rateLimitService.clearAllBuckets();
    }

    @Test
    void post_shouldReturnOkForCorrectRequest() throws Exception {
        Mockito.doNothing().when(chatService).register(TG_CHAT_ID);
        doRequest(MockMvcRequestBuilders::post).andExpect(status().isOk());
    }

    @Test
    void post_shouldReturnNotFoundIfNotFoundException() throws Exception {
        Mockito.doThrow(ChatAlreadyExistsException.class)
               .when(chatService)
               .register(TG_CHAT_ID);

        doRequest(MockMvcRequestBuilders::post).andExpect(status().isBadRequest());
    }

    @Test
    void post_shouldReturnTooManyRequestsWhenLimitExhausted() throws Exception {
        Mockito.doNothing()
               .when(chatService)
               .register(TG_CHAT_ID);

        for (int i = 0; i < LIMIT; i++) {
            doRequest(MockMvcRequestBuilders::post).andExpect(status().isOk());
        }
        doRequest(MockMvcRequestBuilders::post).andExpect(status().isTooManyRequests());
    }

    @Test
    void delete_shouldReturnOkForCorrectRequest() throws Exception {
        Mockito.doNothing().when(chatService).unregister(TG_CHAT_ID);
        doRequest(MockMvcRequestBuilders::delete).andExpect(status().isOk());
    }

    @Test
    void delete_shouldReturnNotFoundIfNotFoundException() throws Exception {
        Mockito.doThrow(ResourceNotFoundException.class)
               .when(chatService)
               .unregister(TG_CHAT_ID);

        doRequest(MockMvcRequestBuilders::delete).andExpect(status().isNotFound());
    }

    @Test
    void delete_shouldReturnTooManyRequestsWhenLimitExhausted() throws Exception {
        Mockito.doNothing()
               .when(chatService)
               .unregister(TG_CHAT_ID);

        for (int i = 0; i < LIMIT; i++) {
            doRequest(MockMvcRequestBuilders::delete).andExpect(status().isOk());
        }
        doRequest(MockMvcRequestBuilders::delete).andExpect(status().isTooManyRequests());
    }

    /*
    @Test
    void delete_shouldReturnTooManyRequestsWhenLimitExhausted() throws Exception {

        OngoingStubbing<LinkInfoDto> stubbing = Mockito.when(linkService.remove(TG_CHAT_ID, URL));

        for (int i = 0; i < LIMIT; i++) {
            stubbing = stubbing.thenReturn(LINK_INFO_DTO);
            doRequest(MockMvcRequestBuilders::delete).andExpect(status().isOk());
        }
        stubbing.thenReturn(null);
        doRequest(MockMvcRequestBuilders::delete).andExpect(status().isTooManyRequests());
    }

    @Test
    void post_shouldReturnOkForCorrectRequest() throws Exception {
        Mockito.when(linkService.add(TG_CHAT_ID, URL))
               .thenReturn(LINK_INFO_DTO);

        doRequest(MockMvcRequestBuilders::post).andExpect(status().isOk());
    }

    @Test
    void post_shouldReturnNotFoundIfNotFoundException() throws Exception {
        Mockito.when(linkService.add(TG_CHAT_ID, URL))
               .thenThrow(ResourceNotFoundException.chatNotFound(TG_CHAT_ID));

        doRequest(MockMvcRequestBuilders::post).andExpect(status().isNotFound());
    }

    @Test
    void post_shouldReturnTooManyRequestsWhenLimitExhausted() throws Exception {
        OngoingStubbing<LinkInfoDto> stubbing = Mockito.when(linkService.add(TG_CHAT_ID, URL));

        for (int i = 0; i < LIMIT; i++) {
            stubbing = stubbing.thenReturn(LINK_INFO_DTO);
            doRequest(MockMvcRequestBuilders::post).andExpect(status().isOk());
        }
        stubbing.thenReturn(null);
        doRequest(MockMvcRequestBuilders::post).andExpect(status().isTooManyRequests());
    }
*/
    @NotNull
    private ResultActions doRequest(Function<String, MockHttpServletRequestBuilder> operation) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return mockMvc.perform(operation.apply("/tg-chat/" + TG_CHAT_ID).with(request -> {
                                            request.setRemoteAddr(REMOTE_ADDR);
                                            return request;
                                        })
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .accept(MediaType.APPLICATION_JSON));
    }
}
