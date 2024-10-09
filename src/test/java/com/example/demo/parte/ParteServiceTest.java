package com.example.demo.parte;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
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

import entities.Parte;
import entities.Processo;
import enums.TipoParte;
import exception.ParteNotFoundException;
import repositories.ParteRepository;
import services.ParteService;
import services.ProcessoService;

 class ParteServiceTest {

    @InjectMocks
    private ParteService parteService;

    @Mock
    private ParteRepository parteRepository;

    @Mock
    private ProcessoService processoService;

    @BeforeEach
     void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
     void deveriaCriarParte() {
        Processo processo = new Processo();
        processo.setId(1L);

        Parte parte = new Parte();
        parte.setNomeCompleto("Parte 1");
        parte.setCpfCnpj("111.222.333-44");
        parte.setTipo(TipoParte.AUTOR);
        parte.setEmail("parte1@gmail.com");
        parte.setTelefone("11111111");

        when(processoService.obterProcesso(1L)).thenReturn(processo);
        when(parteRepository.save(any(Parte.class))).thenReturn(parte);

        Parte result = parteService.criarParte(1L, parte);

        assertNotNull(result);
        assertEquals("Parte 1", result.getNomeCompleto());
        verify(parteRepository, times(1)).save(parte);
    }

    @Test
     void deveriaListarPartes() {
        Parte parte1 = new Parte();
        Parte parte2 = new Parte();
        when(parteRepository.findAll()).thenReturn(Arrays.asList(parte1, parte2));

        List<Parte> partes = parteService.listarPartes();

        assertEquals(2, partes.size());
        verify(parteRepository, times(1)).findAll();
    }

    @Test
     void deveriaObterPartePorId() {
        Parte parte = new Parte();
        parte.setId(1L);
        when(parteRepository.findById(1L)).thenReturn(Optional.of(parte));

        Parte result = parteService.obterParte(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(parteRepository, times(1)).findById(1L);
    }

    @Test
     void deveriaEditarParte() {
        Parte parte = new Parte();
        parte.setId(1L);
        parte.setNomeCompleto("Parte Original");

        Parte parteAtualizada = new Parte();
        parteAtualizada.setNomeCompleto("Parte Atualizada");

        when(parteRepository.findById(1L)).thenReturn(Optional.of(parte));
        when(parteRepository.save(any(Parte.class))).thenReturn(parteAtualizada);

        Parte result = parteService.editarParte(1L, parteAtualizada);

        assertEquals("Parte Atualizada", result.getNomeCompleto());
        verify(parteRepository, times(1)).save(parte);
    }

    @Test
    void deveriaDeletarParteExistente() {
        Long idExistente = 1L;
        Parte parteExistente = new Parte(); 
        parteExistente.setId(idExistente); 

        when(parteRepository.findById(idExistente)).thenReturn(Optional.of(parteExistente));

        assertDoesNotThrow(() -> {
            parteService.deletarParte(idExistente);
        });

        verify(parteRepository, times(1)).delete(parteExistente);
    }

     
    @Test
    void deveriaLancarExcecaoAoEditarParteInexistente() {
        Long idInexistente = 1L;
        Parte parteAtualizada = new Parte(); 

        when(parteRepository.findById(idInexistente)).thenReturn(Optional.empty());

        ParteNotFoundException exception = assertThrows(ParteNotFoundException.class, () -> {
            parteService.editarParte(idInexistente, parteAtualizada);
        });

        String expectedMessage = "Parte não encontrada: " + idInexistente;
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void deveriaLancarExcecaoAoObterParteInexistente() {
        Long idInexistente = 1L;
        when(parteRepository.findById(idInexistente)).thenReturn(Optional.empty());

        ParteNotFoundException exception = assertThrows(ParteNotFoundException.class, () -> {
            parteService.obterParte(idInexistente);
        });

        String expectedMessage = "Parte não encontrada: " + idInexistente;
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    
    @Test
    void deveriaLancarExcecaoAoDeletarParteInexistente() {
        Long idInexistente = 1L;

        when(parteRepository.findById(idInexistente)).thenReturn(Optional.empty());

        ParteNotFoundException exception = assertThrows(ParteNotFoundException.class, () -> {
            parteService.deletarParte(idInexistente);
        });

        String expectedMessage = "Parte não encontrada: " + idInexistente;
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }
}