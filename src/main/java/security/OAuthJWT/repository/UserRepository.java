package security.OAuthJWT.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import security.OAuthJWT.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
}
