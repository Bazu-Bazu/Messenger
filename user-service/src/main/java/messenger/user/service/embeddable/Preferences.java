package messenger.user.service.embeddable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import messenger.user.service.userEnum.Language;
import messenger.user.service.userEnum.Theme;

@Embeddable
public class Preferences {

    private boolean emailNotifications = true;
    private boolean pushNotifications = true;

    @Enumerated(EnumType.STRING)
    private Language language = Language.RU;

    @Enumerated(EnumType.STRING)
    private Theme theme = Theme.SYSTEM;

}
