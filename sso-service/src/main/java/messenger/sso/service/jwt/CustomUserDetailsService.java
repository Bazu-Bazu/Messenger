package messenger.sso.service.jwt;

import messenger.sso.service.domain.entity.SsoUser;
import messenger.sso.service.service.SsoUserService;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final SsoUserService ssoUserService;

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        SsoUser user = ssoUserService.findSsoUserByPhone(phone);

        if (!user.isEnabled()) {
            throw new DisabledException("Phone not verified");
        }

        return new CustomUserDetails(user);
    }

}