package br.com.teste.extrator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import br.com.bibliotecaedt.modelo.Norma;

public class DecretosAmazonas {

    public static void main(String[] args) {
	decretos();
    }

    private static final String LINK_DECRETOS_ESTADUAIS = "http://www.sefaz.am.gov.br/Areas/OpcaoSistemas/SILT/Normas/Legisla%C3%A7%C3%A3o%20Estadual/Decreto%20Estadual/Decreto%20Estadual.htm";
    private static final String NAO_E_LEI = "ESTE TEXTO NÃO SUBSTITUI O PUBLICADO NO DIÁRIO OFICIAL";

    private static List<Norma> decretos() {
	final List<Norma> decretos = new ArrayList<>();
	try {
	    Document document = Jsoup.connect(LINK_DECRETOS_ESTADUAIS).get();
	    Elements elements = document.select("a[href]");
	    for (Element element : elements) {
		if (eElementoDeLei(element)) {
		    final String linkDecreto = element.attr("abs:href");
		    System.out.println("Decreto: \n" +linkDecreto);
		    final Document docLei = Jsoup.connect(linkDecreto).get();
		    final Elements table = docLei.select("table");
		    for (Element row : table.get(1).select("tr")) {
			Elements tds = row.select("td");
			if (tds.size() == 3 && naoECabecalhoDaTabela(tds.get(0),tds.get(1), tds.get(2))) {
			    final Norma decreto = new Norma();
			    decreto.setAno(element.text());
			    decreto.setNumero(tds.get(0).text());
			    final String linkDetalheDecreto = recuperaLinkDeDetalheDaLei(tds.get(0));
			    System.out.println("Detalhe: \n" + linkDetalheDecreto);
			    decreto.setDataPublicacao(tds.get(1).text());
			    decreto.setResumo(tds.get(2).text());
			    decreto.setDescricao(recuperaTextoDaLei(linkDetalheDecreto));
			    decretos.add(decreto);
			}
		    }
		}
	    }
	} catch (IOException e) {
	    System.out.println("Error: " + e.getMessage());
	    e.printStackTrace();
	}
	return decretos;
    }

    /**
     * Verifica se elemento é um link de uma lei.
     * 
     * @param element
     * @return
     */
    private static boolean eElementoDeLei(final Element element) {
	return !element.attr("abs:href").equalsIgnoreCase(
		LINK_DECRETOS_ESTADUAIS)
		&& element.attr("abs:href").contains(element.text());
    }

    /**
     * Verifica se o elemento não é o cabeçalho da tabela de ementas.
     * 
     * @param elements
     * @return
     */
    private static boolean naoECabecalhoDaTabela(final Element... elements) {
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
    private static String recuperaLinkDeDetalheDaLei(final Element element) {
	return element != null ? element.select("a[href]").attr("abs:href")
		: "";
    }

    /**
     * Extrai o texto de detalhe de lei.
     * 
     * @param url
     * @return
     */
    private static String recuperaTextoDaLei(final String url) {
	final StringBuilder builder = new StringBuilder();
	try {
	    final Document document = Jsoup.connect(url).get();
	    for (Element element : document.select("body p")) {
		builder.append(element.text()).append("\n");
	    }
	    if (builder.length() > 0) {
		int indexOf = builder.indexOf(NAO_E_LEI);
		builder.delete(0, indexOf + NAO_E_LEI.length());
	    }
	} catch (IOException e) {
	    System.out.println("Error Texto da Lei: " + e.getMessage());
	    e.printStackTrace();
	}
	return builder.length() > 0 ? builder.toString() : "";
    }

}
