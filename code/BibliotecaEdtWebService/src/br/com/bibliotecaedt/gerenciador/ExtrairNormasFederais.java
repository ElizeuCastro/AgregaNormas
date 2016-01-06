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
package br.com.bibliotecaedt.gerenciador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import br.com.bibliotecaedt.modelo.Norma;

/**
 * 
 * Classe que contém as regras de extração de leis e decretos federais.
 */
public class ExtrairNormasFederais implements ExtrairNormas {

    /**
     * Time out.
     */
    private static final int TIMEOUT = 60 * 1000;

    /**
     * Url de decretos federais.
     */
    private static final String DECRETOS_FEDERAIS = "http://www4.planalto.gov.br/legislacao/legislacao-1/decretos1#content";

    /**
     * Usado verificar se um url corresponde a uma url de norma válida.
     */
    private static final String DECRETO_URL_BASE_VALIDA = "http://www4.planalto.gov.br/legislacao/legislacao-1/decretos1/";

    @Override
    public void extrairNormas() {
	decretos().toString();
    }

    /**
     * Busca decretos federais.
     * 
     * @return
     */
    private List<Norma> decretos() {
	System.out.println(getClass().getCanonicalName()
		+ " Extrair decretos - início");
	final List<Norma> decretos = new ArrayList<Norma>();
	try {
	    final Document document = Jsoup.connect(DECRETOS_FEDERAIS)
		    .timeout(TIMEOUT).get();
	    final Elements elements = document.select("a[href]");
	    for (final Element element : elements) {
		if (eUrlValidaDeDecreto(element)) {
		    final String linkDecreto = element.attr("abs:href");
		    System.out.println(linkDecreto);
		    final Document ementas = Jsoup.connect(linkDecreto)
			    .timeout(TIMEOUT).get();
		    final Elements table = ementas.select("table");
		    for (final Element row : table.get(1).select("tr")) {
			final Elements tds = row.select("td");
			if (tds.size() >= 2
				&& naoECabecalhoDaTabela(tds.get(0), tds.get(1))) {
			    final Norma norma = new Norma();
			    norma.setNumero(numeroDaNorma(tds.get(0)));
			    norma.setDataPublicacao(anoDaNorma(tds.get(0)));
			    norma.setAno(element.text());
			    norma.setResumo(tds.get(1).text());
			    norma.setDescricao(urlDeDetalhe(tds.get(0)));
			    System.out.println(norma.toString());
			    decretos.add(norma);
			}
		    }
		}
	    }
	} catch (final IOException e) {
	    System.out.println(getClass().getCanonicalName()
		    + " Erro ao extrair decretos");
	}
	System.out.println(getClass().getCanonicalName()
		+ " Extrair decretos - fim");
	return decretos;
    }

    /**
     * Verifica se é um url de norma válida.
     * 
     * @param element
     * @return
     */
    private boolean eUrlValidaDeDecreto(final Element element) {
	final int anoAtual = Calendar.getInstance().get(Calendar.YEAR);
	int anoInicial = 1980;
	final String urlCanditada = element.attr("abs:href");
	if (urlCanditada.contains(DECRETO_URL_BASE_VALIDA)) {
	    while (anoInicial <= anoAtual) {
		if (urlCanditada.contains(String.valueOf(anoInicial))) {
		    return true;
		}
		anoInicial++;
	    }
	}
	return false;
    }

    /**
     * Verifica se o elemento não é o cabeçalho da tabela de ementas.
     * 
     * @param elements
     * @return
     */
    private boolean naoECabecalhoDaTabela(final Element... elements) {
	boolean notHeader = true;
	for (final Element td : elements) {
	    if (td.text().equals("Decreto") || td.text().equals("Ementa")) {
		notHeader = false;
		break;
	    }
	}
	return notHeader;
    }

    /**
     * Extrai número da norma.
     * 
     * @param element
     * @return
     */
    private String numeroDaNorma(final Element element) {
	if (element != null && !element.select("a").text().isEmpty()) {
	    final String texto = element.select("a").text();
	    final String regex1 = ", de ";
	    final String regex2 = " de ";
	    final int index1 = texto.indexOf(regex1);
	    final int index2 = texto.indexOf(regex2);
	    if (index1 > -1) {
		return texto.substring(0, index1);
	    } else if (index2 > -1) {
		return texto.substring(0, index2);
	    }
	}
	return "";
    }

    /**
     * Extrai ano da norma.
     * 
     * @param element
     * @return
     */
    private String anoDaNorma(final Element element) {
	if (element != null && !element.select("a").text().isEmpty()) {
	    final String texto = element.select("a").text();
	    final String regex1 = ", de ";
	    final String regex2 = " de ";
	    final int index1 = texto.indexOf(regex1);
	    final int index2 = texto.indexOf(regex2);
	    if (index1 > -1) {
		return texto
			.substring(index1 + regex1.length(), texto.length());
	    } else if (index2 > -1) {
		return texto
			.substring(index2 + regex2.length(), texto.length());
	    }
	}
	return "";
    }

    /**
     * Extrai a url de detalhe da norma.
     * 
     * @param element
     * @return
     */
    private String urlDeDetalhe(final Element element) {
	if (element != null) {
	    final Elements href = element.select("a[href]");
	    if (href != null) {
		final String url = href.attr("abs:href");
		return url;
	    }
	}
	return "";
    }

}
