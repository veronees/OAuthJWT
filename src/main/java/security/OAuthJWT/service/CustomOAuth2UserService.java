package security.OAuthJWT.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import security.OAuthJWT.dto.CustomOauth2User;
import security.OAuthJWT.dto.NaverResponse;
import security.OAuthJWT.dto.OAuth2Response;
import security.OAuthJWT.dto.UserDTO;
import security.OAuthJWT.entity.User;
import security.OAuthJWT.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        System.out.println("oAuth2User = " + oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2Response oAuth2Response = null;

        if (registrationId.equals("naver")) {

            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());

        } else {
            return null;
        }

        // 추후 작성

        String username = oAuth2Response.getProvider() +" " + oAuth2Response.getProviderId();

        User findUser = userRepository.findByUsername(username);

        if (findUser == null) {
            User newUser = User.builder()
                    .username(username)
                    .name(oAuth2Response.getName())
                    .email(oAuth2Response.getEmail())
                    .role("ROLE_USER")
                    .build();
            userRepository.save(newUser);

            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(username);
            userDTO.setName(oAuth2Response.getName());
            userDTO.setRole("ROLE_USER");

            return new CustomOauth2User(userDTO);
        } else {
            findUser.updateUserInfo(oAuth2Response.getName(), oAuth2Response.getEmail());

            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(findUser.getUsername());
            userDTO.setName(findUser.getName());
            userDTO.setRole(findUser.getRole());

            return new CustomOauth2User(userDTO);
        }
    }
}