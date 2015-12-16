package br.com.bibliotecaedt.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import br.com.bibliotecaedt.modelo.Usuario;

public final class UsuarioDao {

	private static UsuarioDao instancia;

	public static UsuarioDao getInstancia() {
		if (instancia == null) {
			instancia = new UsuarioDao();
		}
		return instancia;
	}

	public Usuario cadastrar(Usuario usuario) {
		int generetedId = -1;
		try {
			final Connection connection = Conexao.getConexao();
			String sql = "insert into usuario (nome, email, login, senha) values (?,?,?,?)";
			PreparedStatement statement = connection.prepareStatement(sql,
					Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, usuario.getNome());
			statement.setString(2, usuario.getEmail());
			statement.setString(3, usuario.getLogin());
			statement.setString(4, usuario.getSenha());
			statement.executeUpdate();
			ResultSet resultSet = statement.getGeneratedKeys();
			while (resultSet.next()) {
				generetedId = resultSet.getInt(1);
			}
		} catch (final SQLException exception) {
			System.out.println(exception.getMessage());
		} finally {
			usuario.setIdentificador(generetedId);
			Conexao.fechaConexao();
		}
		return usuario;
	}

	public Usuario autenticar(Usuario usuario) {
		try {
			final String sql = "select * from usuario where login = ? and senha = ?";
			PreparedStatement statement = Conexao.getConexao()
					.prepareStatement(sql);
			statement.setString(1, usuario.getLogin());
			statement.setString(2, usuario.getSenha());
			final ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				usuario.setIdentificador(resultSet.getInt(1));
			}
		} catch (final SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			Conexao.fechaConexao();
		}
		return usuario;
	}

	public boolean emailExiste(String email) {
		try {
			final String sql = "select * from usuario where email = ?";
			PreparedStatement statement = Conexao.getConexao()
					.prepareStatement(sql);
			statement.setString(1, email);
			final ResultSet resultSet = statement.executeQuery();
			return resultSet.next();
		} catch (final SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			Conexao.fechaConexao();
		}
		return false;
	}

	public boolean loginExiste(String login) {
		try {
			final String sql = "select * from usuario where login = ?";
			PreparedStatement statement = Conexao.getConexao()
					.prepareStatement(sql);
			statement.setString(1, login);
			final ResultSet resultSet = statement.executeQuery();
			return resultSet.next();
		} catch (final SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			Conexao.fechaConexao();
		}
		return false;
	}
}
