package br.com.bibliotecaedt.enumerado;

public enum TipoDeEsfera {

	FEDERAL(1, "Esfera Federal"), 
	ESTADUAL(2, "Esfera Estadual"), 
	MUNICIPAL(3, "Esfera Municipal");

	private int id;
	private String nome;

	private TipoDeEsfera(int id, String nome) {
		this.id = id;
		this.nome = nome;
	}

	public int getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

}
