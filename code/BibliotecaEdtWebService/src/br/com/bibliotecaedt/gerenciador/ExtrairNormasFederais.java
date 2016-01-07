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
import java.util.HashMap;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import br.com.bibliotecaedt.controle.ControleNorma;
import br.com.bibliotecaedt.enumerado.EsferaEnum;
import br.com.bibliotecaedt.enumerado.TipoDeNormaEnum;
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
     * Url de leis federais.
     */
    private static final String LEIS_FEDERAIS = "http://www4.planalto.gov.br/legislacao/legislacao-1/leis-ordinarias#content";

    /**
     * URL base de decreto válida.
     */
    private static final String DECRETO_URL_BASE_VALIDA = "http://www4.planalto.gov.br/legislacao/legislacao-1/decretos1/";

    /**
     * URL base de lei válida.
     */
    private static final String LEI_URL_BASE_VALIDA = "http://www4.planalto.gov.br/legislacao/legislacao-1/leis-ordinarias/";

    /**
     * Primeiro ano para extração de decretos.
     */
    private static final int DECRETO_ANO_INICIAL = 1980;

    /**
     * Primeiro ano para extração de leis.
     */
    private static final int LEI_ANO_INICIAL = 1988;

    /**
     * Instância de {@link ControleNorma}.
     */
    private final ControleNorma controle = new ControleNorma();

    @Override
    public void extrairNormas() {
	final HashMap<TipoDeNormaEnum, List<Norma>> normas = new HashMap<TipoDeNormaEnum, List<Norma>>();

	final List<Norma> leis = leis();
	if (leis != null && !leis.isEmpty()) {
	    normas.put(TipoDeNormaEnum.LEI, leis);
	}

	final List<Norma> decretos = decretos();
	if (decretos != null && !decretos.isEmpty()) {
	    normas.put(TipoDeNormaEnum.DECRETO, decretos);
	}

	System.out.println(getClass().getCanonicalName()
		+ " - db- salvar - iniciado");

	controle.salvarNormas(normas, null, EsferaEnum.FEDERAL);

	System.out.println(getClass().getCanonicalName()
		+ " - db- salvar - finalizado");

	leis();
	decretos();
    }

    /**
     * Busca leis federais.
     * 
     * @return
     */
    private List<Norma> leis() {
	System.out.println(getClass().getCanonicalName()
		+ " Extrair leis - início");
	final List<Norma> leis = new ArrayList<Norma>();
	try {
	    leis.addAll(extrairNormas(LEIS_FEDERAIS, LEI_URL_BASE_VALIDA,
		    LEI_ANO_INICIAL));
	} catch (final IOException e) {
	    System.out.println(getClass().getCanonicalName()
		    + " Erro ao extrair leis => " + e.getMessage());
	}
	System.out.println(getClass().getCanonicalName()
		+ " Extrair leis - fim");
	return leis;
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
	    decretos.addAll(extrairNormas(DECRETOS_FEDERAIS,
		    DECRETO_URL_BASE_VALIDA, DECRETO_ANO_INICIAL));
	} catch (final IOException e) {
	    System.out.println(getClass().getCanonicalName()
		    + " Erro ao extrair decretos => " + e.getMessage());
	}
	System.out.println(getClass().getCanonicalName()
		+ " Extrair decretos - fim");
	return decretos;
    }

    private List<Norma> extrairNormas(final String linkNormas,
	    final String urlBaseValida, final int anoInicial)
	    throws IOException {
	final List<Norma> decretos = new ArrayList<Norma>();
	final Document document = Jsoup.connect(linkNormas).timeout(TIMEOUT)
		.get();
	final Elements elements = document.select("a[href]");
	for (final Element element : elements) {
	    if (eUrlValidaDeNorma(element, urlBaseValida, anoInicial)) {
		final String linkDecreto = element.attr("abs:href");
		final Document ementas = Jsoup.connect(linkDecreto)
			.timeout(TIMEOUT).get();
		final Elements table = ementas.select("table");
		for (final Element row : table.get(1).select("tr")) {
		    final Elements tds = row.select("td");
		    if (tds.size() >= 2
			    && naoECabecalhoDaTabela(tds.get(0), tds.get(1))) {
			final Norma decreto = new Norma();
			decreto.setNumero(numeroDaNorma(tds.get(0)));
			decreto.setDataPublicacao(anoDaNorma(tds.get(0)));
			decreto.setAno(element.text());
			decreto.setResumo(tds.get(1).text());
			decreto.setDescricao(urlDeDetalhe(tds.get(0)));
			decretos.add(decreto);
		    }
		}
	    }
	}
	return decretos;
    }

    /**
     * Verifica se é um url válida de lei ou decreto.
     * 
     * @param element
     * @return
     */
    private boolean eUrlValidaDeNorma(final Element element,
	    final String urlBase, final int primeiroAno) {
	final int anoAtual = Calendar.getInstance().get(Calendar.YEAR);
	int anoInicial = primeiroAno;
	final String urlCanditada = element.attr("abs:href");
	if (urlCanditada.contains(urlBase)) {
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
	    if (td.text().equals("Decreto")
		    || td.text().equalsIgnoreCase("Nº da Lei")
		    || td.text().equals("Ementa")) {
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
		return texto.substring(0, index1).replaceAll("\\s", "");
	    } else if (index2 > -1) {
		return texto.substring(0, index2).replaceAll("\\s", "");
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
			.substring(index1 + regex1.length(), texto.length())
			.replaceAll("\\s", "");
	    } else if (index2 > -1) {
		return texto
			.substring(index2 + regex2.length(), texto.length())
			.replaceAll("\\s", "");
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
		return url.replaceAll("\\s", "");
	    }
	}
	return "";
    }

}
