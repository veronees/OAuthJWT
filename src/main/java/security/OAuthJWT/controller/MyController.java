package security.OAuthJWT.controller;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import security.OAuthJWT.dto.CustomOauth2User;
import security.OAuthJWT.dto.UserDTO;

import java.util.Collection;
import java.util.Iterator;

@RestController
public class MyController {

    @GetMapping("/my")
    public UserDTO myAPI(@AuthenticationPrincipal CustomOauth2User customOauth2User) {
        String role = null;
        Collection<? extends GrantedAuthority> authorities = customOauth2User.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        if (iterator.hasNext()) {
            GrantedAuthority authority = iterator.next();
            role = authority.getAuthority();
        }

        UserDTO userDTO = UserDTO.builder()
                .role(role)
                .name(customOauth2User.getName())
                .username(customOauth2User.getUsername())
                .build();
        System.out.println("컨트롤러에서 customOauth2User에서 get한 Name 값 : " + customOauth2User.getName());
        System.out.println("======================================");
        System.out.println("name = " + userDTO.getName());
        System.out.println("username = " + userDTO.getUsername());
        System.out.println("role = " + userDTO.getRole());

        return userDTO;
    }
}