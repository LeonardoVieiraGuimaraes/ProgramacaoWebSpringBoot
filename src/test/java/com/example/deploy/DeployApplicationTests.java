package com.example.deploy;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.deploy.model.Produto;
import com.example.deploy.service.ProdutoService;
import com.example.deploy.repository.ProdutoRepository;

@SpringBootTest
@ActiveProfiles("test") // Use o perfil de teste
class DeployApplicationTests {

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private ProdutoRepository produtoRepository;

    @AfterEach
    void tearDown() {
        produtoRepository.deleteAll();
    }

    @Test
    void contextLoads() {
        assertThat(produtoService).isNotNull();
    }

    @Test
    void testSalvarProduto() {
        Produto produto = new Produto(null, "Produto Teste", 10.0);
        Produto salvo = produtoService.salvar(produto);
        assertThat(salvo.getId()).isNotNull();
        assertThat(salvo.getNome()).isEqualTo("Produto Teste");
    }

    @Test
    void testListarProdutos() {
        produtoService.salvar(new Produto(null, "Produto A", 1.0));
        produtoService.salvar(new Produto(null, "Produto B", 2.0));
        assertThat(produtoService.listarTodos()).isNotEmpty();
    }
}
