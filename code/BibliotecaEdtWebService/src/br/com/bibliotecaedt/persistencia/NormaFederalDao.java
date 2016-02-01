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
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.bibliotecaedt.enumerado.EsferaEnum;
import br.com.bibliotecaedt.enumerado.TipoDeNormaEnum;
import br.com.bibliotecaedt.modelo.EsferaFederal;
import br.com.bibliotecaedt.modelo.Norma;
import br.com.bibliotecaedt.modelo.TipoDeNorma;
import br.com.bibliotecaedt.utilitario.Util;

/**
 * Classe responsável pelas operações de banco referentes a normas federais.
 *
 */
class NormaFederalDao {

    /**
     * Instância de {@link NormaDao}.
     */
    private static NormaFederalDao instancia;

    /**
     * Instância de {@link EsferaFederalDao}
     */
    private final EsferaFederalDao esferaFederalDao;

    /**
     * Instância de {@link TipoNormaDao}
     */
    private final TipoNormaDao tipoNormaDao;

    /**
     * Construtor privado para obrigaar a obter uma instância da classe através
     * do método getInstancia.
     */
    private NormaFederalDao() {
	tipoNormaDao = TipoNormaDao.getInstancia();
	esferaFederalDao = EsferaFederalDao.getInstancia();
    }

    /**
     * Retorna um instância de {@link NormaFederalDao}.
     * 
     * @return
     */
    public static NormaFederalDao getInstancia() {
	if (instancia == null) {
	    instancia = new NormaFederalDao();
	}
	return instancia;
    }

    /**
     * @param normas
     */
    public void salvar(final HashMap<TipoDeNormaEnum, List<Norma>> normas) {
	final Connection connection = Conexao.getConexao();
	Savepoint savepoint = null;

	try {
	    connection.setAutoCommit(false);
	    savepoint = connection.setSavepoint();

	    deletarNormasFederais(connection);

	    salvarNormas(normas, TipoDeNormaEnum.LEI, connection);

	    salvarNormas(normas, TipoDeNormaEnum.DECRETO, connection);

	    connection.commit();

	} catch (final SQLException exception) {
	    try {
		System.out.println("Salvar Normas: Erro => "
			+ exception.getMessage());
		connection.rollback(savepoint);
		connection.close();
		Conexao.fechaConexao();
	    } catch (final SQLException e) {
		System.out.println("Salvar Normas: Erro ao fechar conexão => "
			+ e.getMessage());
		e.printStackTrace();
	    }
	}
    }

    /**
     * Deleta normas federais.
     * 
     * @param connection
     *            uma conexão de banco de dados
     * @throws SQLException
     *             se ocorrer erro nas transações de banco de dados.
     */
    private void deletarNormasFederais(final Connection connection)
	    throws SQLException {
	final String sql = "delete from esfera where esfera.descricao = ?";
	final PreparedStatement preparedStatement = connection
		.prepareStatement(sql);
	preparedStatement.setString(1, EsferaEnum.FEDERAL.getNome());
	preparedStatement.executeUpdate();
	preparedStatement.close();
    }

    /**
     * Salva decretos.
     * 
     * @param normas
     *            {@link Map} de normas
     * @param tipoDeNormaEnum
     *            enumerado que indica o tipo de norma
     * @param connection
     *            conexão jdbc
     * @throws SQLException
     *             se ocorrer erro nas transações de banco de dados.
     */
    private void salvarNormas(
	    final HashMap<TipoDeNormaEnum, List<Norma>> normas,
	    final TipoDeNormaEnum tipoDeNormaEnum, final Connection connection)
	    throws SQLException {
	if (normas.containsKey(tipoDeNormaEnum)) {
	    final List<Norma> decretos = new ArrayList<Norma>();
	    decretos.addAll(normas.get(tipoDeNormaEnum));
	    final boolean decretosValidos = preparaNormas(decretos,
		    tipoDeNormaEnum, connection);
	    if (decretosValidos) {
		for (final Norma norma : decretos) {
		    salvar(norma, connection);
		}
	    }
	}
    }

    /**
     * Método usado para buscar e preparar todas as informações necessárias para
     * registro de uma norma.
     * 
     * @param normas
     *            lista de normas
     * @param tipoDeNormaEnum
     *            enumerado que representa o tipo de norma
     * @param connection
     *            uma conexão de banco de dados
     * @return true se os dados foram registrados com sucesso, false se ocorrer
     *         algum erro
     * @throws SQLException
     *             se ocorrer erro nas transações de banco de dados.
     */
    private boolean preparaNormas(final List<Norma> normas,
	    final TipoDeNormaEnum tipoDeNormaEnum, final Connection connection)
	    throws SQLException {
	final EsferaFederal esferaEstadual = esferaFederalDao
		.cadastrar(connection);
	final TipoDeNorma tipoDeNorma = tipoNormaDao.cadastrarTipo(
		tipoDeNormaEnum, connection);
	if (temValoresValidos(esferaEstadual, tipoDeNorma)) {
	    for (final Norma norma : normas) {
		norma.setTipoDeNorma(tipoDeNorma);
		norma.setEsfera(esferaEstadual);
	    }
	    return true;
	}
	return false;
    }

    /**
     * Verifica se os valores são válidos para o cadastro de uma norma.
     * 
     * @param esferaFederal
     *            classe que representa a esfera federal
     * @param tipoDeNorma
     *            classe que representa o tipo de norma
     * @return true se valores válidos, false se valores inválidos
     */
    private boolean temValoresValidos(final EsferaFederal esferaFederal,
	    final TipoDeNorma tipoDeNorma) {
	return esferaFederal != null && esferaFederal.estaCadastrado()
		&& tipoDeNorma != null && tipoDeNorma.estaCadastrado();
    }

    /**
     * Salvar normas
     * 
     * @param norma
     *            uma norma com valores válidos
     * @param connection
     *            uma conexão de banco de dados
     * @throws SQLException
     *             se ocorrer erro nas transações de banco de dados.
     */
    private void salvar(final Norma norma, final Connection connection)
	    throws SQLException {
	if (norma.eValida()) {
	    final String sql = "insert into norma "
		    + "(numero, ano, data_publicacao, resumo, descricao, fk_id_tipo, fk_id_esfera)"
		    + " values (?,?,?,?,?,?,?)";
	    final PreparedStatement statement = connection
		    .prepareStatement(sql);
	    statement.setString(1, norma.getNumero());
	    statement.setString(2, norma.getAno());
	    statement.setDate(3,
		    Util.StringParaSqlDate(norma.getDataPublicacao()));
	    statement.setString(4, norma.getResumo());
	    statement.setString(5, norma.getDescricao());
	    statement.setInt(6, norma.getTipoDeNorma().getIdentificador());
	    statement.setInt(7, norma.getEsfera().getIdEsfera());
	    statement.execute();
	}
    }

    /**
     * Busca normas federais
     * 
     * @param tipoDeNormaEnum
     *            lei ou decreto
     * @param numero
     *            identicador da norma
     * @param inicio
     *            determina o indice inicial de pesquisa
     * @param limite
     *            determina o limite de retorno
     * @return
     */
    public List<Norma> buscar(final TipoDeNormaEnum tipoDeNormaEnum,
	    final String numero, final Integer limite, final Integer inicio) {
	final Connection connection = Conexao.getConexao();
	final List<Norma> normas = new ArrayList<Norma>();
	try {
	    String sql = "select norma.id_norma, norma.numero, norma.ano, "
		    + "norma.data_publicacao, norma.resumo, norma.descricao, "
		    + "tipo.id_tipo, tipo.descricao "
		    + "from norma, tipo, esfera_federal "
		    + "where norma.fk_id_esfera = esfera_federal.id_esfera "
		    + "and norma.fk_id_tipo = tipo.id_tipo %s %s";

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
		norma.setDataPublicacao(Util.converterParaDataBrasileira(resultSet.getString(4)));
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
     * Busca o quantidade de total de norma para determinado filtros
     * 
     * @param tipoDeNorma
     * @param numero
     * @return
     */
    public int total(final TipoDeNormaEnum tipoDeNorma, final String numero) {
	final Connection connection = Conexao.getConexao();
	int count = 0;
	try {
	    String sql = "select count(norma.id_norma) from norma, esfera_federal %s where norma.fk_id_esfera = esfera_federal.id_esfera %s %s";

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
     * Buscar todos os anos que possuem normas cadastradas
     * 
     * @return
     */
    public List<Norma> buscarAnos() {
	final Connection connection = Conexao.getConexao();
	final List<Norma> normas = new ArrayList<Norma>();
	try {
	    final String sql = "select distinct norma.ano from norma, esfera_federal where norma.fk_id_esfera = esfera_federal.id_esfera";

	    final PreparedStatement preparedStatement = connection
		    .prepareStatement(sql);
	    final ResultSet resultSet = preparedStatement.executeQuery();
	    while (resultSet.next()) {
		final Norma norma = new Norma();
		norma.setAno(resultSet.getString(1));
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
     * Busca normas de um ano específico
     * 
     * @param ano
     * @return
     */
    public List<Norma> buscarPorAno(final String ano) {
	final Connection connection = Conexao.getConexao();
	final List<Norma> normas = new ArrayList<Norma>();
	try {
	    final String sql = "select norma.id_norma, norma.numero, norma.ano, "
		    + "norma.data_publicacao, norma.resumo, norma.descricao, "
		    + "tipo.id_tipo, tipo.descricao "
		    + "from norma, tipo, esfera_federal "
		    + "where norma.fk_id_esfera = esfera_federal.id_esfera "
		    + "and norma.fk_id_tipo = tipo.id_tipo "
		    + "and norma.ano like '" + ano + "'";

	    final PreparedStatement preparedStatement = connection
		    .prepareStatement(sql);
	    final ResultSet resultSet = preparedStatement.executeQuery();
	    while (resultSet.next()) {
		final Norma norma = new Norma();
		norma.setIdentificador(resultSet.getInt(1));
		norma.setNumero(resultSet.getString(2));
		norma.setAno(resultSet.getString(3));
		norma.setDataPublicacao(Util.converterParaDataBrasileira(resultSet.getString(4)));
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
}
