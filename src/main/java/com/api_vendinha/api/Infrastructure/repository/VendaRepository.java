package com.api_vendinha.api.Infrastructure.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api_vendinha.api.domain.entities.User;
import com.api_vendinha.api.domain.entities.Venda;

public interface VendaRepository extends JpaRepository<Venda, Long> {
    List<Venda> findByUser(User user);
}