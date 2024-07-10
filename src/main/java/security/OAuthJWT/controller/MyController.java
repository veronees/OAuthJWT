package security.OAuthJWT.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import security.OAuthJWT.dto.UserDTO;

@RestController
public class MyController {

    @GetMapping("/my")
    public UserDTO myAPI() {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("유저 name");
        userDTO.setUsername("유저 username");
        userDTO.setRole("유저 Role");
        return userDTO;
    }
}