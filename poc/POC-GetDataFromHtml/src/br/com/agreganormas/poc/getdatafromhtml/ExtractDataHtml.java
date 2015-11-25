package br.com.agreganormas.poc.getdatafromhtml;

import java.util.List;

/**
 * Interface que define métodos comuns para extrair dados das páginas html.
 * 
 * @author elizeu
 *
 */
public interface ExtractDataHtml {

	/**
	 * Método usado para extrair as leis estaduais.
	 * 
	 * @return lista de leis estaduais
	 */
	List<StateLaw> extractLaws();

}
