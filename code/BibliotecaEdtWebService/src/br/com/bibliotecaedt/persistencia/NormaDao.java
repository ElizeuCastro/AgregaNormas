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
import java.util.List;

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
	private EsferaEstadualDao esferaEstadualDao;

	/**
	 * Instância de {@link TipoNormaDao}
	 */
	private TipoNormaDao tipoNormaDao;

	/**
	 * Instância de {@link EstadoDao}
	 */
	private EstadoDao estadoDao;

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
	 *            lista de normas
	 * @param estadoEnum
	 *            enumerado que representado o estado, <code>EstadoEnum</code>
	 * @param esferaEnum
	 *            enumerado que representado a esferao, <code>EsferaEnum</code>
	 * @param tipoDeNormaEnum
	 *            enumerado que representado o tipo de norma,
	 *            <code>TipoDeNormaEnum</code>
	 */
	public void salvar(final List<Norma> normas, final EstadoEnum estadoEnum,
			final EsferaEnum esferaEnum, final TipoDeNormaEnum tipoDeNormaEnum) {
		if (normas == null || normas.isEmpty()) {
			return;
		}

		final Connection connection = Conexao.getConexao();
		Savepoint savepoint = null;

		try {

			connection.setAutoCommit(false);

			savepoint = connection.setSavepoint();

			deletarNormasEstaduais(estadoEnum, connection);

			final boolean valoresValidos = preparaNormas(normas, estadoEnum,
					esferaEnum, tipoDeNormaEnum, connection);
			if (!valoresValidos) {
				connection.rollback(savepoint);
				connection.close();
				Conexao.fechaConexao();
				return;
			}

			for (final Norma norma : normas) {
				salvar(norma, connection);
			}

			connection.commit();

		} catch (SQLException exception) {
			try {
				System.out.println("Salvar Normas: Erro => "
						+ exception.getMessage());
				connection.rollback(savepoint);
				connection.close();
				Conexao.fechaConexao();
			} catch (SQLException e) {
				System.out.println("Salvar Normas: Erro ao fechar conexão =>"
						+ e.getMessage());
				e.printStackTrace();
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
	private boolean preparaNormas(List<Norma> normas, EstadoEnum estadoEnum,
			EsferaEnum esferaEnum, TipoDeNormaEnum tipoDeNormaEnum,
			Connection connection) throws SQLException {
		final Estado estado = estadoDao.cadastrar(estadoEnum, connection);
		final EsferaEstadual esferaEstadual = esferaEstadualDao
				.cadastrarEsferaEstadual(esferaEnum, estado, connection);
		final TipoDeNorma tipoDeNorma = tipoNormaDao.cadastrarTipo(
				tipoDeNormaEnum, connection);
		if (temValoresValidos(estado, esferaEstadual, tipoDeNorma)) {
			for (Norma norma : normas) {
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
}
