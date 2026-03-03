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
public class UniqueEmailValidatorTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UniqueEmailValidator uniqueEmailValidator;

    @Test
    void uniqueEmail_isValid_shouldReturnFalse_ifEmailExists() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        boolean result = uniqueEmailValidator.isValid("test@example.com", null);

        assertFalse(result);
    }

    @Test
    void uniqueEmail_isValid_shouldReturnTrue_ifEmailNotExists() {
        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);

        boolean result = uniqueEmailValidator.isValid("new@example.com", null);

        assertTrue(result);
    }

    @Test
    void uniqueEmail_isValid_shouldReturnFalse_ifEmailNullOrBlank() {
        assertFalse(uniqueEmailValidator.isValid(null, null));
        assertFalse(uniqueEmailValidator.isValid("", null));
        assertFalse(uniqueEmailValidator.isValid("   ", null));
    }
}
