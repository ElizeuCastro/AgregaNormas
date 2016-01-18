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
import java.util.IllegalFormatException;
import java.util.List;

import br.com.bibliotecaedt.enumerado.TipoDeNormaEnum;
import br.com.bibliotecaedt.modelo.Norma;
import br.com.bibliotecaedt.modelo.TipoDeNorma;

public class NormaEstadualDao {

    private static NormaEstadualDao instancia;

    public static NormaEstadualDao getInstancia() {
	if (instancia == null) {
	    instancia = new NormaEstadualDao();
	}
	return instancia;
    }

    /**
     * Busca anos que possuem normas cadastradas
     * 
     * @param estado
     * 
     * @return
     */
    public List<Norma> buscarAnos(final Integer estado) {
	if (estado == null) {
	    return new ArrayList<Norma>();
	}

	final Connection connection = Conexao.getConexao();
	final List<Norma> normas = new ArrayList<Norma>();
	try {
	    final String sql = "select distinct norma.ano "
		    + "from norma, esfera, esfera_estadual, estado "
		    + "where esfera.id_esfera = esfera_estadual.id_esfera "
		    + "and esfera_estadual.fk_id_estado = estado.id_estado "
		    + "and estado.id_estado = "
		    + estado
		    + " and norma.fk_id_esfera = esfera.id_esfera order by 1 desc";

	    final PreparedStatement preparedStatement = connection
		    .prepareStatement(sql);
	    final ResultSet resultSet = preparedStatement.executeQuery();
	    while (resultSet.next()) {
		final Norma norma = new Norma();
		norma.setAno(resultSet.getString(1));
		normas.add(norma);
	    }
	} catch (final SQLException exception) {
	    System.out.println("Buscar Normas Estaduais: Erro => "
		    + exception.getMessage());
	} finally {
	    try {

		connection.close();
		Conexao.fechaConexao();
	    } catch (final SQLException e) {
		System.out
			.println("Buscar Normas Estaduais: Erro ao fechar conexão => "
				+ e.getMessage());
		e.printStackTrace();
	    }
	}

	return normas;
    }

    /**
     * Busca todas as normas de um determinada ano
     * 
     * @param ano
     * @param estado
     * @return
     */
    public List<Norma> buscarPorAno(final String ano, final Integer estado) {
	final Connection connection = Conexao.getConexao();
	final List<Norma> normas = new ArrayList<Norma>();
	try {
	    final String sql = "select norma.id_norma, norma.numero, norma.ano, "
		    + "norma.data_publicacao, norma.resumo, norma.descricao, "
		    + "tipo.id_tipo, tipo.descricao "
		    + "from norma, tipo, esfera, esfera_estadual, estado "
		    + "where norma.fk_id_esfera = esfera.id_esfera "
		    + "and esfera.id_esfera = esfera_estadual.id_esfera "
		    + "and norma.fk_id_tipo = tipo.id_tipo "
		    + "and esfera_estadual.fk_id_estado = estado.id_estado "
		    + "and estado.id_estado = "
		    + estado
		    + " and norma.ano like '" + ano + "'";

	    final PreparedStatement preparedStatement = connection
		    .prepareStatement(sql);
	    final ResultSet resultSet = preparedStatement.executeQuery();
	    while (resultSet.next()) {
		final Norma norma = new Norma();
		norma.setIdentificador(resultSet.getInt(1));
		norma.setNumero(resultSet.getString(2));
		norma.setAno(resultSet.getString(3));
		norma.setDataPublicacao(resultSet.getString(4));
		norma.setResumo(resultSet.getString(5));
		norma.setDescricao(resultSet.getString(6));
		final TipoDeNorma tipoDeNorma = new TipoDeNorma(
			resultSet.getInt(7), resultSet.getString(8));
		norma.setTipoDeNorma(tipoDeNorma);
		normas.add(norma);
	    }
	} catch (final SQLException exception) {
	    System.out.println("Buscar Normas Federais: Erro => "
		    + exception.getMessage());
	} finally {
	    try {
		connection.close();
		Conexao.fechaConexao();
	    } catch (final SQLException e) {
		System.out
			.println("Buscar Normas Federais: Erro ao fechar conexão => "
				+ e.getMessage());
		e.printStackTrace();
	    }
	}
	return normas;
    }

    /**
     * Busca total de normas de determinado estado
     * 
     * @param tipoDeNorma
     * @param numero
     * @param estado
     * @return
     */
    public int total(final TipoDeNormaEnum tipoDeNorma, final String numero,
	    final Integer estado) {
	if (estado == null) {
	    return 0;
	}

	final Connection connection = Conexao.getConexao();
	int count = 0;
	try {
	    String sql = "select count(norma.id_norma) "
		    + "from norma, esfera, esfera_estadual, estado %s "
		    + "where norma.fk_id_esfera = esfera.id_esfera "
		    + "and esfera_estadual.fk_id_estado = estado.id_estado "
		    + "and estado.id_estado = " + estado
		    + " and esfera.id_esfera = esfera_estadual.id_esfera %s %s";

	    String tableTipo = "";
	    String filtroTipo = "";
	    if (tipoDeNorma != null) {
		tableTipo = ", tipo";
		filtroTipo = " and norma.fk_id_tipo = tipo.id_tipo and tipo.descricao like'"
			+ tipoDeNorma.getNome() + "'";
	    }

	    String filtroNumero = "";
	    if (numero != null && !numero.isEmpty()) {
		filtroNumero = " and norma.numero like " + "'%" + numero + "%'";
	    }

	    sql = String.format(sql, tableTipo, filtroTipo, filtroNumero);

	    final PreparedStatement preparedStatement = connection
		    .prepareStatement(sql);
	    final ResultSet resultSet = preparedStatement.executeQuery();
	    if (resultSet.next()) {
		count = resultSet.getInt(1);
	    }
	} catch (final SQLException exception) {
	    System.out.println("Buscar Normas Federais: Erro => "
		    + exception.getMessage());
	} catch (final IllegalFormatException exception) {
	    System.out.println("Buscar Normas Federais: Erro => "
		    + exception.getMessage());
	} finally {
	    try {

		connection.close();
		Conexao.fechaConexao();
	    } catch (final SQLException e) {
		System.out
			.println("Buscar Normas Federais: Erro ao fechar conexão => "
				+ e.getMessage());
		e.printStackTrace();
	    }
	}
	return count;
    }

    /**
     * Busca normas
     * 
     * @param tipoDeNorma
     * @param numero
     * @param limite
     * @param inicio
     * @param estado
     * @return
     */
    public List<Norma> buscar(TipoDeNormaEnum tipoDeNormaEnum, String numero,
	    Integer limite, Integer inicio, Integer estado) {
	final Connection connection = Conexao.getConexao();
	final List<Norma> normas = new ArrayList<Norma>();
	try {
	    String sql = "select norma.id_norma, norma.numero, norma.ano, "
		    + "norma.data_publicacao, norma.resumo, norma.descricao, "
		    + "tipo.id_tipo, tipo.descricao "
		    + "from norma, tipo, esfera_estadual, esfera, estado "
		    + "where norma.fk_id_esfera = esfera.id_esfera "
		    + "and esfera.id_esfera = esfera_estadual.id_esfera "
		    + "and esfera_estadual.fk_id_estado = estado.id_estado "
		    + "and estado.id_estado = " + estado
		    + " and norma.fk_id_tipo = tipo.id_tipo %s %s";

	    String filtroTipo = "";
	    if (tipoDeNormaEnum != null) {
		filtroTipo = " and tipo.descricao = '"
			+ tipoDeNormaEnum.getNome() + "'";
	    }

	    String filtroNumero = "";
	    if (numero != null && !numero.isEmpty()) {
		filtroNumero = " and norma.numero like " + "'%" + numero + "%'";
	    }

	    sql = String.format(sql, filtroTipo, filtroNumero);
	    sql += " LIMIT " + limite + " OFFSET " + inicio;

	    final PreparedStatement preparedStatement = connection
		    .prepareStatement(sql);
	    final ResultSet resultSet = preparedStatement.executeQuery();
	    while (resultSet.next()) {
		final Norma norma = new Norma();
		norma.setIdentificador(resultSet.getInt(1));
		norma.setNumero(resultSet.getString(2));
		norma.setAno(resultSet.getString(3));
		norma.setDataPublicacao(resultSet.getString(4));
		norma.setResumo(resultSet.getString(5));
		norma.setDescricao(resultSet.getString(6));
		final TipoDeNorma tipoDeNorma = new TipoDeNorma(
			resultSet.getInt(7), resultSet.getString(8));
		norma.setTipoDeNorma(tipoDeNorma);
		normas.add(norma);
	    }
	} catch (final SQLException exception) {
	    System.out.println("Buscar Normas Estaduais: Erro => "
		    + exception.getMessage());
	} finally {
	    try {

		connection.close();
		Conexao.fechaConexao();
	    } catch (final SQLException e) {
		System.out
			.println("Buscar Normas Estaduais: Erro ao fechar conexão => "
				+ e.getMessage());
		e.printStackTrace();
	    }
	}

	return normas;
    }

}
