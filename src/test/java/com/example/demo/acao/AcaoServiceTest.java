package com.example.demo.acao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import entities.Acao;
import entities.Processo;
import enums.TipoAcao;
import exception.AcaoNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import repositories.AcaoRepository;
import repositories.ProcessoRepository;
import services.AcaoService;
import services.ProcessoService;

 class AcaoServiceTest {

    @InjectMocks
    private AcaoService acaoService;

    @Mock
    private AcaoRepository acaoRepository;

    @Mock
    private ProcessoRepository processoRepository;

    @Mock
    private ProcessoService processoService;

    @BeforeEach
     void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
     void deveriaCriarAcao() {
        Processo processo = new Processo();
        processo.setId(1L);
        
        Acao acao = new Acao();
        acao.setId(1L);
        acao.setTipo(TipoAcao.AUDIENCIA);
        acao.setDescricao("Descrição da Ação");

        when(processoService.obterProcesso(1L)).thenReturn(processo);
        when(acaoRepository.save(any(Acao.class))).thenReturn(acao);

        Acao novaAcao = acaoService.criarAcao(1L, acao);

        assertEquals(1L, novaAcao.getId());
        assertEquals(TipoAcao.AUDIENCIA, novaAcao.getTipo());
        assertEquals("Descrição da Ação", novaAcao.getDescricao());
        verify(processoService).obterProcesso(1L);
        verify(acaoRepository).save(any(Acao.class));
    }
    
    @Test
    void deveriaLancarExcecaoQuandoProcessoNaoEncontrado() {
        Long idInexistente = 1L; 

        when(processoService.obterProcesso(idInexistente)).thenReturn(null);

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            acaoService.criarAcao(idInexistente, new Acao());
        });
        
        assertEquals("Processo não encontrado com o número: " + idInexistente, exception.getMessage());
        
        verify(processoService).obterProcesso(idInexistente);
    }

    @Test
     void deveriaListarAcoes() {
        Acao acao1 = new Acao();
        acao1.setId(1L);
        acao1.setTipo(TipoAcao.PETICAO);

        Acao acao2 = new Acao();
        acao2.setId(2L);
        acao2.setTipo(TipoAcao.AUDIENCIA);

        List<Acao> acoes = Arrays.asList(acao1, acao2);

        when(acaoRepository.findAll()).thenReturn(acoes);

        List<Acao> resultado = acaoService.listarAcoes();

        assertEquals(2, resultado.size());
        assertEquals(TipoAcao.PETICAO, resultado.get(0).getTipo());
        assertEquals(TipoAcao.AUDIENCIA, resultado.get(1).getTipo());
    }

    @Test
     void deveriaObterAcaoPorId() {
        Acao acao = new Acao();
        acao.setId(1L);
        acao.setTipo(TipoAcao.AUDIENCIA);

        when(acaoRepository.findById(1L)).thenReturn(Optional.of(acao));

        Acao resultado = acaoService.obterAcao(1L);

        assertEquals(1L, resultado.getId());
        assertEquals(TipoAcao.AUDIENCIA, resultado.getTipo());
    }

    @Test
    void deveriaLancarExcecaoAoObterAcaoInexistente() {
        Long idInexistente = 1L;
        when(acaoRepository.findById(idInexistente)).thenReturn(Optional.empty());

        AcaoNotFoundException exception = assertThrows(AcaoNotFoundException.class, () -> {
            acaoService.obterAcao(idInexistente);
        });

        String expectedMessage = "Ação não encontrada: " + idInexistente;
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
     void deveriaEditarAcao() {
        Acao acaoExistente = new Acao();
        acaoExistente.setId(1L);
        acaoExistente.setTipo(TipoAcao.AUDIENCIA);

        Acao acaoAtualizada = new Acao();
        acaoAtualizada.setTipo(TipoAcao.PETICAO);
        acaoAtualizada.setDescricao("Descrição Atualizada");

        when(acaoRepository.findById(1L)).thenReturn(Optional.of(acaoExistente));
        when(acaoRepository.save(any(Acao.class))).thenReturn(acaoExistente);

        Acao resultado = acaoService.editarAcao(1L, acaoAtualizada);

        assertEquals(TipoAcao.PETICAO, resultado.getTipo());
        assertEquals("Descrição Atualizada", resultado.getDescricao());
    }

    @Test
     void deveriaDeletarAcao() {
        Acao acao = new Acao();
        acao.setId(1L);
        
        when(acaoRepository.findById(1L)).thenReturn(Optional.of(acao));
        doNothing().when(acaoRepository).delete(any(Acao.class));

        acaoService.deletarAcao(1L);

        verify(acaoRepository).delete(acao);
    }
}
