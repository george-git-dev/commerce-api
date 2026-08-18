package br.com.george.commerce.repository;

import br.com.george.commerce.entity.User;
import br.com.george.commerce.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    long countByRole(Role role);

    Optional<User> findByCpf(String cpf);

}