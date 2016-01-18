package br.com.teste.util;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import br.com.bibliotecaedt.utilitario.Util;

public class UtilTeste {

	@Test
	public void StringToDateTest() {
		Date date = Util.StringParaSqlDate("7.6.1998");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		assertEquals(1998, calendar.get(Calendar.YEAR));
		assertEquals(Calendar.JUNE, calendar.get(Calendar.MONTH));
		assertEquals(7, calendar.get(Calendar.DAY_OF_MONTH));
	}
}
