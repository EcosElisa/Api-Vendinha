package com.api_vendinha.api.domain.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.api_vendinha.api.Infrastructure.repository.ProdutoRepository;
import com.api_vendinha.api.Infrastructure.repository.UserRepository;
import com.api_vendinha.api.domain.dtos.request.ProdutoRequestDto;
import com.api_vendinha.api.domain.dtos.response.ProdutoResponseDto;
import com.api_vendinha.api.domain.entities.Produto;
import com.api_vendinha.api.domain.entities.User;

@Service

public class ProdutoServiceImpl implements ProdutoService {
    private final ProdutoRepository produtoRepository;
    private final UserRepository userRepository;

    public ProdutoServiceImpl(ProdutoRepository produtoRepository, UserRepository userRepository){
        this.produtoRepository = produtoRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ProdutoResponseDto save(ProdutoRequestDto produtoRequestDto) {
        Optional<Produto> produtoExite = produtoRepository.findByName(produtoRequestDto.getName());

        if(produtoExite.isPresent()){
            throw new IllegalArgumentException("Já existe um cadastro deste produto");
        }

        User user = userRepository.findById(produtoRequestDto.getUser_id())
            .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        Produto prod = new Produto();
        prod.setName(produtoRequestDto.getName());
        prod.setQuantidade(produtoRequestDto.getQuantidade());
        prod.setPreco(produtoRequestDto.getPreco());
        prod.setUser(user); // Associate user with product

        Produto saveproduct = produtoRepository.save(prod);

        ProdutoResponseDto prodRes = new ProdutoResponseDto();
        prodRes.setName(saveproduct.getName());
        prodRes.setQuantidade(saveproduct.getQuantidade());
        prodRes.setPreco(saveproduct.getPreco());
        prodRes.setActive(saveproduct.getActive());

        return prodRes;
    }

    @Override
    public ProdutoResponseDto update(ProdutoRequestDto produtoRequestDto, Long id) {
        Produto prodExist = produtoRepository.findById(id).orElseThrow();

        User user = userRepository.findById(produtoRequestDto.getUser_id())
            .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        prodExist.setName(produtoRequestDto.getName());
        prodExist.setQuantidade(produtoRequestDto.getQuantidade());
        prodExist.setPreco(produtoRequestDto.getPreco());
        prodExist.setUser(user); // Update user association

        Produto saveproduct = produtoRepository.save(prodExist);

        ProdutoResponseDto prodRes = new ProdutoResponseDto();
        prodRes.setName(saveproduct.getName());
        prodRes.setQuantidade(saveproduct.getQuantidade());
        prodRes.setPreco(saveproduct.getPreco());
        prodRes.setActive(saveproduct.getActive());

        return prodRes;
    }

    @Override
    public List<Produto> findAll() {
        return produtoRepository.findAll();
    }

    @Override
    public Produto findById(Long id) {
        return produtoRepository.findById(id).orElseThrow();
    }

    @Override
    public void deletar(Long id) {
        Produto prodExist = produtoRepository.findById(id).orElseThrow();
        produtoRepository.delete(prodExist);
    }
}
