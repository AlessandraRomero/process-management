package com.example.demo.processo;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import entities.Processo;
import enums.StatusProcesso;
import repositories.ProcessoRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProcessoRepositoryTest {

	@Autowired
	private ProcessoRepository processoRepository;

	@BeforeEach
	void setUp() {
		processoRepository.deleteAll();
	}

	@Test
	void testFindByNumeroProcesso() {
		Processo processo = new Processo();
		processo.setDescricao("descricao");
		processo.setDataAbertura(LocalDate.now());
		processo.setNumeroProcesso("12345");
		processo.setStatus(StatusProcesso.ATIVO);
		processoRepository.save(processo);

		Processo found = processoRepository.findByNumeroProcesso("12345");

		assertThat(found).isNotNull();
		assertThat(found.getNumeroProcesso()).isEqualTo("12345");
	}

	@Test
	void testFindByStatus() {
		Processo processo = new Processo();
		processo.setDescricao("descricao");
		processo.setDataAbertura(LocalDate.now());
		processo.setNumeroProcesso("12345");
		processo.setStatus(StatusProcesso.ATIVO);
		processoRepository.save(processo);

		List<Processo> found = processoRepository.findByStatus(StatusProcesso.ATIVO);

		assertThat(found).hasSize(1);
		assertThat(found.get(0).getStatus()).isEqualTo(StatusProcesso.ATIVO);
	}

	@Test
	void testFindByDataAbertura() {
		LocalDate dataAbertura = LocalDate.of(2023, 1, 1);
		Processo processo = new Processo();
		processo.setDescricao("descricao");
		processo.setDataAbertura(dataAbertura);
		processo.setNumeroProcesso("12345");
		processo.setStatus(StatusProcesso.ATIVO);
		processoRepository.save(processo);

		List<Processo> found = processoRepository.findByDataAbertura(dataAbertura);

		assertThat(found).hasSize(1);
		assertThat(found.get(0).getDataAbertura()).isEqualTo(dataAbertura);
	}

}