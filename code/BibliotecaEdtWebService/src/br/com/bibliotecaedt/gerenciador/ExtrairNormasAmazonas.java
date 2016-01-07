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
import java.util.HashMap;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import br.com.bibliotecaedt.controle.ControleNorma;
import br.com.bibliotecaedt.enumerado.EsferaEnum;
import br.com.bibliotecaedt.enumerado.EstadoEnum;
import br.com.bibliotecaedt.enumerado.TipoDeNormaEnum;
import br.com.bibliotecaedt.modelo.Norma;

/**
 * Classe que contém as regras de extração de leis e decretos do estado do
 * Amazonas.
 *
 */
public class ExtrairNormasAmazonas implements ExtrairNormas {
    
    /**
     * Time out.
     */
    private static final int TIMEOUT = 60 * 1000;

    /**
     * URL inicial que contém as leis estaduais do Amazonas
     */
    private static final String LINK_LEIS_ESTADUAIS = "http://www.sefaz.am.gov.br/Areas/OpcaoSistemas/SILT/Normas/Legisla%C3%A7%C3%A3o%20Estadual/Lei%20Estadual/Lei%20Estadual.htm";

    /**
     * URL inicial que contém os decretos estaduais do Amazonas
     */
    private static final String LINK_DECRETOS_ESTADUAIS = "http://www.sefaz.am.gov.br/Areas/OpcaoSistemas/SILT/Normas/Legisla%C3%A7%C3%A3o%20Estadual/Decreto%20Estadual/Decreto%20Estadual.htm";

    /**
     * Instância de {@link ControleNorma}.
     */
    private final ControleNorma controle = new ControleNorma();

    /**
     * {@inheritDoc}
     */
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

	controle.salvarNormas(normas, EstadoEnum.AMAZONAS, EsferaEnum.ESTADUAL);

	System.out.println(getClass().getCanonicalName()
		+ " - db- salvar - finalizado");
    }

    /**
     * Busca leis.
     * 
     * @return {@link List} de leis
     */
    private List<Norma> leis() {
	List<Norma> leis = new ArrayList<Norma>();
	try {
	    System.out.println(getClass().getCanonicalName()
		    + " Extrair Leis Início");
	    leis = extrairNormas(LINK_LEIS_ESTADUAIS);
	    System.out.println(getClass().getCanonicalName()
		    + " Extrair Leis Fim");
	} catch (final IOException e) {
	    System.out.println(getClass().getCanonicalName()
		    + " Erro ao extrair leis => " + e.getMessage());
	}
	return leis;
    }

    /**
     * Busca decretos.
     * 
     * @return {@link List} de decretos.
     */
    private List<Norma> decretos() {
	List<Norma> decretos = new ArrayList<Norma>();
	try {
	    System.out.println(getClass().getCanonicalName()
		    + " Extrair Decretos Início");
	    decretos = extrairNormas(LINK_DECRETOS_ESTADUAIS);
	    System.out.println(getClass().getCanonicalName()
		    + " Extrair Leis Fim");
	} catch (final IOException e) {
	    System.out.println(getClass().getCanonicalName()
		    + " Erro ao extrair leis => " + e.getMessage());
	}
	return decretos;
    }

    /**
     * Busca normas (Leis ou Decretos).
     * 
     * @param linkNormas
     *            url da norma.
     * @return {@link List} de normas.
     * @throws IOException
     */
    private List<Norma> extrairNormas(final String linkNormas)
	    throws IOException {
	final List<Norma> normas = new ArrayList<>();
	final Document document = Jsoup.connect(linkNormas).timeout(TIMEOUT).get();
	final Elements elements = document.select("a[href]");
	for (final Element element : elements) {
	    if (eElementoDeNorma(element, linkNormas)) {
		final String linkLei = element.attr("abs:href");
		final Document docLei = Jsoup.connect(linkLei).timeout(TIMEOUT).get();
		final Elements table = docLei.select("table");
		for (final Element row : table.get(1).select("tr")) {
		    final Elements tds = row.select("td");
		    if (tds.size() == 3
			    && naoECabecalhoDaTabela(tds.get(0), tds.get(1),
				    tds.get(2))) {
			final Norma norma = new Norma();
			norma.setAno(element.text().replaceAll("\\s", ""));
			norma.setNumero(tds.get(0).text().replaceAll("\\s", ""));
			final String linkLeiDetalhe = recuperaLinkDeDetalheDaNorma(tds
				.get(0));
			norma.setDataPublicacao(tds.get(1).text()
				.replaceAll("\\s", ""));
			norma.setResumo(tds.get(2).text());
			norma.setDescricao(linkLeiDetalhe);
			normas.add(norma);
		    }
		}
	    }
	}
	return normas;
    }

    /**
     * Verifica se elemento é um link de uma norma.
     * 
     * @param element
     *            elemento DOM
     * @param linkNormas
     *            url da norma correspodente.
     * @return true se for um link válido de norma,do contrário retorna false.
     */
    private boolean eElementoDeNorma(final Element element,
	    final String linkNormas) {
	return !element.attr("abs:href").equalsIgnoreCase(linkNormas)
		&& element.attr("abs:href").contains(element.text());
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
	    if (td.text().equals("NR") || td.text().equals("DATA")
		    || td.text().equals("EMENTA")) {
		notHeader = false;
		break;
	    }
	}
	return notHeader;
    }

    /**
     * Pega o link de detalhe da leis
     * 
     * @param element
     * @return
     */
    private String recuperaLinkDeDetalheDaNorma(final Element element) {
	return element != null ? element.select("a[href]").attr("abs:href")
		: "";
    }

}
