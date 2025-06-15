package com.example.deploy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.deploy.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
