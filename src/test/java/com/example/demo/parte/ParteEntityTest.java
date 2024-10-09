package com.example.demo.parte;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import entities.Parte;
import entities.Processo;
import enums.TipoParte;

public class ParteEntityTest {
	
	    private Parte parte;

	    @BeforeEach
	     void setUp() {
	        parte = new Parte();
	        parte.setId(1L);
	        parte.setNomeCompleto("Nome Completo");
	        parte.setCpfCnpj("111.222.333-44");
	        parte.setTipo(TipoParte.AUTOR);
	        parte.setEmail("parte@example.com");
	        parte.setTelefone("123456789");
	    }

	    @Test
	     void deveriaInicializarCorretamente() {
	        assertNotNull(parte);
	        assertEquals(1L, parte.getId());
	        assertEquals("Nome Completo", parte.getNomeCompleto());
	        assertEquals("111.222.333-44", parte.getCpfCnpj());
	        assertEquals(TipoParte.AUTOR, parte.getTipo());
	        assertEquals("parte@example.com", parte.getEmail());
	        assertEquals("123456789", parte.getTelefone());
	    }

	    @Test
	     void deveriaAssociarProcesso() {
	        Processo processo = new Processo();
	        processo.setId(2L);
	        parte.setProcesso(processo);

	        assertNotNull(parte.getProcesso());
	        assertEquals(2L, parte.getProcesso().getId());
	    }
	}
