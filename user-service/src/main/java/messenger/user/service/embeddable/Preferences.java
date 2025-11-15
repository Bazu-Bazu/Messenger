package messenger.user.service.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import messenger.user.service.userEnum.Language;
import messenger.user.service.userEnum.Theme;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Preferences {

    @Builder.Default
    private boolean emailNotifications = true;

    @Builder.Default
    private boolean pushNotifications = true;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(length = 2)
    private Language language = Language.RU;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(length = 6)
    private Theme theme = Theme.SYSTEM;

}
