package br.com.bibliotecaedt.modelo;

public class TipoDeNorma implements Validador {

	public static final String TB_CAMPO_ID = "id_tipo";
	public static final String TB_CAMPO_DESCRICAO = "descricao";
	private Integer identificador;
	private String descricao;

	public TipoDeNorma() {
		super();
	}

	public TipoDeNorma(final Integer identificador, final String descricao) {
		super();
		this.identificador = identificador;
		this.descricao = descricao;
	}

	public Integer getIdentificador() {
		return identificador;
	}

	public void setIdentificador(Integer identificador) {
		this.identificador = identificador;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public boolean estaCadastrado() {
		return this.identificador != null && this.identificador > 0;
	}

}
