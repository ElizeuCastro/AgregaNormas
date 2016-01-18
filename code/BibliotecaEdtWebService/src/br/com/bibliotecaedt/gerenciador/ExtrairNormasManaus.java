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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ExtrairNormasManaus implements ExtrairNormas {

    @Override
    public void extrairNormas() {
	try {
	    Document document = Jsoup
		    .connect(
			    "http://www.cmm.am.gov.br/tipo-leis-projetos/ementario/leis-sancionadas/?ano=2015")
		    .userAgent("Mozilla").timeout(60 * 1000).get();
	    final Elements table = document.select("table");
	    for (final Element row : table.get(0).select("tr")) {
		System.out.println(row);
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}

    }
}
