package vending.machine.VendingMachine.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import vending.machine.VendingMachine.models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
