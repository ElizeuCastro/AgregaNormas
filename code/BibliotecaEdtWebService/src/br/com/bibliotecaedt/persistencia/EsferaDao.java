package br.com.bibliotecaedt.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.bibliotecaedt.enumerado.EsferaEnum;
import br.com.bibliotecaedt.modelo.Esfera;

public class EsferaDao {

    private static EsferaDao instancia;

    public static EsferaDao getInstancia() {
	if (instancia == null) {
	    instancia = new EsferaDao();
	}
	return instancia;
    }

    public Esfera cadastrarEsfera(final EsferaEnum esferaEnum,
	    final Connection connection) throws SQLException {
	Esfera esfera = new Esfera();
	final String sql = "insert into esfera (descricao) values (?)";
	final PreparedStatement preparedStatement = connection
		.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
	preparedStatement.setString(1, esferaEnum.getNome());
	preparedStatement.executeUpdate();
	final ResultSet resultSet = preparedStatement.getGeneratedKeys();
	if (resultSet.next()) {
	    esfera.setIdEsfera(resultSet.getInt(1));
	    esfera.setDescricao(esferaEnum.getNome());
	}
	return esfera;
    }

    public Esfera buscarPorNome(final EsferaEnum esferaEnum,
	    final Connection connection) throws SQLException {
	final Esfera esfera = new Esfera();
	final String sql = "select * from esfera where descricao = ?";
	final PreparedStatement preparedStatement = connection
		.prepareStatement(sql);
	preparedStatement.setString(1, esferaEnum.getNome());
	final ResultSet resultSet = preparedStatement.executeQuery();
	if (resultSet.next()) {
	    esfera.setIdEsfera(resultSet.getInt(Esfera.TB_CAMPO_ID_ESFERA));
	    esfera.setDescricao(resultSet.getString(Esfera.TB_CAMPO_DESCRICAO));
	}
	return esfera;
    }

}
