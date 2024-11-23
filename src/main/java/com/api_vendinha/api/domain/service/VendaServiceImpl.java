package com.api_vendinha.api.domain.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.api_vendinha.api.Infrastructure.repository.ProdutoRepository;
import com.api_vendinha.api.Infrastructure.repository.UserRepository;
import com.api_vendinha.api.Infrastructure.repository.VendaRepository;
import com.api_vendinha.api.domain.dtos.request.VendaRequestDto;
import com.api_vendinha.api.domain.dtos.response.VendaResponseDto;
import com.api_vendinha.api.domain.entities.Produto;
import com.api_vendinha.api.domain.entities.User;
import com.api_vendinha.api.domain.entities.Venda;
import com.api_vendinha.api.domain.exception.BusinessException;
import com.api_vendinha.api.domain.exception.EntityNotFoundException;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class VendaServiceImpl implements VendaService {
    private final VendaRepository vendaRepository;
    private final ProdutoRepository produtoRepository;
    private final UserRepository userRepository;

    public VendaServiceImpl(VendaRepository vendaRepository, 
                             ProdutoRepository produtoRepository,
                             UserRepository userRepository) {
        this.vendaRepository = vendaRepository;
        this.produtoRepository = produtoRepository;
        this.userRepository = userRepository;
    }

    @Override
    public VendaResponseDto save(VendaRequestDto vendaRequestDto) {
        // Recupera o produto e usuário associados à venda
        Produto produto = produtoRepository.findById(vendaRequestDto.getProduto_id())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        
        User user = userRepository.findById(vendaRequestDto.getUser_id())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Verifica se há quantidade suficiente
        if (produto.getQuantidade() < vendaRequestDto.getQuantidade()) {
            throw new RuntimeException("Quantidade insuficiente em estoque");
        }

        // Cria uma nova instância de Venda
        Venda venda = new Venda();
        venda.setQuantidade(vendaRequestDto.getQuantidade());
        venda.setUser(user);
        venda.setProduto(produto);
        
        // Calcula o preço total
        BigDecimal precoTotal = BigDecimal.valueOf(produto.getPreco())
            .multiply(BigDecimal.valueOf(vendaRequestDto.getQuantidade()));
        venda.setPrice(precoTotal);

        // Salva a venda
        Venda saveVenda = vendaRepository.save(venda);

        // Atualiza o estoque do produto
        produto.setQuantidade(produto.getQuantidade() - venda.getQuantidade());
        produtoRepository.save(produto);

        // Converte para ResponseDto
        return convertToResponseDto(saveVenda);
    }

    @Override
    public VendaResponseDto update(VendaRequestDto vendaRequestDto, Long id) {
        // Busca a venda existente
        Venda existingVenda = vendaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Venda não encontrada"));

        // Recupera o novo produto (se alterado)
        Produto novoProduto = produtoRepository.findById(vendaRequestDto.getProduto_id())
            .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        
        // Recupera o novo usuário (se alterado)
        User novoUser = userRepository.findById(vendaRequestDto.getUser_id())
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Ajusta o estoque se a quantidade mudar
        if (!existingVenda.getQuantidade().equals(vendaRequestDto.getQuantidade())) {
            // Primeiro, devolve a quantidade original ao estoque
            Produto produtoOriginal = existingVenda.getProduto();
            produtoOriginal.setQuantidade(produtoOriginal.getQuantidade() + existingVenda.getQuantidade());

            // Verifica se há quantidade suficiente do novo produto
            if (novoProduto.getQuantidade() < vendaRequestDto.getQuantidade()) {
                throw new RuntimeException("Quantidade insuficiente em estoque");
            }

            // Atualiza o estoque do novo produto
            novoProduto.setQuantidade(novoProduto.getQuantidade() - vendaRequestDto.getQuantidade());
            produtoRepository.save(novoProduto);
        }

        // Atualiza os dados da venda
        existingVenda.setQuantidade(vendaRequestDto.getQuantidade());
        existingVenda.setUser(novoUser);
        existingVenda.setProduto(novoProduto);
        
        // Recalcula o preço total
        BigDecimal precoTotal = BigDecimal.valueOf(novoProduto.getPreco())
            .multiply(BigDecimal.valueOf(vendaRequestDto.getQuantidade()));
        existingVenda.setPrice(precoTotal);

        // Salva as alterações
        Venda updatedVenda = vendaRepository.save(existingVenda);

        return convertToResponseDto(updatedVenda);
    }

    @Override
    public List<Venda> findAll() {
        return vendaRepository.findAll();
    }

    @Override
    public Venda findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("O id fornecido não pode ser nulo");
        }
        return vendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venda não encontrada para o ID: " + id));
    }


    @Override
    @Transactional
    public void deletar(Long id) {
        try {
            User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

            // Log do início da operação
            log.info("Iniciando processo de deleção do usuário ID: {}", id);

            // Deleta vendas
            List<Venda> vendas = vendaRepository.findByUser(user);
            log.info("Deletando {} vendas associadas ao usuário ID: {}", vendas.size(), id);
            vendaRepository.deleteAll(vendas);

            // Desassocia produtos
            List<Produto> produtos = produtoRepository.findByUser(user);
            log.info("Desassociando {} produtos do usuário ID: {}", produtos.size(), id);
            for (Produto produto : produtos) {
                produto.setUser(null);
                produtoRepository.save(produto);
            }

            // Deleta usuário
            log.info("Deletando usuário ID: {}", id);
            userRepository.delete(user);
            
            log.info("Processo de deleção do usuário ID: {} concluído com sucesso", id);
            
        } catch (EntityNotFoundException e) {
            log.error("Usuário não encontrado ID: {}", id);
            throw e;
        } catch (Exception e) {
            log.error("Erro ao deletar usuário ID: {}. Erro: {}", id, e.getMessage());
            throw new BusinessException("Erro ao tentar excluir usuário: " + e.getMessage());
        }
    }

    // Método auxiliar para conversão
    private VendaResponseDto convertToResponseDto(Venda venda) {
        VendaResponseDto vendaRes = new VendaResponseDto();
        vendaRes.setId(venda.getId());
        vendaRes.setQuantidade(venda.getQuantidade());
        vendaRes.setPrice(venda.getPrice());
        vendaRes.setUser(venda.getUser());
        vendaRes.setProduto(venda.getProduto());
        return vendaRes;
    }
}
