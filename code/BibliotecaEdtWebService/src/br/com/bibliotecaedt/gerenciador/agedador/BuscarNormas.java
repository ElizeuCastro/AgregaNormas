package br.com.bibliotecaedt.gerenciador.agedador;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import br.com.bibliotecaedt.gerenciador.ExtrairNormas;
import br.com.bibliotecaedt.gerenciador.ExtrairNormasAmazonas;
import br.com.bibliotecaedt.gerenciador.ExtrairNormasFederais;
import br.com.bibliotecaedt.gerenciador.ExtrairNormasManaus;

public class BuscarNormas implements Job {

    @Override
    public void execute(JobExecutionContext context)
	    throws JobExecutionException {
	System.out.println("Execution " + new Date());

	final List<ExtrairNormas> extractores = new ArrayList<ExtrairNormas>();
	extractores.add(new ExtrairNormasFederais());
	extractores.add(new ExtrairNormasAmazonas());
	extractores.add(new ExtrairNormasManaus());

	for (ExtrairNormas extrairDadosHtml : extractores) {
	    System.out.println("Inicio da extração de normas " + new Date());
	    extrairDadosHtml.extrairNormas();
	    System.out.println("Fim da extração de normas " + new Date());
	}

    }

}
