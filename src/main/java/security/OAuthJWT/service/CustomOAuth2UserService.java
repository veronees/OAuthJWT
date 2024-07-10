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

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // OAuth2 서버 쪽에서 해당 유저의 정보를 가져온다.
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        //Naver, Kakao, Google 마다 응답 데이터 형식이 차이 있기 때문에 응답 형식을 통일시킨다.
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("naver")) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        } else {
            return null;
        }

        //Provider=서비스명, ProviderId=zDvFvOAV3UJb1OKVII10nnEvexwm0q2s7CK3DnmgbSc와 같은 해당 유저의 랜덤(?) 고유 Id값
        //해당 값으로 우리 서비스 db에 username으로 저장해서 해당 유저를 식별한다.
        String username = oAuth2Response.getProvider() +" " + oAuth2Response.getProviderId();

        //해당 username으로 db에 기존에 우리 서비스에 가입이 되어 있는지 확인한다.
        User findUser = userRepository.findByUsername(username);

        //기존에 가입이 안되어 있던 회원이면 User 엔티티를 생성해 db에 저장한다.
        if (findUser == null) {
            User newUser = User.builder()
                    .username(username)
                    .name(oAuth2Response.getName())
                    .email(oAuth2Response.getEmail())
                    .role("ROLE_USER")
                    .build();
            userRepository.save(newUser);

            //리턴할 CustomOAuth2User객체의 인자값인 UserDTO 값을 생성한다.
            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(username);
            userDTO.setName(oAuth2Response.getName());
            userDTO.setRole("ROLE_USER");


            return new CustomOauth2User(userDTO);

            //기존에 회원가입이 되어 있는 유저인 경우
        } else {
            //findUser객체의 name과 email을 업데이트 해준다.
            //해당 OAuth 서비스에서 정보를 수정했을 수 있기 때문에, 우리 db에도 업데이트를 해서 관리해준다.
            findUser.updateUserInfo(oAuth2Response.getName(), oAuth2Response.getEmail());

            //리턴할 CustomOAuth2User객체의 인자값인 UserDTO 값을 생성한다.
            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(findUser.getUsername());
            userDTO.setName(findUser.getName());
            userDTO.setRole(findUser.getRole());

            return new CustomOauth2User(userDTO);
        }
    }
}