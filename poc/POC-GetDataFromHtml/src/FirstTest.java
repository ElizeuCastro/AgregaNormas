import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class FirstTest {
									
	static final String BASE_URL = "http://www.sefaz.am.gov.br/Areas/OpcaoSistemas/SILT/Normas/Legisla%C3%A7%C3%A3o%20Estadual/Lei%20Estadual/Lei%20Estadual.htm";

	public static void main(String[] args) {
		try {
			Document document = Jsoup.connect(BASE_URL).get();
			System.out.println(document.baseUri());
			Elements elements = document.select("a[href]");
			for (Element element : elements){
				if (!element.attr("abs:href").equals(BASE_URL) && element.attr("abs:href").contains(element.text())){
					System.out.println(BASE_URL);
					System.out.println(element.attr("abs:href"));
				}
				final LeiEstadual leiEstadual = new LeiEstadual();
				leiEstadual.setAno(element.text());
				leiEstadual.setLinkLei(element.attr("abs:href"));
//				final Document docLei = Jsoup.connect(leiEstadual.getLinkLei()).get();
//				final Elements elementsLei = docLei.select("class=[MsoNormalTable]");
//				for (Element elementLei : elementsLei){
//					elementLei.select("a[href]");
//				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
