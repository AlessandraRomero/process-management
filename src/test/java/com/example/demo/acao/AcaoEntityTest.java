package com.example.demo.acao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import entities.Acao;
import enums.TipoAcao;

 class AcaoEntityTest {
	private Acao acao;

    @BeforeEach
     void setUp() {
        acao = new Acao();
        acao.setId(1L);
        acao.setTipo(TipoAcao.AUDIENCIA);
        acao.setDescricao("Descrição da Ação");
    }

    @Test
     void deveriaInicializarCorretamente() {
        assertNotNull(acao);
        assertEquals(1L, acao.getId());
        assertEquals(TipoAcao.AUDIENCIA, acao.getTipo());
        assertEquals("Descrição da Ação", acao.getDescricao());
    }

    @Test
     void deveriaDefinirDataRegistroAntesDePersistir() {
        acao.prePersist();
        assertNotNull(acao.getDataRegistro());
        assertEquals(LocalDateTime.now().getDayOfYear(), acao.getDataRegistro().getDayOfYear());
    }
}
