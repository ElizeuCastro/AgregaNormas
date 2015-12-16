package br.com.bibliotecaedt.enumerado;

public enum Municipio {

	MANAUS("Manaus", Estado.AMAZONAS);

	private String nome;
	private Estado estado;

	private Municipio(String nome, Estado estado) {
		this.nome = nome;
		this.estado = estado;
	}

	public String getNome() {
		return nome;
	}

	public Estado getEstado() {
		return estado;
	}

}
