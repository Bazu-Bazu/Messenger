package messenger.sso.service.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sso_users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SsoUser {

    @Id
    private Long id;

    @Column(nullable = false, unique = true, length = 25)
    private String username;

    @Column(unique = true, nullable = false, length = 15)
    private String phone;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Builder.Default
    private boolean enabled = true;

}
