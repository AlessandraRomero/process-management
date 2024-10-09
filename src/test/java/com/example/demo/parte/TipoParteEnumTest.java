package com.example.demo.parte;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import enums.TipoParte;

 class TipoParteEnumTest {
	@Test
     void deveriaRetornarOsNomesCorretos() {
        assertEquals("AUTOR", TipoParte.AUTOR.name());
        assertEquals("REU", TipoParte.REU.name());
        assertEquals("ADVOGADO", TipoParte.ADVOGADO.name());
    }

    @Test
     void deveriaRetornarOsValoresCorretos() {
        assertEquals(0, TipoParte.AUTOR.ordinal());
        assertEquals(1, TipoParte.REU.ordinal());
        assertEquals(2, TipoParte.ADVOGADO.ordinal());
    }

    @Test
     void deveriaSerPossivelUsarComoString() {
        TipoParte tipoParte = TipoParte.REU;
        String tipoParteString = tipoParte.toString();
        assertEquals("REU", tipoParteString);
    }
}
