package br.com.bibliotecaedt.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;

import br.com.bibliotecaedt.modelo.Norma;
import br.com.bibliotecaedt.utilitario.Util;

public class NormaDao {

	public void salvar(final Norma norma) {
		try {
			final String sql = "insert into norma (numero, ano, data_publicacao, resumo, descricao)"
					+ " values (?,?,?,?,?)";
			final Connection connection = Conexao.getConexao();
			final PreparedStatement statement = connection
					.prepareStatement(sql);
			statement.setString(1, norma.getNumero());
			statement.setString(2, norma.getAno());
			statement.setDate(3, Util.StringParaSqlDate(norma.getDataPublicacao()));
			statement.setString(4, norma.getResumo());
			statement.setString(5, norma.getDescricao());
			statement.executeUpdate();
		} catch (final Exception e) {
			System.out.println("NormaDao - Salvar: " + e.getMessage());
		} finally {
			Conexao.fechaConexao();
		}

	}

}
