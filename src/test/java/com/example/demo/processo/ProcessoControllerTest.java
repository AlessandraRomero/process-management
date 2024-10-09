package com.example.demo.processo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import controllers.ProcessoController;
import entities.Acao;
import entities.Parte;
import entities.Processo;
import enums.StatusProcesso;
import enums.TipoParte;
import services.ParteService;
import services.ProcessoService;

@AutoConfigureMockMvc
 class ProcessoControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@InjectMocks
	private ProcessoController processoController;

	@Mock
	private ProcessoService processoService;

	@MockBean
	private ParteService parteService;

	@BeforeEach
	 void setup() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(processoController).build();
		MockitoAnnotations.openMocks(this);

	}

	@Test
	void deveriaCriarProcessoComPartesEAcoes() throws Exception {
	    Processo novoProcesso = new Processo();
	    novoProcesso.setId(1L);
	    novoProcesso.setStatus(StatusProcesso.ATIVO);
	    
	    Parte parte1 = new Parte();
	    parte1.setNomeCompleto("Parte 1");
	    parte1.setCpfCnpj("111.222.333-44");
	    parte1.setTipo(TipoParte.AUTOR);
	    parte1.setEmail("parte1@gmail.com");
	    parte1.setTelefone("11111111");
	    parte1.setProcesso(novoProcesso); 

	    novoProcesso.setPartes(Arrays.asList(parte1));

	 
	    Acao acao1 = new Acao();
	    acao1.setDescricao("Ação 1");
	    acao1.setProcesso(novoProcesso); 
	    
	    novoProcesso.setAcoes(Arrays.asList(acao1)); 
	    when(processoService.criarProcesso(any(Processo.class))).thenReturn(novoProcesso);

	    mockMvc.perform(post("/api/processos")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content("{\"partes\":[{\"nomeCompleto\":\"Parte 1\", \"cpfCnpj\":\"111.222.333-44\", \"tipo\":\"AUTOR\", \"email\":\"parte1@gmail.com\", \"telefone\":\"11111111\"}], \"acoes\":[{\"descricao\":\"Ação 1\"}]}"))
	            .andExpect(status().isCreated())
	            .andExpect(jsonPath("$.id").value(1L))
	            .andExpect(jsonPath("$.status").value(StatusProcesso.ATIVO.toString()))
	            .andExpect(jsonPath("$.partes[0].nomeCompleto").value("Parte 1"))
	            .andExpect(jsonPath("$.acoes[0].descricao").value("Ação 1"));
	}
	
	@Test
	void deveriaListarProcessos() throws Exception {
	    Processo processo1 = new Processo();
	    processo1.setId(1L);
	    processo1.setNumeroProcesso("1234567890");
	    processo1.setDescricao("Processo 1");

	    Processo processo2 = new Processo();
	    processo2.setId(2L);
	    processo2.setNumeroProcesso("0987654321");
	    processo2.setDescricao("Processo 2");

	    List<Processo> processos = Arrays.asList(processo1, processo2);
	    
	    when(processoService.listarProcessos()).thenReturn(processos);

	    mockMvc.perform(get("/api/processos"))
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$").isArray())
	            .andExpect(jsonPath("$.length()").value(2))
	            .andExpect(jsonPath("$[0].id").value(1L))
	            .andExpect(jsonPath("$[0].numeroProcesso").value("1234567890"))
	            .andExpect(jsonPath("$[0].descricao").value("Processo 1"))
	            .andExpect(jsonPath("$[1].id").value(2L))
	            .andExpect(jsonPath("$[1].numeroProcesso").value("0987654321"))
	            .andExpect(jsonPath("$[1].descricao").value("Processo 2"));
	}

	@Test
	 void deveriaBuscarPorStatus() throws Exception {
		List<Processo> processos = Collections.singletonList(new Processo());
		when(processoService.buscarPorStatus(StatusProcesso.ATIVO)).thenReturn(processos);

		mockMvc.perform(get("/api/processos/status/ATIVO")).andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray()).andExpect(jsonPath("$.length()").value(1));
	}

	@Test
	 void deveriaBuscarPorDataAbertura() throws Exception {
		List<Processo> processos = Collections.singletonList(new Processo());
		LocalDate data = LocalDate.now();
		when(processoService.buscarPorDataAbertura(data)).thenReturn(processos);

		mockMvc.perform(get("/api/processos/data-abertura/{dataAbertura}", data)).andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray()).andExpect(jsonPath("$.length()").value(1));
	}

	@Test
	 void deveriaBuscarPorCpfCnpj() throws Exception {
		List<Processo> processos = Collections.singletonList(new Processo());
		String cpfCnpj = "12345678900";
		when(processoService.buscarPorCpfCnpj(cpfCnpj)).thenReturn(processos);

		mockMvc.perform(get("/api/processos/cpf-cnpj/{cpfCnpj}", cpfCnpj)).andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray()).andExpect(jsonPath("$.length()").value(1));
	}

	@Test
	 void deveriaObterProcesso() throws Exception {
		Processo processo = new Processo();
		processo.setId(1L);
		processo.setNumeroProcesso("1234567890");
		processo.setDescricao("Processo de Teste");

		when(processoService.obterProcesso(1L)).thenReturn(processo);

		mockMvc.perform(get("/api/processos/1")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1L))
				.andExpect(jsonPath("$.numeroProcesso").value("1234567890"))
				.andExpect(jsonPath("$.descricao").value("Processo de Teste"));
	}

	@Test
	 void deveriaEditarProcesso() throws Exception {
		Processo processoEditado = new Processo();
		processoEditado.setId(1L);
		processoEditado.setNumeroProcesso("1234567890");
		processoEditado.setDescricao("Processo editado");

		when(processoService.editarProcesso(eq(1L), any(Processo.class))).thenReturn(processoEditado);

		String processoEditadoJson = "{ \"numeroProcesso\": \"1234567890\", \"descricao\": \"Processo editado\" }";

		mockMvc.perform(put("/api/processos/1").contentType(MediaType.APPLICATION_JSON).content(processoEditadoJson))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.numeroProcesso").value("1234567890"))
				.andExpect(jsonPath("$.descricao").value("Processo editado"));
	}

	@Test
	 void deveriaArquivarProcesso() throws Exception {
		doNothing().when(processoService).arquivarProcesso(1L);

		mockMvc.perform(put("/api/processos/1/arquivar")).andExpect(status().isNoContent());
	}

	@Test
	 void deveriaDeletarProcesso() throws Exception {
		doNothing().when(processoService).deletarProcesso(1L);

		mockMvc.perform(delete("/api/processos/1")).andExpect(status().isNoContent());
	}
}