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

public enum EsferaEnum {

	FEDERAL("Esfera Federal"), 
	ESTADUAL("Esfera Estadual"), 
	MUNICIPAL("Esfera Municipal");

	private String nome;

	private EsferaEnum(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

}
