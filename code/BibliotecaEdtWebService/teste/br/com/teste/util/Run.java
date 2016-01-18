package br.com.teste.util;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Run {

	public static void main(String[] args) {
		try {
			Document document = Jsoup.connect("http://sisam.cptec.inpe.br/sisam_webservice/services/CidadesWebService?wsdl").get();
			System.out.println(document.childNodeSize());
			System.out.println(document.baseUri());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
