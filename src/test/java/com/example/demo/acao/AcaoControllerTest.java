package com.example.demo.acao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import controllers.AcaoController;
import entities.Acao;
import enums.TipoAcao;
import services.AcaoService;

 class AcaoControllerTest {

    @InjectMocks
    private AcaoController acaoController;

    @Mock
    private AcaoService acaoService;

    @BeforeEach
     void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
     void deveriaCriarAcao() {
        Acao acao = new Acao();
        acao.setId(1L);
        acao.setTipo(TipoAcao.AUDIENCIA);

        when(acaoService.criarAcao(eq(1L), any(Acao.class))).thenReturn(acao);

        ResponseEntity<Acao> response = acaoController.criarAcao(1L, acao);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(acao, response.getBody());
        verify(acaoService).criarAcao(eq(1L), any(Acao.class));
    }

    @Test
     void deveriaListarAcoes() {
        Acao acao1 = new Acao();
        acao1.setId(1L);
        acao1.setTipo(TipoAcao.AUDIENCIA);

        Acao acao2 = new Acao();
        acao2.setId(2L);
        acao2.setTipo(TipoAcao.PETICAO);

        List<Acao> acoes = Arrays.asList(acao1, acao2);

        when(acaoService.listarAcoes()).thenReturn(acoes);

        ResponseEntity<List<Acao>> response = acaoController.listarAcoes();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals(TipoAcao.AUDIENCIA, response.getBody().get(0).getTipo());
        assertEquals(TipoAcao.PETICAO, response.getBody().get(1).getTipo());
    }

    @Test
     void deveriaObterAcao() {
        Acao acao = new Acao();
        acao.setId(1L);
        acao.setTipo(TipoAcao.AUDIENCIA);

        when(acaoService.obterAcao(1L)).thenReturn(acao);

        ResponseEntity<Acao> response = acaoController.obterAcao(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(acao, response.getBody());
    }

    @Test
     void deveriaEditarAcao() {
        Acao acao = new Acao();
        acao.setId(1L);
        acao.setTipo(TipoAcao.AUDIENCIA);

        when(acaoService.editarAcao(eq(1L), any(Acao.class))).thenReturn(acao);

        ResponseEntity<Acao> response = acaoController.editarAcao(1L, acao);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(acao, response.getBody());
        verify(acaoService).editarAcao(eq(1L), any(Acao.class));
    }

    @Test
     void deveriaDeletarAcao() {
        doNothing().when(acaoService).deletarAcao(1L);

        ResponseEntity<Void> response = acaoController.deletarAcao(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(acaoService).deletarAcao(1L);
    }
}
