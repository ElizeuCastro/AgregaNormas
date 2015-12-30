package br.com.bibliotecaedt.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.bibliotecaedt.enumerado.EsferaEnum;
import br.com.bibliotecaedt.modelo.Esfera;
import br.com.bibliotecaedt.modelo.EsferaEstadual;
import br.com.bibliotecaedt.modelo.Estado;

/**
 * Classe reponsável pelas operações de banco referentes a esfera estadual.
 */
public final class EsferaEstadualDao {

	/**
	 * Armazena a instância única da classe;
	 */
	private static EsferaEstadualDao instancia;

	/**
	 * Armazena a instância de {@link EsferaDao}.
	 */
	private final EsferaDao esferaDao;

	/**
	 * 
	 */
	private EsferaEstadualDao() {
		esferaDao = EsferaDao.getInstancia();
	}

	/**
	 * Pega a instância da classe.
	 * 
	 * @return
	 */
	public static EsferaEstadualDao getInstancia() {
		if (instancia == null) {
			instancia = new EsferaEstadualDao();
		}
		return instancia;
	}

	/**
	 * Cadatra esfera estadual caso ainda não exista registro da esfera no banco
	 * de dados. <br>
	 *
	 * # regra 1: retorna sempre a instancia de uma esfera. <br>
	 * # regra 2: sempre verificar o método estaCadastrado, para verificar se a
	 * norma é válida.
	 * 
	 * @param esferaEnum
	 * @param estado
	 * @param connection
	 * @return instancia de uma esfera estadual
	 * @throws SQLException 
	 */
	public EsferaEstadual cadastrarEsferaEstadual(final EsferaEnum esferaEnum,
			final Estado estado, Connection connection) throws SQLException {
		EsferaEstadual esferaEstadual = new EsferaEstadual();
		if (estado == null || !estado.estaCadastrado()) {
			return new EsferaEstadual();
		}
		esferaEstadual = buscarEsferaPorUf(estado.getUf(), connection);
		if (!esferaEstadual.estaCadastrado()) {
			final Esfera esfera = esferaDao.cadastrarEsfera(esferaEnum, connection);
			if (esfera.estaCadastrado()) {
				final String sql = "insert into esfera_estadual (id_esfera, fk_id_estado) values (?,?)";
				final PreparedStatement preparedStatement = connection
						.prepareStatement(sql);
				preparedStatement.setInt(1, esfera.getIdEsfera());
				preparedStatement.setInt(2, estado.getIdentificador());
				if (preparedStatement.executeUpdate() == 1) {
					esferaEstadual.setIdEsfera(esfera.getIdEsfera());
					esferaEstadual.setDescricao(esferaEnum.getNome());
					esferaEstadual.setEstado(estado);
				}
			}
		}
		return esferaEstadual;
	}

	/**
	 * Busca um esfera estadual no banco de dados pela UF. <br>
	 * 
	 * # regra 1: retorna sempre a instancia de uma esfera. <br>
	 * # regra 2: sempre verificar o método estaCadastrado, para verificar se a
	 * norma é válida.
	 * 
	 * @param uf
	 * @param connection
	 * @return instancia de uma esfera estadual
	 * @throws SQLException
	 */
	private EsferaEstadual buscarEsferaPorUf(final String uf,
			final Connection connection) throws SQLException {
		final EsferaEstadual esferaEstadual = new EsferaEstadual();
		final String sql = "select * from esfera_estadual "
				+ "inner join estado on estado.id_estado = esfera_estadual.fk_id_estado and estado.uf = ?";
		final PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, uf);
		final ResultSet resultSet = statement.executeQuery();
		if (resultSet.next()) {
			System.out.println("Esfera estadual existente");
			esferaEstadual.setIdEsfera(resultSet
					.getInt(EsferaEstadual.TB_CAMPO_ID_ESFERA));
			esferaEstadual.setDescricao(resultSet
					.getString(EsferaEstadual.TB_CAMPO_DESCRICAO));
			// esferaEstadual.setIdEsferaEstadual(resultSet
			// .getInt(EsferaEstadual.TB_CAMPO_ID_ESFERA_ESTADUAL));
			final Estado estado = new Estado();
			estado.setIdentificador(resultSet.getInt(Estado.TB_CAMPO_ID_ESTADO));
			estado.setDescricao(resultSet.getString(Estado.TB_CAMPO_DESCRICAO));
			estado.setUf(resultSet.getString(Estado.TB_CAMPO_UF));
			esferaEstadual.setEstado(estado);
		}
		return esferaEstadual;
	}
}
