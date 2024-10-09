package com.example.demo.processo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import entities.Processo;
import enums.StatusProcesso;

 class ProcessoEntityTest {
    
    private Processo processo;

    @BeforeEach
     void setUp() {
        processo = new Processo();
        processo.setId(1L);
        processo.setNumeroProcesso("1234567890");
        processo.setDataAbertura(LocalDate.now());
        processo.setDescricao("Processo de Teste");
        processo.setStatus(StatusProcesso.ATIVO);
    }

    @Test
     void deveriaInicializarCorretamente() {
        assertNotNull(processo);
        assertEquals(1L, processo.getId());
        assertEquals("1234567890", processo.getNumeroProcesso());
        assertEquals(StatusProcesso.ATIVO, processo.getStatus());
    }

    @Test
     void deveriaEditarProcessoCorretamente() {
        String novoNumeroProcesso = "0987654321";
        LocalDate novaDataAbertura = LocalDate.of(2023, 1, 1);
        String novaDescricao = "Novo Processo de Teste";
        StatusProcesso novoStatus = StatusProcesso.ARQUIVADO;

        processo.editar(novoNumeroProcesso, novaDataAbertura, novaDescricao, novoStatus);

        assertEquals(novoNumeroProcesso, processo.getNumeroProcesso());
        assertEquals(novaDataAbertura, processo.getDataAbertura());
        assertEquals(novaDescricao, processo.getDescricao());
        assertEquals(novoStatus, processo.getStatus());
    }

    @Test
     void deveriaArquivarProcessoCorretamente() {
        processo.arquivar();

        assertEquals(StatusProcesso.ARQUIVADO, processo.getStatus());
    }
}
