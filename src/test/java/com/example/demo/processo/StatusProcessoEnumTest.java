package com.example.demo.processo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import enums.StatusProcesso;

 class StatusProcessoEnumTest {
	 @Test
	     void deveriaRetornarOsNomesCorretos() {
	        assertEquals("ATIVO", StatusProcesso.ATIVO.name());
	        assertEquals("SUSPENSO", StatusProcesso.SUSPENSO.name());
	        assertEquals("ARQUIVADO", StatusProcesso.ARQUIVADO.name());
	    }

	    @Test
	     void deveriaRetornarOsValoresCorretos() {
	        assertEquals(0, StatusProcesso.ATIVO.ordinal());
	        assertEquals(1, StatusProcesso.SUSPENSO.ordinal());
	        assertEquals(2, StatusProcesso.ARQUIVADO.ordinal());
	    }

	    @Test
	     void deveriaSerPossivelUsarComoString() {
	        StatusProcesso status = StatusProcesso.ATIVO;
	        String statusString = status.toString();
	        assertEquals("ATIVO", statusString);
	    }
}
