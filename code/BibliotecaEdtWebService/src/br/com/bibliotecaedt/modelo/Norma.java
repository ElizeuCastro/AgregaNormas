package br.com.bibliotecaedt.modelo;

public class Norma {

	private Integer identificador;
	private TipoDeNorma tipoDeNorma;
	private Esfera esfera;
	private String ano;
	private String numero;
	private String dataPublicacao;
	private String resumo;
	private String descricao;

	public Integer getIdentificador() {
		return identificador;
	}

	public void setIdentificador(Integer identificador) {
		this.identificador = identificador;
	}

	public void setTipoDeNorma(TipoDeNorma tipoDeNorma) {
		this.tipoDeNorma = tipoDeNorma;
	}

	public TipoDeNorma getTipoDeNorma() {
		return tipoDeNorma;
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getDataPublicacao() {
		return dataPublicacao;
	}

	public void setDataPublicacao(String dataPublicacao) {
		this.dataPublicacao = dataPublicacao;
	}

	public String getResumo() {
		return resumo;
	}

	public void setResumo(String resumo) {
		this.resumo = resumo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Esfera getEsfera() {
		return esfera;
	}

	public void setEsfera(Esfera esfera) {
		this.esfera = esfera;
	}

	@Override
	public String toString() {
		return "Norma [identificador=" + identificador + ", tipoDeNorma="
				+ tipoDeNorma + ", esfera=" + esfera + ", ano=" + ano
				+ ", numero=" + numero + ", dataPublicacao=" + dataPublicacao
				+ ", resumo=" + resumo + ", descricao=" + descricao + "]";
	}

}
