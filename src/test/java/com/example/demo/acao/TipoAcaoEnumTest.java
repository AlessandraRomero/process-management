package com.example.demo.acao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import enums.TipoAcao;

 class TipoAcaoEnumTest {
	@Test
     void deveriaRetornarOsNomesCorretos() {
        assertEquals("PETICAO", TipoAcao.PETICAO.name());
        assertEquals("AUDIENCIA", TipoAcao.AUDIENCIA.name());
        assertEquals("SENTENCA", TipoAcao.SENTENCA.name());
    }

    @Test
     void deveriaRetornarOsValoresCorretos() {
        assertEquals(0, TipoAcao.PETICAO.ordinal());
        assertEquals(1, TipoAcao.AUDIENCIA.ordinal());
        assertEquals(2, TipoAcao.SENTENCA.ordinal());
    }

    @Test
     void deveriaSerPossivelUsarComoString() {
        TipoAcao tipoAcao = TipoAcao.AUDIENCIA;
        String tipoAcaoString = tipoAcao.toString();
        assertEquals("AUDIENCIA", tipoAcaoString);
    }
}
