package br.com.agreganormas.poc.getdatafromhtml;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ExtractStateLawAm implements ExtractDataHtml {

	private static final String BASE_URL = "http://www.sefaz.am.gov.br/Areas/OpcaoSistemas/SILT/Normas/Legisla%C3%A7%C3%A3o%20Estadual/Lei%20Estadual/Lei%20Estadual.htm";

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

					// Ementas
					final Document docLei = Jsoup.connect(leiEstadual.getLinkLei()).get();
					final Elements table = docLei.select("table");

					for (Element row : table.get(1).select("tr")) {
						Elements tds = row.select("td");
						if (tds.size() == 3 && isNotHeaderTable(tds.get(0), tds.get(1), tds.get(2))) {
							leiEstadual.setNumero(tds.get(0).text());
							leiEstadual.setLinkLeiDetalhe(retriveLinkDetail(tds.get(0)));
							leiEstadual.setData(tds.get(1).text());
							leiEstadual.setEmenta(tds.get(2).text());
						}
					}
					laws.add(leiEstadual);
				}
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return laws;
	}

	private static boolean isLawElement(final Element element) {
		return !element.attr("abs:href").equalsIgnoreCase(BASE_URL)
				&& element.attr("abs:href").contains(element.text());
	}

	private static boolean isNotHeaderTable(final Element... elements) {
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

	private static String retriveLinkDetail(final Element element) {
		return element != null ? element.select("a[href]").attr("abs:href")
				: "";
	}
}
