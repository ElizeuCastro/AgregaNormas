package br.com.agreganormas.poc.getdatafromhtml;
public class StateLaw {

	private String ano;
	private String numero;
	private String data;
	private String ementa;
	private String linkLei;
	private String linkLeiDetalhe;

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

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getEmenta() {
		return ementa;
	}

	public void setEmenta(String ementa) {
		this.ementa = ementa;
	}

	public String getLinkLei() {
		return linkLei;
	}

	public void setLinkLei(String linkLei) {
		this.linkLei = linkLei;
	}

	public String getLinkLeiDetalhe() {
		return linkLeiDetalhe;
	}

	public void setLinkLeiDetalhe(String linkLeiDetalhe) {
		this.linkLeiDetalhe = linkLeiDetalhe;
	}

	@Override
	public String toString() {
		return "LeiEstadual [ano=" + ano + ", numero=" + numero + ", data="
				+ data + ", ementa=" + ementa + ", linkLei=" + linkLei
				+ ", linkLeiDetalhe=" + linkLeiDetalhe + "]";
	}

}
