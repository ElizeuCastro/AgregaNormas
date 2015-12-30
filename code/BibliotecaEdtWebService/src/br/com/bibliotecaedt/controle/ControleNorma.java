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
package br.com.bibliotecaedt.controle;

import java.util.List;

import br.com.bibliotecaedt.enumerado.EsferaEnum;
import br.com.bibliotecaedt.enumerado.EstadoEnum;
import br.com.bibliotecaedt.enumerado.TipoDeNormaEnum;
import br.com.bibliotecaedt.modelo.Norma;
import br.com.bibliotecaedt.persistencia.NormaDao;

/**
 * Classe responsável pelas regras operações de normas
 *
 */
public class ControleNorma {

	/**
	 * Instância de {@link NormaDao}
	 */
	private NormaDao normaDao;

	/**
	 * Construtor
	 */
	public ControleNorma() {
		normaDao = NormaDao.getInstancia();
	}

	/**
	 * Salvar normas.
	 * 
	 * @param normas
	 * @param estadoEnum
	 * @param esferaEnum
	 * @param tipoDeNormaEnum
	 */
	public void salvarNormas(final List<Norma> normas,
			final EstadoEnum estadoEnum, final EsferaEnum esferaEnum,
			final TipoDeNormaEnum tipoDeNormaEnum) {
		normaDao.salvar(normas, estadoEnum, esferaEnum, tipoDeNormaEnum);
	}

}
