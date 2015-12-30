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

public class ExtrairNormasAmazonas implements ExtrairNormas {

	private static final String BASE_URL = "http://www.sefaz.am.gov.br/Areas/OpcaoSistemas/SILT/Normas/Legisla%C3%A7%C3%A3o%20Estadual/Lei%20Estadual/Lei%20Estadual.htm";
	private static final String CUT_OFF_TEXT_LAW = "ESTE TEXTO NÃO SUBSTITUI O PUBLICADO NO DIÁRIO OFICIAL";
	private ControleNorma controle = new ControleNorma();

	@Override
	public void extrairLeis() {
		try {
			final List<Norma> normas = new ArrayList<>();
			System.out.println("Amazonas - Leis - Início");
			System.out.println("Amazonas - Leis - URL - " + BASE_URL);
			Document document = Jsoup.connect(BASE_URL).get();
			Elements elements = document.select("a[href]");
			for (Element element : elements) {
				if (eElementoDeLei(element)) {
					final String linkLei = element.attr("abs:href");
					final Document docLei = Jsoup.connect(linkLei).get();
					final Elements table = docLei.select("table");
					for (Element row : table.get(1).select("tr")) {
						Elements tds = row.select("td");
						if (tds.size() == 3
								&& naoECabecalhoDaTabela(tds.get(0),
										tds.get(1), tds.get(2))) {
							final Norma leiEstadual = new Norma();
							leiEstadual.setAno(element.text());
							leiEstadual.setNumero(tds.get(0).text());
							final String linkLeiDetalhe = recuperaLinkDeDetalheDaLei(tds
									.get(0));
							leiEstadual.setDataPublicacao(tds.get(1).text());
							leiEstadual.setResumo(tds.get(2).text());
							leiEstadual
									.setDescricao(recuperaTextoDaLei(linkLeiDetalhe));
							normas.add(leiEstadual);
						}
					}
				}
			}
			controle.salvarNormas(normas, EstadoEnum.AMAZONAS,
					EsferaEnum.ESTADUAL, TipoDeNormaEnum.LEI);
			System.out.println(getClass().getCanonicalName() + " - Leis - Fim");
		} catch (IOException e) {
			System.out.println(getClass().getCanonicalName()
					+ " - Leis - Erro => " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Verifica se elemento é um link de uma lei.
	 * 
	 * @param element
	 * @return
	 */
	private boolean eElementoDeLei(final Element element) {
		return !element.attr("abs:href").equalsIgnoreCase(BASE_URL)
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
	private String recuperaLinkDeDetalheDaLei(final Element element) {
		return element != null ? element.select("a[href]").attr("abs:href")
				: "";
	}

	/**
	 * Extrai o texto de detalhe de lei.
	 * 
	 * @param url
	 * @return
	 */
	private String recuperaTextoDaLei(final String url) {
		final StringBuilder builder = new StringBuilder();
		try {
			System.out.println("# Url do texto da lei " + url);
			final Document document = Jsoup.connect(url).get();
			for (Element element : document.select("body p")) {
				builder.append(element.text()).append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (builder.length() > 0) {
			int indexOf = builder.indexOf(CUT_OFF_TEXT_LAW);
			builder.delete(0, indexOf + CUT_OFF_TEXT_LAW.length());
		}

		return builder.length() > 0 ? builder.toString() : "";
	}

	@Override
	public void extrairDecretos() {
	}

}
