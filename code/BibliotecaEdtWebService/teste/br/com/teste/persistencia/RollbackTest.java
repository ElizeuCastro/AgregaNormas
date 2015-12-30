/**
 * Copyright (C) 2015 Biblioteca Edt.
 * 
 * Projeto final curso pós-graduação em:
 * Engenharia de Software com Ênfase em Desenvolvimento Web.
 * 
 * UNINORTE - Laureate.
 * 
 * @author elizeu
 * @author danilo
 */
package br.com.teste.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;

import org.junit.Test;

import br.com.bibliotecaedt.modelo.Usuario;
import br.com.bibliotecaedt.persistencia.Conexao;
import br.com.bibliotecaedt.persistencia.UsuarioDao;

public class RollbackTest {

	@Test
	public void cadastrarUsuarioTest() throws SQLException{
		
		final Connection connection = Conexao.getConexao();
		System.out.println(connection);
		connection.setAutoCommit(false);

		Savepoint savepoint2 = connection.setSavepoint();
		PreparedStatement statement1 = connection.prepareStatement("insert into usuario (nome, email, login, senha) values (?,?,?,?)");
		Usuario usuario1 = new Usuario("maria", "maria", "maria", "maria"); 
		statement1.setString(1, usuario1.getNome());
		statement1.setString(2, usuario1.getEmail());
		statement1.setString(3, usuario1.getLogin());
		statement1.setString(4, usuario1.getSenha());
		statement1.executeUpdate();	

		Savepoint savepoint = connection.setSavepoint();
		String sql = "delete from usuario";
		Statement statement = connection.createStatement();
		statement.execute(sql);
		
		System.out.println(Conexao.getConexao());
		Conexao.getConexao().rollback(savepoint);
		connection.commit();
		connection.close();
		
		
		
//		Conexao.getConexao().setAutoCommit(false);
//		UsuarioDao.getInstancia().cadastrar(new Usuario("elizeu1", "elizeu@teste", "vai1", "vali1"));
//		Conexao.getConexao().rollback();
	}
	
	
}
