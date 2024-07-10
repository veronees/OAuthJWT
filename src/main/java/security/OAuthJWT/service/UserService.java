package security.OAuthJWT.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import security.OAuthJWT.dto.UserMoreInfoDTO;
import security.OAuthJWT.entity.User;
import security.OAuthJWT.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User saveMoreInfo(UserMoreInfoDTO userMoreInfoDTO, String username) {
        User user = userRepository.findByUsername(username);
        user.addMoreInfo(userMoreInfoDTO.getLikedFood(), userMoreInfoDTO.getGender());
        user.updateRole("ROLE_USER2");

        return user;
    }
}
