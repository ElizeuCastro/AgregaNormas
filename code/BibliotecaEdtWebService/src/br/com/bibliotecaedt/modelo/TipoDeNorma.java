package br.com.bibliotecaedt.modelo;

public class TipoDeNorma {

	private Integer identificador;
	private String descricao;

	public TipoDeNorma(final String descricao) {
		super();
		this.descricao = descricao;
	}

	public TipoDeNorma(final Integer identificador, final String descricao) {
		super();
		this.identificador = identificador;
		this.descricao = descricao;
	}

	public Integer getIdentificador() {
		return identificador;
	}

	public String getDescricao() {
		return descricao;
	}
}
