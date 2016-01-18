package br.com.agreganormas.poc.getdatafromhtml;

import java.util.ArrayList;
import java.util.List;

public class ExtractorManager {

	public static void main(String[] args) {

		final List<ExtractDataHtml> extractors = new ArrayList<ExtractDataHtml>();
		extractors.add(new ExtractStateLawAm());

		for (ExtractDataHtml extractor : extractors) {
			System.out.println(extractor.extractLaws());
		}

	}

}
