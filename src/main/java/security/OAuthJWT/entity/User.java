package security.OAuthJWT.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String name;
    private String email;
    private String role;
    private String likedFood;
    private String gender;

    public void updateUserInfo(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public void addMoreInfo(String likedFood, String gender) {
        this.likedFood = likedFood;
        this.gender = gender;
    }

    public void updateRole(String role) {
        this.role = role;
    }
}
