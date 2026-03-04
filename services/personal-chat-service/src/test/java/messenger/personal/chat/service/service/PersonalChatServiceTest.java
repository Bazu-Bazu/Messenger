package messenger.personal.chat.service.service;

import messenger.personal.chat.service.domain.entity.PersonalChat;
import messenger.personal.chat.service.domain.repository.PersonalChatRepository;
import messenger.personal.chat.service.dto.request.CreatePersonalChatRequest;
import messenger.personal.chat.service.dto.response.PersonalChatResponse;
import messenger.personal.chat.service.service.validator.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonalChatServiceTest {

    @Mock
    private PersonalChatRepository personalChatRepository;

    @Mock
    private PersonalChatCacheService personalChatCacheService;

    @Mock
    private UserValidator userValidator;

    @InjectMocks
    private PersonalChatService personalChatService;

    private PersonalChat chat;

    @BeforeEach
    void setUp() {
        chat = PersonalChat.builder()
                .id(1L)
                .user1Id(10L)
                .user2Id(20L)
                .createdAt(Instant.now())
                .lastActivityAt(Instant.now())
                .build();
    }

    @Test
    void shouldReturnExistingChatIfExists() {
        when(personalChatRepository.findPersonalChatByUsers(10L, 20L))
                .thenReturn(Optional.of(chat));

        PersonalChatResponse response =
                personalChatService.getOrCreatePersonalChat(
                        10L,
                        new CreatePersonalChatRequest(20L)
                );

        assertEquals(1L, response.getId());
        verify(personalChatRepository, never()).save(any());
        verify(personalChatCacheService, never()).evictUsersChats(any(), any());
    }

    @Test
    void shouldCreateChatIfNotExists() {
        when(personalChatRepository.findPersonalChatByUsers(10L, 20L))
                .thenReturn(Optional.empty());

        when(personalChatRepository.save(any()))
                .thenReturn(chat);

        PersonalChatResponse response =
                personalChatService.getOrCreatePersonalChat(
                        10L,
                        new CreatePersonalChatRequest(20L)
                );

        assertNotNull(response);
        verify(personalChatRepository).save(any());
        verify(personalChatCacheService)
                .evictUsersChats(10L, 20L);
    }

    @Test
    void shouldReturnAllUserChats() {
        when(personalChatRepository.findAllUserChats(10L))
                .thenReturn(List.of(chat));

        List<PersonalChatResponse> result =
                personalChatService.getAllUserPersonalChats(10L);

        assertEquals(1, result.size());
        assertEquals(10L, result.get(0).getUser1Id());
    }

    @Test
    void shouldDeleteChatAndEvictCache() {
        when(personalChatRepository.findById(1L))
                .thenReturn(Optional.of(chat));

        personalChatService.deletePersonalChat(10L, 1L);

        verify(userValidator)
                .validateUserHasRightsToTheChat(chat, 10L);

        verify(personalChatRepository).delete(chat);

        verify(personalChatCacheService)
                .evictUsersChats(10L, 20L);
    }

    @Test
    void shouldUpdateLastActivityAndEvictCache() {
        when(personalChatRepository.findById(1L))
                .thenReturn(Optional.of(chat));

        Instant now = Instant.now();

        personalChatService.updateLastActivity(1L, now);

        assertEquals(now, chat.getLastActivityAt());

        verify(personalChatCacheService)
                .evictUsersChats(10L, 20L);
    }

    @Test
    void shouldReturnTrueIfUserIsMember() {
        when(personalChatRepository
                .existsMemberByChatIdAndUserId(1L, 10L))
                .thenReturn(true);

        boolean result =
                personalChatService.isUserMember(1L, 10L);

        assertTrue(result);
    }

    @Test
    void shouldReturnAllMembers() {
        when(personalChatRepository.findUserIdsByChatId(1L))
                .thenReturn(List.of(10L, 20L));

        List<Long> members =
                personalChatService.getAllMembers(1L);

        assertEquals(2, members.size());
    }
}
