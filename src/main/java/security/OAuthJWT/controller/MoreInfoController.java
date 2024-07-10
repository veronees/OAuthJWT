package security.OAuthJWT.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import security.OAuthJWT.dto.CustomOauth2User;
import security.OAuthJWT.dto.UserMoreInfoDTO;
import security.OAuthJWT.entity.User;
import security.OAuthJWT.jwt.JWTUtil;
import security.OAuthJWT.service.UserService;

@RestController
@RequiredArgsConstructor
public class MoreInfoController {

    private final UserService userService;
    private final JWTUtil jwtUtil;

    @PostMapping("/more-info")
    public String moreInfoSave(@RequestBody UserMoreInfoDTO userMoreInfoDTO, @AuthenticationPrincipal OAuth2User oAuth2User, HttpServletResponse response) {
        CustomOauth2User customOauth2User = (CustomOauth2User) oAuth2User;
        User user = userService.saveMoreInfo(userMoreInfoDTO, customOauth2User.getUsername());

        String token = jwtUtil.createJwt(user.getName(), user.getUsername(), user.getRole(), 60 * 60 * 60L);
        Cookie cookie = new Cookie("Authorization", token);
        response.addCookie(cookie);
        return "유저의 추가 정보 저장 완료";
    }
}
