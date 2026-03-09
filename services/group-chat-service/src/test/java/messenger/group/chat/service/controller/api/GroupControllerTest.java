package messenger.group.chat.service.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import messenger.group.chat.service.dto.request.ChangeGroupInfoRequest;
import messenger.group.chat.service.dto.request.CreateGroupRequest;
import messenger.group.chat.service.dto.response.GroupResponse;
import messenger.group.chat.service.exception.mapper.ErrorMapper;
import messenger.group.chat.service.service.GroupService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GroupController.class)
@AutoConfigureMockMvc(addFilters = false)
class GroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GroupService groupChatService;

    @MockBean
    private ErrorMapper errorMapper;

    @Test
    void createGroupChat_success() throws Exception {
        CreateGroupRequest request =
                new CreateGroupRequest("group", "desc", List.of(2L, 3L), "https://avatar");

        GroupResponse response = GroupResponse.builder()
                .id(10L)
                .name("group")
                .description("desc")
                .createdBy(1L)
                .build();

        when(groupChatService.createGroupChat(eq(1L), any()))
                .thenReturn(response);

        mockMvc.perform(post("/chats/group")
                        .header("X-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10));

        verify(groupChatService).createGroupChat(eq(1L), any());
    }

    @Test
    void changeGroupInfo_success() throws Exception {
        ChangeGroupInfoRequest request =
                new ChangeGroupInfoRequest("newDesc","https://avatar","newName");

        GroupResponse response = GroupResponse.builder()
                .id(10L)
                .name("newName")
                .build();

        when(groupChatService.changeGroupInfo(eq(1L), eq(10L), any()))
                .thenReturn(response);

        mockMvc.perform(patch("/chats/group/10")
                        .header("X-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("newName"));

        verify(groupChatService).changeGroupInfo(eq(1L), eq(10L), any());
    }

    @Test
    void getAllUserGroupChats_success() throws Exception {
        when(groupChatService.getAllUserGroupChat(1L))
                .thenReturn(List.of());

        mockMvc.perform(get("/chats/group")
                        .header("X-User-Id", 1))
                .andExpect(status().isOk());

        verify(groupChatService).getAllUserGroupChat(1L);
    }

    @Test
    void deleteGroupChat_success() throws Exception {
        mockMvc.perform(delete("/chats/group/10")
                        .header("X-User-Id", 1))
                .andExpect(status().isNoContent());

        verify(groupChatService).deleteGroup(1L, 10L);
    }
}
