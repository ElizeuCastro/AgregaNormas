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
package br.com.bibliotecaedt.utilitario;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Classe de métodos utilitários.
 *
 */
public final class Util {

	/**
	 * Converte string para o formato de data do sql.
	 * 
	 * @param data
	 *            string com formato de data, (ex: '23.08.1977')
	 * @return nova data no formato de {@link java.sql.Date}, ou
	 *         <code>null</code> se ocorrer erro no parse de data
	 */
	public static java.sql.Date StringParaSqlDate(final String data) {
		java.sql.Date sqlDate = null;
		try {
			final String strDate = data.replace(".", "-");
			SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd");
			final Date date = format.parse(strDate);
			sqlDate = new java.sql.Date(date.getTime());
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
		}
		return sqlDate;
	}

	/**
	 * Verifica se a string não está vazia.
	 * 
	 * @param valores
	 *            texto(s) a serem válidados
	 * @return true se conter valores em branco, false se conter valores setados
	 */
	public static boolean estaVazio(final String... valores) {
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
