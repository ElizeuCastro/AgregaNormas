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

public enum Municipio {

	MANAUS("Manaus", EstadoEnum.AMAZONAS);

	private String nome;
	private EstadoEnum estado;

	private Municipio(String nome, EstadoEnum estado) {
		this.nome = nome;
		this.estado = estado;
	}

	public String getNome() {
		return nome;
	}

	public EstadoEnum getEstado() {
		return estado;
	}

}
