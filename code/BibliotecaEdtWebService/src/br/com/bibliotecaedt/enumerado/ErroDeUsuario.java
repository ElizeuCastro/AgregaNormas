package br.com.bibliotecaedt.enumerado;

public enum ErroDeUsuario {

	LOGIN_DUPLICADO(1, "Já existe um usuário com este login cadastrado"), 
	EMAIL_DUPLICADO(2, "Já existe um usuário com este email cadastrado"),
	LOGIN_SENHA_INVALIDO(3, "Login ou senha inválidos");

	private int erro;
	private String mensagem;

	private ErroDeUsuario(int erro, String mensagem) {
		this.erro = erro;
		this.mensagem = mensagem;
	}

	public int getErro() {
		return erro;
	}

	public String getMensagem() {
		return mensagem;
	}

}
