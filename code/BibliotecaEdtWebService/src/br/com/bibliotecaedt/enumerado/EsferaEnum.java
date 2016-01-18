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

    FEDERAL(1, "Esfera Federal"), 
    ESTADUAL(2, "Esfera Estadual"), 
    MUNICIPAL(3, "Esfera Municipal");

    private int tipo;
    private String nome;

    private EsferaEnum(final int tipo, final String nome) {
	this.tipo = tipo;
	this.nome = nome;
    }

    public String getNome() {
	return nome;
    }

    public int getTipo() {
	return tipo;
    }

    public static EsferaEnum getEsferaPorId(Integer id) {
	if (id != null) {
	    for (EsferaEnum esferaEnum : values()) {
		if (esferaEnum.getTipo() == id) {
		    return esferaEnum;
		}
	    }
	}
	return null;
    }
}
