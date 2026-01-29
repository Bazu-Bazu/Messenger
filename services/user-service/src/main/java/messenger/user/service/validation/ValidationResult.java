package messenger.user.service.validation;

import java.util.ArrayList;
import java.util.List;

public record ValidationResult(boolean valid, List<String> errors) {

    public ValidationResult(boolean valid, List<String> errors) {
        this.valid = valid;
        this.errors = errors != null ? errors : new ArrayList<>();
    }

}
