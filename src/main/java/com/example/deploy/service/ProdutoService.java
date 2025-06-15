package com.example.deploy.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.deploy.exception.ResourceNotFoundException;
import com.example.deploy.model.Produto;
import com.example.deploy.repository.ProdutoRepository;

@Service
public class ProdutoService {

    private final ProdutoRepository repository;

    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    public Produto salvar(Produto produto) {
        return repository.save(produto);
    }

    public List<Produto> listarTodos() {
        return repository.findAll();
    }

    public Produto buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com ID: " + id));
    }

    public void excluir(Long id) {
        repository.deleteById(id);
    }
}
