package br.com.bibliotecaedt.enumerado;

public enum Estado {

	AMAZONAS("Amazonas", "AM");

	private String nome;
	private String uf;

	private Estado(String nome, String uf) {
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
