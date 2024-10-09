package com.example.demo.processo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import entities.Acao;
import entities.Parte;
import entities.Processo;
import enums.StatusProcesso;
import enums.TipoAcao;
import exception.ParteNotFoundException;
import exception.ProcessoNotFoundException;
import repositories.ProcessoRepository;
import services.ProcessoService;

class ProcessoServiceTest {

	@InjectMocks
	private ProcessoService processoService;

	@Mock
	private ProcessoRepository processoRepository;

	private Processo processo;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		processo = new Processo();
		processo.setId(1L);
		processo.setNumeroProcesso("999");
		processo.setDataAbertura(LocalDate.of(2024, 10, 9));
		processo.setDescricao("Processo teste");
		processo.setStatus(StatusProcesso.ATIVO);
		processo.setPartes(new ArrayList<>());
		processo.setAcoes(new ArrayList<>());

		Acao acaoExistente = new Acao();
		acaoExistente.setId(1L);
		acaoExistente.setTipo(TipoAcao.AUDIENCIA);
		acaoExistente.setDataRegistro(LocalDateTime.now());
		acaoExistente.setDescricao("Descrição original");

		processo.getAcoes().add(acaoExistente); // Adiciona a ação ao processo
	}

	@Test
	void deveriaCriarProcesso() {
		when(processoRepository.save(any(Processo.class))).thenReturn(processo);

		Processo resultado = processoService.criarProcesso(processo);

		assertNotNull(resultado);
		assertEquals("999", resultado.getNumeroProcesso());
	}

	@Test
	void deveriaObterProcessoPorId() {
		when(processoRepository.findById(1L)).thenReturn(Optional.of(processo));

		Processo resultado = processoService.obterProcesso(1L);

		assertNotNull(resultado);
		assertEquals(1L, resultado.getId());
	}

	@Test
	void deveriaEditarProcesso() {
		when(processoRepository.findById(1L)).thenReturn(Optional.of(processo));
		when(processoRepository.save(any(Processo.class))).thenAnswer(invocation -> invocation.getArgument(0));

		processo.setDescricao("Descrição atualizada");
		Processo atualizado = processoService.editarProcesso(1L, processo);

		assertNotNull(atualizado);
		assertEquals("Descrição atualizada", atualizado.getDescricao());
	}

	@Test
	void deveriaListarProcessos() {
		List<Processo> processos = Arrays.asList(processo);
		when(processoRepository.findAll()).thenReturn(processos);

		List<Processo> resultado = processoService.listarProcessos();

		assertThat(resultado).isNotEmpty();
		assertThat(resultado.size()).isEqualTo(1);
	}

	@Test
	void deveriaAtualizarPartesExistentes() {
		Processo processo = new Processo();
		Parte parteExistente = new Parte();
		parteExistente.setId(1L);
		parteExistente.setNomeCompleto("Parte 1");
		parteExistente.setCpfCnpj("12345678900");

		processo.setPartes(new ArrayList<>());
		processo.getPartes().add(parteExistente);

		List<Parte> partesAtualizadas = new ArrayList<>();
		Parte parteAtualizada = new Parte();
		parteAtualizada.setId(1L);
		parteAtualizada.setNomeCompleto("Parte 1 Atualizada");
		parteAtualizada.setCpfCnpj("09876543211");

		partesAtualizadas.add(parteAtualizada);

		processoService.atualizarPartes(processo, partesAtualizadas);

		assertEquals("Parte 1 Atualizada", parteExistente.getNomeCompleto());
		assertEquals("09876543211", parteExistente.getCpfCnpj());
	}

	@Test
	void deveriaLancarExcecaoSeParteNaoEncontrada() {
		Processo processo = new Processo();
		Parte parteExistente = new Parte();
		parteExistente.setId(1L);
		processo.setPartes(new ArrayList<>());
		processo.getPartes().add(parteExistente);

		List<Parte> partesAtualizadas = new ArrayList<>();
		Parte parteInexistente = new Parte();
		parteInexistente.setId(2L); // ID que não existe no processo

		partesAtualizadas.add(parteInexistente);

		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			processoService.atualizarPartes(processo, partesAtualizadas);
		});

		assertEquals("Parte não encontrada: 2", exception.getMessage());
	}

	@Test
	void deveriaAdicionarNovaParte() {
		Processo processo = new Processo();
		processo.setPartes(new ArrayList<>());

		List<Parte> partesAtualizadas = new ArrayList<>();
		Parte novaParte = new Parte();
		novaParte.setNomeCompleto("Nova Parte");
		novaParte.setCpfCnpj("11223344556");

		partesAtualizadas.add(novaParte);

		processoService.atualizarPartes(processo, partesAtualizadas);

		assertEquals(1, processo.getPartes().size());
		assertEquals("Nova Parte", processo.getPartes().get(0).getNomeCompleto());
	}

	void deveriaAtualizarAcoesExistentes() {
		List<Acao> acoesAtualizadas = new ArrayList<>();

		Acao acaoAtualizada1 = new Acao();
		acaoAtualizada1.setId(1L);
		acaoAtualizada1.setTipo(TipoAcao.AUDIENCIA);
		acaoAtualizada1.setDataRegistro(LocalDateTime.now());
		acaoAtualizada1.setDescricao("Nova Descrição 1");

		acoesAtualizadas.add(acaoAtualizada1);

		processoService.atualizarAcoes(processo, acoesAtualizadas);

		assertEquals(TipoAcao.AUDIENCIA, processo.getAcoes().get(0).getTipo());
		assertEquals("Nova Descrição 1", processo.getAcoes().get(0).getDescricao());
	}

	@Test
	void deveriaLancarExcecaoSeAcaoNaoEncontrada() {
		List<Acao> acoesAtualizadas = new ArrayList<>();
		Acao acaoInexistente = new Acao();
		acaoInexistente.setId(2L);
		acoesAtualizadas.add(acaoInexistente);

		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			processoService.atualizarAcoes(processo, acoesAtualizadas);
		});

		assertEquals("Ação não encontrada: 2", exception.getMessage());
	}

	@Test
	void deveriaAdicionarNovaAcao() {
		List<Acao> acoesAtualizadas = new ArrayList<>();
		Acao novaAcao = new Acao();
		novaAcao.setTipo(TipoAcao.AUDIENCIA);
		novaAcao.setDataRegistro(LocalDateTime.now());
		novaAcao.setDescricao("Descrição da Nova Ação");

		acoesAtualizadas.add(novaAcao);

		processoService.atualizarAcoes(processo, acoesAtualizadas);

		assertEquals(2, processo.getAcoes().size());
		assertEquals("Descrição da Nova Ação", processo.getAcoes().get(1).getDescricao());
	}

	@Test
	void deveriaBuscarPorStatus() {
		List<Processo> processos = Arrays.asList(processo);
		when(processoRepository.findByStatus(StatusProcesso.ATIVO)).thenReturn(processos);

		List<Processo> resultado = processoService.buscarPorStatus(StatusProcesso.ATIVO);

		assertThat(resultado).isNotEmpty();
		assertThat(resultado.get(0).getStatus()).isEqualTo(StatusProcesso.ATIVO);
	}

	@Test
	void deveriaBuscarPorDataAbertura() {
		List<Processo> processos = Arrays.asList(processo);
		when(processoRepository.findByDataAbertura(LocalDate.of(2024, 10, 9))).thenReturn(processos);

		List<Processo> resultado = processoService.buscarPorDataAbertura(LocalDate.of(2024, 10, 9));

		assertThat(resultado).isNotEmpty();
		assertThat(resultado.get(0).getDataAbertura()).isEqualTo(LocalDate.of(2024, 10, 9));
	}

	@Test
	void deveriaBuscarPorCpfCnpj() {
		List<Processo> processos = Arrays.asList(processo);
		when(processoRepository.findByCpfCnpj("123.000.666-99")).thenReturn(processos);

		List<Processo> resultado = processoService.buscarPorCpfCnpj("123.000.666-99");

		assertThat(resultado).isNotEmpty();
		assertThat(resultado.get(0).getNumeroProcesso()).isEqualTo("999");
	}

	@Test
	void deveriaArquivarProcesso() {
		when(processoRepository.findById(1L)).thenReturn(Optional.of(processo));

		processoService.arquivarProcesso(1L);

		assertEquals(StatusProcesso.ARQUIVADO, processo.getStatus());
		verify(processoRepository).save(processo);
	}

	@Test
	void deveriaDeletarProcessoSeExistente() {
	    Long idProcesso = 1L;

	    Processo processo = new Processo();
	    processo.setId(idProcesso);
	    
	    when(processoRepository.findById(idProcesso)).thenReturn(Optional.of(processo));

	    processoService.deletarProcesso(idProcesso);

	    verify(processoRepository, times(1)).delete(processo);
	}


	@Test
	void deveriaLancarExcecaoAoObterProcessoInexistente() {
		Long idInexistente = 1L;

		when(processoRepository.findById(idInexistente)).thenReturn(Optional.empty());

		ProcessoNotFoundException exception = assertThrows(ProcessoNotFoundException.class, () -> {
			processoService.obterProcesso(idInexistente);
		});

		assertEquals("Processo não encontrado: " + idInexistente, exception.getMessage());
	}

	@Test
	void deveriaLancarExcecaoAoAtualizarParteInexistente() {
		Long idProcesso = 1L;
		Long idParteInexistente = 2L;

		Processo processo = new Processo();
		processo.setId(idProcesso);
		processo.setPartes(new ArrayList<>()); // Sem partes inicialmente

		when(processoRepository.findById(idProcesso)).thenReturn(Optional.of(processo));

		Parte parteAtualizada = new Parte();
		parteAtualizada.setId(idParteInexistente);

		ParteNotFoundException exception = assertThrows(ParteNotFoundException.class, () -> {
			processoService.atualizarPartes(processo, List.of(parteAtualizada));
		});

		assertEquals("Parte não encontrada: " + idParteInexistente, exception.getMessage());
	}

	@Test
	void deveriaLancarExcecaoAoDeletarProcessoInexistente() {
		Long idInexistente = 1L;

		when(processoRepository.findById(idInexistente)).thenReturn(Optional.empty());

		ProcessoNotFoundException exception = assertThrows(ProcessoNotFoundException.class, () -> {
			processoService.deletarProcesso(idInexistente);
		});

		assertEquals("Processo não encontrado: " + idInexistente, exception.getMessage());
	}

}