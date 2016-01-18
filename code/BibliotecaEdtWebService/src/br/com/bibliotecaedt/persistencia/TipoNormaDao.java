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
package br.com.bibliotecaedt.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.bibliotecaedt.enumerado.TipoDeNormaEnum;
import br.com.bibliotecaedt.modelo.TipoDeNorma;

/**
 * Classe reponsável pelas operações de banco de dados referentes a tipo de
 * norma.
 * 
 * @author BibliotecaEdt
 *
 */
public class TipoNormaDao {

	private static TipoNormaDao instancia;

	public static TipoNormaDao getInstancia() {
		if (instancia == null) {
			instancia = new TipoNormaDao();
		}
		return instancia;
	}

	/**
	 * Cadastra um tipo se o mesmo ainda não exitir no banco de dados;
	 * 
	 * @param tipo
	 * @param connection
	 * @return instancia de um tipo de norma
	 * @throws SQLException 
	 */
	public TipoDeNorma cadastrarTipo(final TipoDeNormaEnum tipo,
			Connection connection) throws SQLException {
		TipoDeNorma tipoDeNorma = new TipoDeNorma();
		tipoDeNorma = buscarTipoPorNome(tipo.getNome(), connection);
		if (!tipoDeNorma.estaCadastrado()) {
			final String sql = "insert into tipo (descricao) values (?)";
			final PreparedStatement preparedStatement = connection
					.prepareStatement(sql,
							PreparedStatement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, tipo.getNome());
			preparedStatement.executeUpdate();
			final ResultSet resultSet = preparedStatement.getGeneratedKeys();
			if (resultSet.next()) {
				tipoDeNorma.setIdentificador(resultSet.getInt(1));
				tipoDeNorma.setDescricao(tipo.getNome());
			}
		}
		return tipoDeNorma;
	}

	/**
	 * Busca determinado tipo de norma pelo nome.
	 * 
	 * @param nome
	 * @param connection
	 * @return
	 * @throws SQLException
	 */
	private TipoDeNorma buscarTipoPorNome(final String nome,
			final Connection connection) throws SQLException {
		final TipoDeNorma tipoDeNorma = new TipoDeNorma();
		final String sql = "select * from tipo where descricao = ?";
		final PreparedStatement preparedStatement = connection
				.prepareStatement(sql);
		preparedStatement.setString(1, nome);
		final ResultSet resultSet = preparedStatement.executeQuery();
		if (resultSet.next()) {
			tipoDeNorma.setIdentificador(resultSet
					.getInt(TipoDeNorma.TB_CAMPO_ID));
			tipoDeNorma.setDescricao(resultSet
					.getString(TipoDeNorma.TB_CAMPO_DESCRICAO));
		}
		return tipoDeNorma;
	}

}
