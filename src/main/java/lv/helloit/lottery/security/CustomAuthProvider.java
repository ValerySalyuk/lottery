package lv.helloit.lottery.security;

import lv.helloit.lottery.admin.Admin;
import lv.helloit.lottery.admin.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.token.Sha512DigestUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;

@Component
public class CustomAuthProvider implements AuthenticationProvider {

    private final AdminService adminService;
    private final SecurityProperties securityProperties;

    @Autowired
    public CustomAuthProvider(AdminService adminService, SecurityProperties securityProperties) {
        this.adminService = adminService;
        this.securityProperties = securityProperties;
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String login = authentication.getName();
        String password = authentication.getCredentials().toString();

        Optional<Admin> wrappedAdmin = adminService.getByLogin(login);

        if (wrappedAdmin.isPresent()) {

            String realPasswordHash = wrappedAdmin.get().getPasswordHash();
            String incomingPasswordHash = Sha512DigestUtils.shaHex(password + securityProperties.getSalt());

            if(realPasswordHash.equals(incomingPasswordHash)) {
                return new UsernamePasswordAuthenticationToken(login, password, new ArrayList<>());
            }

        }

        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class == authentication;
    }
}
