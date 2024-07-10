package security.OAuthJWT.oauth2;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import security.OAuthJWT.jwt.JWTUtil;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //소셜 로그인에 성공했을 경우 인증된 Authentication을 OAuth2AuthenticationToken으로 캐스팅한다.
        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;

        //OAuth2AuthenticationToken에서 principal을 가져와 그 안의 name값을 꺼낸다.
        OAuth2User principal = authToken.getPrincipal();
        String username = principal.getName();

        //OAuth2AuthenticationToken에서 Authorities를 꺼낸다.
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        //꺼낸 authorities에서 role을 꺼낸다.
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        //인증된 Authentication객체에서 얻은 username과 role로 JWT token을 생성한다.
        String token = jwtUtil.createJwt(username, role, 60*60*60L);

        //JWT token을 헤더 쿠키에 넣어서 응답한다.
        response.addCookie(createCookie("Authorization", token));
        response.sendRedirect("http://localhost:3000/");
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*60);
        //cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}