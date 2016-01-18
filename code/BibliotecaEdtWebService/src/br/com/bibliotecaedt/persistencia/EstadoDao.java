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
import java.util.ArrayList;
import java.util.List;

import br.com.bibliotecaedt.enumerado.EstadoEnum;
import br.com.bibliotecaedt.modelo.Estado;
import br.com.bibliotecaedt.modelo.Municipio;

public class EstadoDao {

    private static EstadoDao instancia;

    public static EstadoDao getInstancia() {
	if (instancia == null) {
	    instancia = new EstadoDao();
	}
	return instancia;
    }

    /**
     * Verifica se o estado existe na base de dados, caso contrário registra um
     * novo estado.
     * 
     * @param estadoEnum
     * @param connection
     * @return instancia de um estado, antes de usar o retorna chamar o
     *         estaCadastro que indica se o objeto é válido.
     * @throws SQLException
     */
    public Estado cadastrar(final EstadoEnum estadoEnum,
	    final Connection connection) throws SQLException {
	Estado novoEstado = new Estado();
	novoEstado = buscarPorUf(estadoEnum.getUf(), connection);
	if (!novoEstado.estaCadastrado()) {
	    final String sql = "insert into estado (descricao, uf) values (?,?)";
	    final PreparedStatement statement = connection.prepareStatement(
		    sql, PreparedStatement.RETURN_GENERATED_KEYS);
	    statement.setString(1, estadoEnum.getNome());
	    statement.setString(2, estadoEnum.getUf());
	    statement.executeUpdate();
	    final ResultSet resultSet = statement.getGeneratedKeys();
	    if (resultSet.next()) {
		novoEstado.setIdentificador(resultSet.getInt(1));
		novoEstado.setDescricao(estadoEnum.getNome());
		novoEstado.setUf(estadoEnum.getUf());
	    }
	}
	return novoEstado;
    }

    /**
     * Buscar um estado por UF.
     * 
     * @param uf
     * @param connection
     * @return objeto estado, chegar método estaCadastrado.
     * @throws SQLException
     */
    private Estado buscarPorUf(final String uf, final Connection connection)
	    throws SQLException {
	final Estado estado = new Estado();
	final String sql = "select * from estado where uf = ?";
	final PreparedStatement statement = connection.prepareStatement(sql);
	statement.setString(1, uf);
	final ResultSet resultSet = statement.executeQuery();
	if (resultSet.next()) {
	    System.out.println("Estado " + uf + " já existe!");
	    estado.setIdentificador(resultSet.getInt(1));
	    estado.setDescricao(resultSet.getString(2));
	    estado.setUf(resultSet.getString(3));
	}
	return estado;
    }

    /**
     * @return
     */
    public List<Estado> buscarEstados() {
	final List<Estado> estados = new ArrayList<Estado>();
	final Connection connection = Conexao.getConexao();
	try {
	    final String sql = "select distinct * from estado";
	    final PreparedStatement preparedStatement = connection
		    .prepareStatement(sql);
	    final ResultSet resultSet = preparedStatement.executeQuery();
	    while (resultSet.next()) {

		final Estado estado = new Estado();
		estado.setIdentificador(resultSet.getInt(1));
		estado.setDescricao(resultSet.getString(2));
		estado.setUf(resultSet.getString(3));

		final PreparedStatement statement = connection
			.prepareStatement("select * from municipio where fk_id_estado = ?");
		statement.setInt(1, estado.getIdentificador());
		final ResultSet result = statement.executeQuery();
		List<Municipio> municipios = new ArrayList<Municipio>();
		while (result.next()) {
		    final Municipio municipio = new Municipio();
		    municipio.setIdentifiacador(result.getInt(1));
		    municipio.setDescricao(result.getString(2));
		    municipio.setEstado(estado);
		    municipios.add(municipio);
		}
		estado.setMunicipios(municipios);
		estados.add(estado);
	    }
	} catch (final SQLException exception) {
	    System.out.println("Buscar Estados: Erro => "
		    + exception.getMessage());
	} finally {
	    try {
		connection.close();
		Conexao.fechaConexao();
	    } catch (final SQLException e) {
		System.out.println("Buscar Estados: Erro ao fechar conexão => "
			+ e.getMessage());
		e.printStackTrace();
	    }
	}
	return estados;
    }
}
