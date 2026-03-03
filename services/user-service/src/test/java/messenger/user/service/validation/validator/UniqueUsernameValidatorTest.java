package messenger.user.service.validation.validator;

import messenger.user.service.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UniqueUsernameValidatorTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UniqueUsernameValidator uniqueUsernameValidator;

    @Test
    void uniqueUsername_isValid_shouldReturnFalse_ifUsernameExists() {
        when(userRepository.existsByUsername("john_doe")).thenReturn(true);

        boolean result = uniqueUsernameValidator.isValid("john_doe", null);

        assertFalse(result);
    }

    @Test
    void uniqueUsername_isValid_shouldReturnTrue_ifUsernameNotExists() {
        when(userRepository.existsByUsername("jane_doe")).thenReturn(false);

        boolean result = uniqueUsernameValidator.isValid("jane_doe", null);

        assertTrue(result);
    }

    @Test
    void uniqueUsername_isValid_shouldReturnFalse_ifUsernameNullOrBlank() {
        assertFalse(uniqueUsernameValidator.isValid(null, null));
        assertFalse(uniqueUsernameValidator.isValid("", null));
        assertFalse(uniqueUsernameValidator.isValid("   ", null));
    }
}
