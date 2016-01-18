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

import br.com.bibliotecaedt.enumerado.EsferaEnum;
import br.com.bibliotecaedt.modelo.Esfera;
import br.com.bibliotecaedt.modelo.EsferaFederal;

/**
 * Classe responsável pelas operações de banco referentes a esfera federal.
 *
 */
public class EsferaFederalDao {

    /**
     * Armazena a instância única da classe;
     */
    private static EsferaFederalDao instancia;

    /**
     * Armazena a instância de {@link EsferaDao}.
     */
    private final EsferaDao esferaDao;

    /**
	 * 
	 */
    private EsferaFederalDao() {
	esferaDao = EsferaDao.getInstancia();
    }

    /**
     * Singleton instance.
     * 
     * @return
     */
    public static EsferaFederalDao getInstancia() {
	if (instancia == null) {
	    instancia = new EsferaFederalDao();
	}
	return instancia;
    }

    /**
     * Cadastra esfera federal
     * 
     * <P>
     * # regra 1: retorna sempre a instancia de uma esfera. <br>
     * # regra 2: sempre verificar o método estaCadastrado, para verificar se a
     * esfera é válida.
     * 
     * @param connection
     * @return instancia de uma esfera federal
     * @throws SQLException
     */
    public EsferaFederal cadastrar(final Connection connection)
	    throws SQLException {
	Esfera esfera = new Esfera();
	esfera = esferaDao.buscarPorNome(EsferaEnum.FEDERAL, connection);
	if (!esfera.estaCadastrado()) {
	    esfera = esferaDao.cadastrarEsfera(EsferaEnum.FEDERAL, connection);
	}
	final EsferaFederal esferaFederal = buscarEsferaFederal(esfera,
		connection);
	if (!esferaFederal.estaCadastrado()) {
	    final String sql = "insert into esfera_federal (id_esfera) values (?)";
	    final PreparedStatement preparedStatement = connection
		    .prepareStatement(sql);
	    preparedStatement.setInt(1, esfera.getIdEsfera());
	    if (preparedStatement.executeUpdate() == 1) {
		esferaFederal.setIdEsfera(esfera.getIdEsfera());
		esferaFederal.setDescricao(EsferaEnum.FEDERAL.getNome());
	    }
	}
	return esferaFederal;
    }

    /**
     * Busca um esfera federal.
     *
     * <B>Note:</B>
     * <P>
     * # regra 1: retorna sempre a instancia de uma esfera.
     * <P>
     * # regra 2: sempre verificar o método estaCadastrado, para verificar se a
     * norma é válida.
     * 
     * @param idenficador
     *            da esfera
     * @param connection
     * @return instancia de uma esfera federal
     * @throws SQLException
     */
    private EsferaFederal buscarEsferaFederal(final Esfera esfera,
	    final Connection connection) throws SQLException {
	final EsferaFederal esferaFederal = new EsferaFederal();
	if (esfera != null && esfera.estaCadastrado()) {
	    final String sql = "select * from esfera_federal where esfera_federal.id_esfera = ?";
	    final PreparedStatement statement = connection
		    .prepareStatement(sql);
	    statement.setInt(1, esfera.getIdEsfera());
	    final ResultSet resultSet = statement.executeQuery();
	    if (resultSet.next()) {
		System.out.println("Esfera federal existente");
		esferaFederal.setIdEsfera(esfera.getIdEsfera());
		esferaFederal.setDescricao(esfera.getDescricao());
	    }
	}
	return esferaFederal;
    }

}
