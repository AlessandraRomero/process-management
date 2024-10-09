package com.example.demo.acao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import entities.Acao;
import enums.TipoAcao;
import jakarta.persistence.EntityNotFoundException;
import repositories.AcaoRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) 
class AcaoRepositoryTest {

    @Autowired
    private AcaoRepository acaoRepository;

    @BeforeEach
    void setUp() {
        acaoRepository.deleteAll(); 
    }

    @Test
    @Rollback 
    void deveriaSalvarEEncontrarAcao() {
        Acao acao = new Acao();
        acao.setTipo(TipoAcao.AUDIENCIA);
        acao.setDescricao("Descrição do teste");

        Acao savedAcao = acaoRepository.save(acao);

   
        Acao foundAcao = acaoRepository.findById(savedAcao.getId()).orElse(null);
        assertThat(foundAcao).isNotNull();
        assertThat(foundAcao.getTipo()).isEqualTo(TipoAcao.AUDIENCIA);
        assertThat(foundAcao.getDescricao()).isEqualTo("Descrição do teste");
    }

    @Test
    @Rollback
    void deveriaDeletarAcao() {
        Acao acao = new Acao();
        acao.setTipo(TipoAcao.AUDIENCIA);
        acao.setDescricao("Descrição do teste");

        Acao savedAcao = acaoRepository.save(acao);

        acaoRepository.delete(savedAcao);

        assertThrows(EntityNotFoundException.class, () -> {
            acaoRepository.findById(savedAcao.getId()).orElseThrow(() -> new EntityNotFoundException("Ação não encontrada"));
        });
    }
}
