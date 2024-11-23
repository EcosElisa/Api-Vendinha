package com.api_vendinha.api.Infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api_vendinha.api.domain.entities.Produto;
import com.api_vendinha.api.domain.entities.User;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    Optional<Produto> findByName(String name);
    List<Produto> findByUser(User user);
}
