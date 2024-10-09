package com.example.demo.parte;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.http.MediaType;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import controllers.ParteController;
import entities.Parte;
import enums.TipoParte;
import services.ParteService;

@AutoConfigureMockMvc
 class ParteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ParteService parteService;

    @InjectMocks
    private ParteController parteController;

    @BeforeEach
     void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(parteController).build();
    }

    @Test
     void deveriaCriarParte() throws Exception {
        Parte novaParte = new Parte();
        novaParte.setId(1L);
        novaParte.setNomeCompleto("Parte 1");
        novaParte.setCpfCnpj("111.222.333-44");
        novaParte.setTipo(TipoParte.AUTOR);
        novaParte.setEmail("parte1@gmail.com");
        novaParte.setTelefone("11111111");

        when(parteService.criarParte(eq(1L), any(Parte.class))).thenReturn(novaParte);

        mockMvc.perform(post("/api/partes/processos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(novaParte)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nomeCompleto").value("Parte 1"))
                .andExpect(jsonPath("$.cpfCnpj").value("111.222.333-44"));
    }

    @Test
     void deveriaListarPartes() throws Exception {
        Parte parte1 = new Parte();
        parte1.setNomeCompleto("Parte 1");

        Parte parte2 = new Parte();
        parte2.setNomeCompleto("Parte 2");

        List<Parte> partes = Arrays.asList(parte1, parte2);

        when(parteService.listarPartes()).thenReturn(partes);

        mockMvc.perform(get("/api/partes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nomeCompleto").value("Parte 1"))
                .andExpect(jsonPath("$[1].nomeCompleto").value("Parte 2"));
    }

    @Test
     void deveriaObterPartePorId() throws Exception {
        Parte parte = new Parte();
        parte.setId(1L);
        parte.setNomeCompleto("Parte 1");

        when(parteService.obterParte(1L)).thenReturn(parte);

        mockMvc.perform(get("/api/partes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nomeCompleto").value("Parte 1"));
    }

    @Test
     void deveriaEditarParte() throws Exception {
        Parte parteAtualizada = new Parte();
        parteAtualizada.setId(1L);
        parteAtualizada.setNomeCompleto("Parte Atualizada");
        parteAtualizada.setCpfCnpj("111.222.333-44");

        when(parteService.editarParte(eq(1L), any(Parte.class))).thenReturn(parteAtualizada);

        mockMvc.perform(put("/api/partes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(parteAtualizada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nomeCompleto").value("Parte Atualizada"))
                .andExpect(jsonPath("$.cpfCnpj").value("111.222.333-44"));
    }

    @Test
     void deveriaDeletarParte() throws Exception {
        doNothing().when(parteService).deletarParte(1L);

        mockMvc.perform(delete("/api/partes/1"))
                .andExpect(status().isNoContent());
    }
}