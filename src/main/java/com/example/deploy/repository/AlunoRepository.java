package com.example.deploy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.deploy.model.Aluno;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {
}
