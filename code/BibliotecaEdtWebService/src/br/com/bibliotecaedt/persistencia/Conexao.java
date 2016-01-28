package br.com.bibliotecaedt.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Conexao {

//    private static final String DB_USER = "root";
//    private static final String DB_PASSWORD = "root";
//    private static final String PATH_DB = "jdbc:mysql://localhost/bibliotecaedt";
    
    //jelastic db
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "ALIazp33552";
    private static final String PATH_DB = "jdbc:mysql://mysql80184-bibliotecaedt.jelasticlw.com.br/bibliotecaedtdb";
    private static Connection connection;

    public static Connection getConexao() {
	try {
	    Class.forName("com.mysql.jdbc.Driver");
	    connection = DriverManager.getConnection(PATH_DB, DB_USER,
		    DB_PASSWORD);
	} catch (SQLException e) {
	    throw new RuntimeException(e);
	} catch (ClassNotFoundException e) {
	    throw new RuntimeException(e);
	}
	return connection;
    }

    public static void fechaConexao() {
	if (connection != null) {
	    try {
		connection.close();
	    } catch (SQLException e) {
		throw new RuntimeException(e);
	    }
	}
    }
}
