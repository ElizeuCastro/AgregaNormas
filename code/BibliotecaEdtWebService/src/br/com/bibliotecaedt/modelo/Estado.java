package br.com.bibliotecaedt.modelo;

public class Estado implements Validador {

	public static final String TB_CAMPO_ID_ESTADO = "id_estado";
	public static final String TB_CAMPO_DESCRICAO = "descricao";
	public static final String TB_CAMPO_UF = "uf";

	private Integer identificador;
	private String descricao;
	private String uf;

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

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	@Override
	public boolean estaCadastrado() {
		return this.identificador != null && this.identificador > 0;
	}

}
