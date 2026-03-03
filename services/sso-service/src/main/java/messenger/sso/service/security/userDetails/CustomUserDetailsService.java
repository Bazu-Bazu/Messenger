package messenger.sso.service.security.userDetails;

import messenger.sso.service.domain.entity.SsoUser;
import messenger.sso.service.exception.SsoUserNotFoundException;
import messenger.sso.service.exception.UserIsNotEnabledException;
import messenger.sso.service.service.SsoUserService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final SsoUserService ssoUserService;

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        try {
            SsoUser user = ssoUserService.findSsoUserByPhone(phone);

            if (!user.isEnabled()) {
                throw new UserIsNotEnabledException(
                        String.format("User %d is not enabled", user.getId())
                );
            }

            return new CustomUserDetails(user);
        } catch (SsoUserNotFoundException e) {
            throw new BadCredentialsException("Invalid username");
        }
    }
}