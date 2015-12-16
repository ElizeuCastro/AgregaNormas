package br.com.bibliotecaedt.modelo;

public class Usuario {

	private Integer identificador;
	private String nome;
	private String email;
	private String login;
	private String senha;

	public Usuario(final String nome, final String email, final String login,
			final String senha) {
		super();
		this.login = login;
		this.nome = nome;
		this.email = email;
		this.senha = senha;
	}

	public Usuario(final String login, final String senha) {
		super();
		this.login = login;
		this.senha = senha;
	}

	public Integer getIdentificador() {
		return identificador;
	}

	public void setIdentificador(Integer identificador) {
		this.identificador = identificador;
	}

	public String getLogin() {
		return login;
	}

	public String getNome() {
		return nome;
	}

	public String getEmail() {
		return email;
	}

	public String getSenha() {
		return senha;
	}

	public boolean estaCadastrado() {
		return this.identificador != null && this.identificador > 0;
	}

}
