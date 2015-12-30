/**
 * Copyright (C) 2015 Biblioteca Edt.
 * 
 * Projeto final curso pós-graduação em:
 * Engenharia de Software com Ênfase em Desenvolvimento Web.
 * 
 * UNINORTE - Laureate.
 * 
 * @author elizeu
 * @author danilo
 */
package br.com.teste.controle;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import br.com.bibliotecaedt.controle.ControleNorma;
import br.com.bibliotecaedt.enumerado.EsferaEnum;
import br.com.bibliotecaedt.enumerado.EstadoEnum;
import br.com.bibliotecaedt.enumerado.TipoDeNormaEnum;
import br.com.bibliotecaedt.modelo.Norma;

public class ControleNormaTeste {

	private ControleNorma controle = new ControleNorma();

	@Test
	public void salvarNormasAMTeste() {
		Norma norma = new Norma();
		norma.setAno("1");
		norma.setDataPublicacao("13.07.1985");
		norma.setDescricao("teste am");
		norma.setResumo("resumo am");
		norma.setNumero("1");
		
		List<Norma> normas = new ArrayList<>();
		normas.add(norma);
		
		controle.salvarNormas(normas,EstadoEnum.AMAZONAS, EsferaEnum.ESTADUAL, TipoDeNormaEnum.LEI);
	}
	
	@Test
	public void salvarNormasACTeste() {
		Norma norma = new Norma();
		norma.setAno("1");
		norma.setDataPublicacao("13.07.1985");
		norma.setDescricao("teste ac");
		norma.setResumo("resumo ac");
		norma.setNumero("1");
		
		List<Norma> normas = new ArrayList<>();
		normas.add(norma);
		
		controle.salvarNormas(normas, EstadoEnum.ACRE, EsferaEnum.ESTADUAL, TipoDeNormaEnum.LEI);
	}

}
