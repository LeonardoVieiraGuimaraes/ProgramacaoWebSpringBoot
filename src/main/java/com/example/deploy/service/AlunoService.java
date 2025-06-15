package com.example.deploy.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.deploy.model.Aluno;
import com.example.deploy.repository.AlunoRepository;

@Service
public class AlunoService {

    private final AlunoRepository repository;

    public AlunoService(AlunoRepository repository) {
        this.repository = repository;
    }

    public Aluno salvar(Aluno aluno) {
        return repository.save(aluno);
    }

    public List<Aluno> listarTodos() {
        return repository.findAll();
    }

    public Aluno buscarPorId(Long id) {
        Optional<Aluno> aluno = repository.findById(id);
        return aluno.orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
    }

    public void excluir(Long id) {
        repository.deleteById(id);
    }
}
