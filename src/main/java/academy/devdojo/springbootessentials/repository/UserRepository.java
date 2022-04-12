package academy.devdojo.springbootessentials.repository;

import academy.devdojo.springbootessentials.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);
}
