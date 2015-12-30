package br.com.bibliotecaedt.modelo;

public class Esfera implements Validador {

	public static final String TB_CAMPO_ID_ESFERA = "id_esfera";
	public static final String TB_CAMPO_DESCRICAO = "descricao";

	private Integer idEsfera;
	private String descricao;

	public Integer getIdEsfera() {
		return idEsfera;
	}

	public void setIdEsfera(Integer idEsfera) {
		this.idEsfera = idEsfera;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public boolean estaCadastrado() {
		return this.idEsfera != null && this.idEsfera > 0;
	}

}
