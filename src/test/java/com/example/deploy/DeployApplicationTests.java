package com.example.deploy;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.deploy.model.Produto;
import com.example.deploy.service.ProdutoService;

@SpringBootTest
@ActiveProfiles("test") // Use o perfil de teste
class DeployApplicationTests {

    @Autowired
    private ProdutoService produtoService;

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
        assertThat(produtoService.listarTodos()).isNotEmpty();
    }
}
