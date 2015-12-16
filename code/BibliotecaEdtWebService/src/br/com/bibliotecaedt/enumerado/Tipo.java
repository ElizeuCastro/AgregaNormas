package br.com.bibliotecaedt.enumerado;

public enum Tipo {

	LEI(1, "Lei"), DECRETO(2, "Decreto");

	private int id;
	private String texto;

	private Tipo(int id, String texto) {
		this.id = id;
		this.texto = texto;
	}

	public int getId() {
		return id;
	}

	public String getTexto() {
		return texto;
	}

}
