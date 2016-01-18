package br.com.bibliotecaedt.modelo;

public class EsferaEstadual extends Esfera {

	// Campos da tabela esfera_estadual
	public static final String TB_CAMPO_ID_ESFERA_ESTADUAL = "id_esfera_estadual";
	public static final String TB_CAMPO_FK_ID_ESFERA = "fk_id_esfera";
	public static final String TB_CAMPO_FK_ID_ESTADO = "fk_id_estado";

//	private Integer idEsferaEstadual;
	private Estado estado;

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

//	public Integer getIdEsferaEstadual() {
//		return idEsferaEstadual;
//	}
//
//	public void setIdEsferaEstadual(Integer idEsferaEstadual) {
//		this.idEsferaEstadual = idEsferaEstadual;
//	}
//
//	@Override
//	public boolean estaCadastrado() {
//		return getIdEsfera() != null && getIdEsfera() > 0
//				&& this.idEsferaEstadual != null && this.idEsferaEstadual > 0;
//	}

}
