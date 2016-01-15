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
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.bibliotecaedt.enumerado.EsferaEnum;
import br.com.bibliotecaedt.enumerado.EstadoEnum;
import br.com.bibliotecaedt.enumerado.TipoDeNormaEnum;
import br.com.bibliotecaedt.modelo.EsferaEstadual;
import br.com.bibliotecaedt.modelo.Estado;
import br.com.bibliotecaedt.modelo.Norma;
import br.com.bibliotecaedt.modelo.TipoDeNorma;
import br.com.bibliotecaedt.utilitario.Util;

/**
 * Classe responsável pelas operações de banco referentes a normas.
 * 
 */
public class NormaDao {

    /**
     * Instância de {@link NormaDao}.
     */
    private static NormaDao instancia;

    /**
     * Instância de {@link EsferaEstadualDao}
     */
    private final EsferaEstadualDao esferaEstadualDao;

    /**
     * Instância de {@link TipoNormaDao}
     */
    private final TipoNormaDao tipoNormaDao;

    /**
     * Instância de {@link EstadoDao}
     */
    private final EstadoDao estadoDao;

    /**
     * Construtor privado para obrigaar a obter uma instância da classe através
     * do método getInstancia.
     */
    private NormaDao() {
	esferaEstadualDao = EsferaEstadualDao.getInstancia();
	tipoNormaDao = TipoNormaDao.getInstancia();
	estadoDao = EstadoDao.getInstancia();
    }

    /**
     * Retorna um instância de {@link NormaDao}.
     * 
     * @return
     */
    public static NormaDao getInstancia() {
	if (instancia == null) {
	    instancia = new NormaDao();
	}
	return instancia;
    }

    /**
     * Método usado para atualizar a base de dados com novas normas. Este método
     * contém regras importantes para a persistência de normas, veja os passos
     * informados abaixo. <br>
     * 
     * <B>Note:</B>
     * <P>
     * # passo 1: deleta todas as normas de um determinado lugar de acordo com o
     * tipo da esfera.
     * <P>
     * # passo 2: registra todas as informações predecessoras necessárias para
     * um norma (estado, municipio, esfera, tipo).
     * <P>
     * # passo 3: salva efetivamente as novas normas na base de dados.
     * <P>
     * # passo 4: caso ocorra alguma exceção em qualquer etapa de registro de um
     * norma é realizado um roolback e a base de dados para o estado anterior a
     * deleção das normas.
     * 
     * @param normas
     *            map com leis e decretos
     * @param estadoEnum
     *            enumerado que representado o estado, <code>EstadoEnum</code>
     * @param esferaEnum
     *            enumerado que representado a esferao, <code>EsferaEnum</code>
     */
    public void salvar(final HashMap<TipoDeNormaEnum, List<Norma>> normas,
	    final EstadoEnum estadoEnum, final EsferaEnum esferaEnum) {
	if (normas == null || normas.isEmpty()) {
	    return;
	}

	if (esferaEnum != null) {
	    switch (esferaEnum) {
	    case ESTADUAL:

		final Connection connection = Conexao.getConexao();
		Savepoint savepoint = null;

		try {
		    connection.setAutoCommit(false);
		    savepoint = connection.setSavepoint();

		    deletarNormasEstaduais(estadoEnum, connection);

		    salvarLeis(normas, estadoEnum, esferaEnum, connection);

		    salvarDecretos(normas, estadoEnum, esferaEnum, connection);

		    connection.commit();

		} catch (final SQLException exception) {
		    try {
			System.out.println("Salvar Normas: Erro => "
				+ exception.getMessage());
			connection.rollback(savepoint);
			connection.close();
			Conexao.fechaConexao();
		    } catch (final SQLException e) {
			System.out
				.println("Salvar Normas: Erro ao fechar conexão => "
					+ e.getMessage());
			e.printStackTrace();
		    }
		}
		break;
	    case FEDERAL:
		NormaFederalDao.getInstancia().salvar(normas);
		break;
	    default: // MUNICIPAL
		break;
	    }
	}

    }

    /**
     * Deleta normas estaduais de um determinado estado.
     * 
     * @param estadoEnum
     *            enumerado que representado o estado
     * @param connection
     *            uma conexão de banco de dados
     * @throws SQLException
     *             se ocorrer erro nas transações de banco de dados.
     */
    private void deletarNormasEstaduais(final EstadoEnum estadoEnum,
	    final Connection connection) throws SQLException {
	String sql = "";
	PreparedStatement preparedStatement = null;
	sql = "delete from esfera where esfera.id_esfera in (select esfera_estadual.id_esfera "
		+ "from esfera_estadual, estado where esfera_estadual.fk_id_estado = estado.id_estado "
		+ "and estado.uf = ?)";
	preparedStatement = connection.prepareStatement(sql);
	preparedStatement.setString(1, estadoEnum.getUf());
	preparedStatement.executeUpdate();
	preparedStatement.close();
    }

    /**
     * Salva decretos.
     * 
     * @param normas
     *            {@link Map} de normas
     * @param estadoEnum
     *            enumerado que indica o estada da federação
     * @param esferaEnum
     *            enumerado que indica o esfera politica
     * @param connection
     *            conexão jdbc
     * @throws SQLException
     *             se ocorrer erro nas transações de banco de dados.
     */
    private void salvarDecretos(
	    final HashMap<TipoDeNormaEnum, List<Norma>> normas,
	    final EstadoEnum estadoEnum, final EsferaEnum esferaEnum,
	    final Connection connection) throws SQLException {
	if (normas.containsKey(TipoDeNormaEnum.DECRETO)) {
	    final List<Norma> decretos = new ArrayList<Norma>();
	    decretos.addAll(normas.get(TipoDeNormaEnum.DECRETO));
	    final boolean decretosValidos = preparaNormas(decretos, estadoEnum,
		    esferaEnum, TipoDeNormaEnum.DECRETO, connection);
	    if (decretosValidos) {
		for (final Norma norma : decretos) {
		    salvar(norma, connection);
		}
	    }
	}
    }

    /**
     * Salva leis.
     * 
     * @param normas
     *            {@link Map} de normas
     * @param estadoEnum
     *            enumerado que indica o estada da federação
     * @param esferaEnum
     *            enumerado que indica o esfera politica
     * @param connection
     *            conexão jdbc
     * @throws SQLException
     *             se ocorrer erro nas transações de banco de dados.
     */
    private void salvarLeis(final HashMap<TipoDeNormaEnum, List<Norma>> normas,
	    final EstadoEnum estadoEnum, final EsferaEnum esferaEnum,
	    final Connection connection) throws SQLException {
	if (normas.containsKey(TipoDeNormaEnum.LEI)) {
	    final List<Norma> leis = new ArrayList<Norma>();
	    leis.addAll(normas.get(TipoDeNormaEnum.LEI));
	    final boolean leisValidas = preparaNormas(leis, estadoEnum,
		    esferaEnum, TipoDeNormaEnum.LEI, connection);
	    if (leisValidas) {
		for (final Norma norma : leis) {
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
     * @param estadoEnum
     *            enumerado que representa o estado
     * @param esferaEnum
     *            enumerado que representa o tipo de esfera
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
	    final EstadoEnum estadoEnum, final EsferaEnum esferaEnum,
	    final TipoDeNormaEnum tipoDeNormaEnum, final Connection connection)
	    throws SQLException {
	final Estado estado = estadoDao.cadastrar(estadoEnum, connection);
	final EsferaEstadual esferaEstadual = esferaEstadualDao
		.cadastrarEsferaEstadual(esferaEnum, estado, connection);
	final TipoDeNorma tipoDeNorma = tipoNormaDao.cadastrarTipo(
		tipoDeNormaEnum, connection);
	if (temValoresValidos(estado, esferaEstadual, tipoDeNorma)) {
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
     * @param estado
     *            classe que representa o estado
     * @param esferaEstadual
     *            classe que representa a esfera estadual
     * @param tipoDeNorma
     *            classe que representa o tipo de norma
     * @return true se valores válidos, false se valores inválidos
     */
    private boolean temValoresValidos(final Estado estado,
	    final EsferaEstadual esferaEstadual, final TipoDeNorma tipoDeNorma) {
	return estado != null && estado.estaCadastrado()
		&& esferaEstadual != null && esferaEstadual.estaCadastrado()
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
	final String sql = "insert into norma "
		+ "(numero, ano, data_publicacao, resumo, descricao, fk_id_tipo, fk_id_esfera)"
		+ " values (?,?,?,?,?,?,?)";
	final PreparedStatement statement = connection.prepareStatement(sql);
	statement.setString(1, norma.getNumero());
	statement.setString(2, norma.getAno());
	statement.setDate(3, Util.StringParaSqlDate(norma.getDataPublicacao()));
	statement.setString(4, norma.getResumo());
	statement.setString(5, norma.getDescricao());
	statement.setInt(6, norma.getTipoDeNorma().getIdentificador());
	statement.setInt(7, norma.getEsfera().getIdEsfera());
	statement.execute();
    }

    /**
     * @param esferaEnum
     * @param tipoDeNorma
     * @param numero
     * @param s
     * @param limite
     * @return
     */
    public List<Norma> buscarNormas(EsferaEnum esferaEnum, TipoDeNormaEnum tipoDeNorma,
	    String numero, Integer limite, Integer inicio) {
	List<Norma> normas = new ArrayList<Norma>();
	switch (esferaEnum) {
	case ESTADUAL:

	    break;
	case FEDERAL:
	    normas = NormaFederalDao.getInstancia().buscar(tipoDeNorma, numero,
		    limite, inicio);
	    break;
	default: // MUNICIPAL
	    break;
	}

	return normas;
    }

    /**
     * @param esferaEnum
     * @param tipoDeNorma
     * @param numero
     * @return
     */
    public int total(EsferaEnum esferaEnum, TipoDeNormaEnum tipoDeNorma, String numero) {
	switch (esferaEnum) {
	case ESTADUAL:
	    return 0;
	case FEDERAL:
	    return NormaFederalDao.getInstancia().total(tipoDeNorma, numero);
	default: // MUNICIPAL
	    return 0;
	}
    }

    /**
     * @param esferaEnum
     * @return
     */
    public List<Norma> buscarAnos(EsferaEnum esferaEnum) {
	switch (esferaEnum) {
	case ESTADUAL:
	    return null;
	case FEDERAL:
	    return NormaFederalDao.getInstancia().buscarAnos();
	default: // MUNICIPAL
	    return null;
	}
    }

    /**
     * @param esferaEnum
     * @param ano
     * @return
     */
    public List<Norma> buscarPorAno(EsferaEnum esferaEnum, String ano) {
	switch (esferaEnum) {
	case ESTADUAL:
	    return null;
	case FEDERAL:
	    return NormaFederalDao.getInstancia().buscarPorAno(ano);
	default: // MUNICIPAL
	    return null;
	}
    }
}
