package com.example.demo.parte;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import entities.Parte;
import entities.Processo;
import enums.StatusProcesso;
import enums.TipoParte;
import repositories.ParteRepository;
import repositories.ProcessoRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)  
 class ParteRepositoryTest {

	@Autowired
	private ParteRepository parteRepository;

	@Autowired
	private ProcessoRepository processoRepository; 

	private Processo processo; 

	@BeforeEach
	 void setUp() {
		LocalDate dataAbertura = LocalDate.of(2023, 1, 1);
		processo = new Processo();
		processo.setDescricao("descricao");
		processo.setDataAbertura(dataAbertura);
		processo.setNumeroProcesso("12345");
		processo.setStatus(StatusProcesso.ATIVO);
		processoRepository.save(processo);
	}

	@Test
	 void testSaveAndFindParte() {
		Parte parte = new Parte();
		parte.setNomeCompleto("João da Silva");
		parte.setCpfCnpj("12345678901");
		parte.setTipo(TipoParte.AUTOR);
		parte.setEmail("joao@example.com");
		parte.setTelefone("999999999");
		parte.setProcesso(processo); 

		Parte savedParte = parteRepository.save(parte);

		Optional<Parte> foundParte = parteRepository.findById(savedParte.getId());
		assertTrue(foundParte.isPresent());
		assertEquals("João da Silva", foundParte.get().getNomeCompleto());
		assertEquals("12345678901", foundParte.get().getCpfCnpj());
		assertEquals(TipoParte.AUTOR, foundParte.get().getTipo());
		assertEquals("joao@example.com", foundParte.get().getEmail());
		assertEquals("999999999", foundParte.get().getTelefone());
		assertEquals(processo.getId(), foundParte.get().getProcesso().getId());
	}

	@Test
	 void testDeleteParte() {
		Parte parte = new Parte();
		parte.setNomeCompleto("Parte para excluir");
		parte.setCpfCnpj("98765432100");
		parte.setTipo(TipoParte.ADVOGADO); 
		parte.setEmail("excluir@example.com");
		parte.setTelefone("888888888");
		parte.setProcesso(processo); 
		Parte savedParte = parteRepository.save(parte);

		parteRepository.deleteById(savedParte.getId());

		Optional<Parte> foundParte = parteRepository.findById(savedParte.getId());
		assertFalse(foundParte.isPresent());
	}

	@Test
	 void testUpdateParte() {
		Parte parte = new Parte();
		parte.setNomeCompleto("Parte para atualizar");
		parte.setCpfCnpj("12345678900");
		parte.setTipo(TipoParte.ADVOGADO); 
		parte.setEmail("atualizar@example.com");
		parte.setTelefone("777777777");
		parte.setProcesso(processo); 
		Parte savedParte = parteRepository.save(parte);

		savedParte.setNomeCompleto("Parte atualizada");
		parteRepository.save(savedParte);

		Optional<Parte> foundParte = parteRepository.findById(savedParte.getId());
		assertTrue(foundParte.isPresent());
		assertEquals("Parte atualizada", foundParte.get().getNomeCompleto());
	}

}