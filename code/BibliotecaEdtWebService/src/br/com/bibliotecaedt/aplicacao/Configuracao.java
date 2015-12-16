package br.com.bibliotecaedt.aplicacao;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import br.com.bibliotecaedt.gerenciador.agedador.Agendador;

public class Configuracao implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent event) {
		Agendador.getInstancia().inicializa();
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {

	}

}
