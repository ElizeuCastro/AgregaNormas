package br.com.agreganormas.poc.getdatafromhtml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Classe que extrai as leis estaduais do estado do amazonas.
 * 
 * @author elizeu
 *
 */
public class ExtractStateLawAm implements ExtractDataHtml {

	/**
	 * Link base das leis estaduais do amazonas
	 */
	private static final String BASE_URL = "http://www.sefaz.am.gov.br/Areas/OpcaoSistemas/SILT/Normas/Legisla%C3%A7%C3%A3o%20Estadual/Lei%20Estadual/Lei%20Estadual.htm";
	
	private static final String CUT_OFF_TEXT_LAW = "ESTE TEXTO NÃO SUBSTITUI O PUBLICADO NO DIÁRIO OFICIAL";

	@Override
	public List<StateLaw> extractLaws() {
		final List<StateLaw> laws = new ArrayList<>();
		try {
			Document document = Jsoup.connect(BASE_URL).get();
			Elements elements = document.select("a[href]");
			for (Element element : elements) {
				if (isLawElement(element)) {
					final StateLaw leiEstadual = new StateLaw();
					leiEstadual.setAno(element.text());
					leiEstadual.setLinkLei(element.attr("abs:href"));

					// extrai as ementas
					final Document docLei = Jsoup.connect(
							leiEstadual.getLinkLei()).get();
					final Elements table = docLei.select("table");

					for (Element row : table.get(1).select("tr")) {
						Elements tds = row.select("td");
						if (tds.size() == 3
								&& isNotHeaderTable(tds.get(0), tds.get(1),
										tds.get(2))) {
							leiEstadual.setNumero(tds.get(0).text());
							leiEstadual.setLinkLeiDetalhe(retriveLinkDetail(tds
									.get(0)));
							leiEstadual.setData(tds.get(1).text());
							leiEstadual.setEmenta(tds.get(2).text());
						}
					}

					// extrai o texto da lei
					leiEstadual.setDescricaoLei(
							extratctTextLawDetail(leiEstadual.getLinkLeiDetalhe())
					);
					laws.add(leiEstadual);
				}
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return laws;
	}

	/**
	 * Verifica se elemento é um link de uma lei.
	 * 
	 * @param element
	 * @return
	 */
	private boolean isLawElement(final Element element) {
		return !element.attr("abs:href").equalsIgnoreCase(BASE_URL)
				&& element.attr("abs:href").contains(element.text());
	}

	/**
	 * Verifica se o elemento não é o cabeçalho da tabela de ementas.
	 * 
	 * @param elements
	 * @return
	 */
	private boolean isNotHeaderTable(final Element... elements) {
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
	private String retriveLinkDetail(final Element element) {
		return element != null ? element.select("a[href]").attr("abs:href")
				: "";
	}

	/**
	 * Extrai o texto de detalhe de lei.
	 * 
	 * @param linkLawDetail
	 * @return
	 */
	private String extratctTextLawDetail(final String linkLawDetail) {
		final StringBuilder builder = new StringBuilder();
		try {
			final Document document = Jsoup.connect(linkLawDetail).get();
			for (Element element : document.select("body p")) {
				builder.append(element.text()).append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (builder.length() > 0){
			int indexOf = builder.indexOf(CUT_OFF_TEXT_LAW);
			builder.delete(0, indexOf + CUT_OFF_TEXT_LAW.length());
		}
		
		return builder.length() > 0 ? builder.toString() : "";
	}
}
