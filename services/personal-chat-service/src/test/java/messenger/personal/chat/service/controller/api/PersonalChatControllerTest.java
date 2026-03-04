package messenger.personal.chat.service.controller.api;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import messenger.personal.chat.service.dto.request.CreatePersonalChatRequest;
import messenger.personal.chat.service.dto.response.PersonalChatResponse;
import messenger.personal.chat.service.exception.mapper.ErrorMapper;
import messenger.personal.chat.service.service.PersonalChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@WebMvcTest(PersonalChatController.class)
@AutoConfigureMockMvc(addFilters = false)
class PersonalChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PersonalChatService personalChatService;

    @MockBean
    private ErrorMapper errorMapper;

    @Test
    void shouldReturnChatWhenCreateOrGet() throws Exception {
        Long userId = 10L;

        CreatePersonalChatRequest request = new CreatePersonalChatRequest(20L);

        PersonalChatResponse response = PersonalChatResponse.builder()
                        .id(1L)
                        .user1Id(10L)
                        .user2Id(20L)
                        .build();

        when(personalChatService.getOrCreatePersonalChat(userId, request))
                .thenReturn(response);

        mockMvc.perform(post("/chats/personal")
                        .header("X-User-Id", String.valueOf(userId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.user1Id").value(10L))
                .andExpect(jsonPath("$.user2Id").value(20L));

        verify(personalChatService).getOrCreatePersonalChat(eq(userId), any());
    }

    @Test
    void shouldReturnAllUserChats() throws Exception {
        Long userId = 10L;

        PersonalChatResponse response =
                PersonalChatResponse.builder()
                        .id(1L)
                        .user1Id(10L)
                        .user2Id(20L)
                        .build();

        when(personalChatService.getAllUserPersonalChats(userId))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/chats/personal")
                        .header("X-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].user1Id").value(10L));

        verify(personalChatService)
                .getAllUserPersonalChats(userId);
    }

    @Test
    void shouldDeleteChat() throws Exception {
        Long userId = 10L;
        Long chatId = 1L;

        mockMvc.perform(delete("/chats/personal/{chatId}", chatId)
                        .header("X-User-Id", userId))
                .andExpect(status().isNoContent());

        verify(personalChatService)
                .deletePersonalChat(userId, chatId);
    }
}