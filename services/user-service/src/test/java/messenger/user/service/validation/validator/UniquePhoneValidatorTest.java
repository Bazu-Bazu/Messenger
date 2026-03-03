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
public class UniquePhoneValidatorTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UniquePhoneValidator uniquePhoneValidator;

    @Test
    void uniquePhone_isValid_shouldReturnFalse_ifPhoneExists() {
        when(userRepository.existsByPhone("+1234567890")).thenReturn(true);

        boolean result = uniquePhoneValidator.isValid("+1234567890", null);

        assertFalse(result);
    }

    @Test
    void uniquePhone_isValid_shouldReturnTrue_ifPhoneNotExists() {
        when(userRepository.existsByPhone("+1987654321")).thenReturn(false);

        boolean result = uniquePhoneValidator.isValid("+1987654321", null);

        assertTrue(result);
    }

    @Test
    void uniquePhone_isValid_shouldReturnFalse_ifPhoneNullOrBlank() {
        assertFalse(uniquePhoneValidator.isValid(null, null));
        assertFalse(uniquePhoneValidator.isValid("", null));
        assertFalse(uniquePhoneValidator.isValid("   ", null));
    }
}
