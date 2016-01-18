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
package br.com.bibliotecaedt.recurso.resposta;

import java.util.List;

import br.com.bibliotecaedt.modelo.Norma;

/**
 * @author elizeu
 *
 */
public class RespostaNorma {

    private int quantidade;
    private List<Norma> normas;

    public RespostaNorma(int quantidade, List<Norma> normas) {
	super();
	this.quantidade = quantidade;
	this.normas = normas;
    }

    public int getQuantidade() {
	return quantidade;
    }

    public List<Norma> getNormas() {
	return normas;
    }

}
