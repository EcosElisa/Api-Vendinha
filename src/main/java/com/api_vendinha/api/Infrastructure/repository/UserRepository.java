package com.api_vendinha.api.Infrastructure.repository;

import com.api_vendinha.api.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //Optional<User> findByName(String name);

    Optional<User> findByCpf(String cpf);
}
