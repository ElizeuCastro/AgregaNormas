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
package br.com.bibliotecaedt.enumerado;

public enum EstadoEnum {

	AMAZONAS("Amazonas", "AM"),
	ACRE("Acre", "AC");

	private String nome;
	private String uf;

	private EstadoEnum(String nome, String uf) {
		this.nome = nome;
		this.uf = uf;
	}

	public String getNome() {
		return nome;
	}

	public String getUf() {
		return uf;
	}
}
