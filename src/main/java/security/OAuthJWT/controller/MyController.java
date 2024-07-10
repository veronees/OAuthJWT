package security.OAuthJWT.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import security.OAuthJWT.dto.CustomOauth2User;
import security.OAuthJWT.dto.UserDTO;

import java.util.Collection;
import java.util.Iterator;

@RestController
public class MyController {
    @GetMapping("/my-info")
    public UserDTO myAPI(@AuthenticationPrincipal OAuth2User oAuth2User) {
        String role = null;
        Collection<? extends GrantedAuthority> authorities = oAuth2User.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        if (iterator.hasNext()) {
            GrantedAuthority authority = iterator.next();
            role = authority.getAuthority();
        }

        CustomOauth2User customOauth2User = (CustomOauth2User) oAuth2User;

        UserDTO userDTO = UserDTO.builder()
                .role(role)
                .name(customOauth2User.getName())
                .username(customOauth2User.getUsername())
                .build();

        return userDTO;
    }
}