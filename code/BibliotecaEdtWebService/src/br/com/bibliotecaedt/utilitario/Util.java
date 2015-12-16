package br.com.bibliotecaedt.utilitario;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class Util {

	public static java.sql.Date StringParaSqlDate(String data) {
		java.sql.Date sqlDate = null;
		try {
			data = data.replace(".", "-");
			SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd");
			final Date date = format.parse(data);
			sqlDate = new java.sql.Date(date.getTime());
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
		}
		return sqlDate;
	}

	public static boolean estaVazio(String... valores) {
		if (valores != null) {
			for (String valor : valores) {
				if (valor != null && valor.isEmpty()) {
					return true;
				}
			}
		}
		return false;

	}

}
