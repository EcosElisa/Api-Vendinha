package com.api_vendinha.api.domain.service;

import com.api_vendinha.api.domain.dtos.request.UserRequestDto;
import com.api_vendinha.api.domain.dtos.response.UserResponseDto;
import com.api_vendinha.api.domain.entities.User;

import java.util.List;

public interface UserServiceInterface {
    UserResponseDto save(UserRequestDto userRequestDto);
    UserResponseDto update(UserRequestDto userRequestDto, Long id);

    void deletar(Long id);

    List<User> findAll();

    User findById(Long id);

    UserResponseDto ativar(Long id);

    UserResponseDto desativar(Long id);

}
