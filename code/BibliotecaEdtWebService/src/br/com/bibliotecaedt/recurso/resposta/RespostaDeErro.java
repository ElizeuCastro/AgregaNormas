package br.com.bibliotecaedt.recurso.resposta;

public class RespostaDeErro {

	private Integer codigo;
	private String mensagem;

	public RespostaDeErro(Integer codigoErro, String mensagem) {
		super();
		this.codigo = codigoErro;
		this.mensagem = mensagem;
	}

	public Integer getCodigoErro() {
		return codigo;
	}

	public void setCodigoErro(Integer codigoErro) {
		this.codigo = codigoErro;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

}
