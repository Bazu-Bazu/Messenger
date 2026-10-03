//package com.burkina.messenger.controller.api;
//
//import com.burkina.messenger.controller.GroupMemberController;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.burkina.messenger.domain.enums.GroupMemberRole;
//import com.burkina.messenger.dto.request.AddNewMembersRequest;
//import com.burkina.messenger.dto.request.RemoveMembersRequest;
//import com.burkina.messenger.dto.request.SetRolesRequest;
//import com.burkina.messenger.mapper.ErrorMapper;
//import com.burkina.messenger.service.GroupService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(GroupMemberController.class)
//@AutoConfigureMockMvc(addFilters = false)
//class GroupMemberControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockBean
//    private GroupService groupChatService;
//
//    @MockBean
//    private ErrorMapper errorMapper;
//
//    @Test
//    void addNewMembers_success() throws Exception {
//        AddNewMembersRequest request = new AddNewMembersRequest(List.of(2L,3L));
//
//        when(groupChatService.addNewMembers(eq(1L), eq(10L), any()))
//                .thenReturn(List.of());
//
//        mockMvc.perform(post("/chats/group/member/10")
//                        .header("X-User-Id", 1)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isCreated());
//
//        verify(groupChatService).addNewMembers(eq(1L), eq(10L), any());
//    }
//
//    @Test
//    void removeMembers_success() throws Exception {
//        RemoveMembersRequest request = new RemoveMembersRequest(List.of(2L));
//
//        mockMvc.perform(delete("/chats/group/member/10")
//                        .header("X-User-Id", 1)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isNoContent());
//
//        verify(groupChatService).removeMembers(eq(1L), eq(10L), any());
//    }
//
//    @Test
//    void getMembers_success() throws Exception {
//        when(groupChatService.getGroupMembers(eq(1L), eq(10L), any()))
//                .thenReturn(List.of());
//
//        mockMvc.perform(get("/chats/group/member/10")
//                        .header("X-User-Id", 1)
//                        .param("page", "0")
//                        .param("size", "10"))
//                .andExpect(status().isOk());
//
//        verify(groupChatService).getGroupMembers(eq(1L), eq(10L), any());
//    }
//
//    @Test
//    void setRoles_success() throws Exception {
//        SetRolesRequest request = new SetRolesRequest(List.of(2L), GroupMemberRole.ADMIN);
//
//        when(groupChatService.setRoles(eq(1L), eq(10L), any()))
//                .thenReturn(List.of());
//
//        mockMvc.perform(patch("/chats/group/member/10")
//                        .header("X-User-Id", 1)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk());
//
//        verify(groupChatService).setRoles(eq(1L), eq(10L), any());
//    }
//}
