package br.com.bibliotecaedt.modelo;

public class Municipio {

	private Integer identifiacador;
	private String descricao;
	private Estado estado;

	public Integer getIdentifiacador() {
		return identifiacador;
	}

	public void setIdentifiacador(Integer identifiacador) {
		this.identifiacador = identifiacador;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

}
