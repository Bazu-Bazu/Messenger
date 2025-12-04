package messenger.sso.service.service;

import messenger.sso.service.dto.CustomUserDetails;
import messenger.sso.service.entity.SsoUser;
import messenger.sso.service.exception.UserException;
import messenger.sso.service.repository.SsoUserRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final SsoUserRepository ssoUserRepository;

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        SsoUser user = ssoUserRepository.findByPhone(phone)
                .orElseThrow(() -> new UserException(
                        String.format("User with phone %s not found", phone)
                ));

        if (!user.isEnabled()) {
            throw new DisabledException("Phone not verified");
        }

        return new CustomUserDetails(user);
    }

}