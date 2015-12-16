package br.com.bibliotecaedt.controle;

import br.com.bibliotecaedt.modelo.Usuario;
import br.com.bibliotecaedt.persistencia.UsuarioDao;

public final class ControleUsuario {

	private UsuarioDao usuarioDao;

	public ControleUsuario() {
		usuarioDao = UsuarioDao.getInstancia();
	}

	public Usuario salvar(Usuario usuario) {
		return usuarioDao.cadastrar(usuario);
	}

	public Usuario autenticar(Usuario usuario) {
		return usuarioDao.autenticar(usuario);
	}

	public boolean verificarEmailExiste(String email) {
		return usuarioDao.emailExiste(email);
	}

	public boolean verificarLoginExiste(String login) {
		return usuarioDao.loginExiste(login);
	}

}
