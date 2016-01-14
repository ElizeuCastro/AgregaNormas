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

import java.util.HashMap;
import java.util.List;

import br.com.bibliotecaedt.enumerado.EsferaEnum;
import br.com.bibliotecaedt.enumerado.EstadoEnum;
import br.com.bibliotecaedt.enumerado.TipoDeNormaEnum;
import br.com.bibliotecaedt.modelo.Norma;
import br.com.bibliotecaedt.persistencia.NormaDao;

/**
 * Classe responsável pelas regras e operações de normas
 *
 */
public class ControleNorma {

    /**
     * Instância de {@link NormaDao}
     */
    private final NormaDao normaDao;

    /**
     * Construtor
     */
    public ControleNorma() {
	normaDao = NormaDao.getInstancia();
    }

    /**
     * Salva normas.
     * 
     * @param normas
     *            {@link List} de normas.
     * @param estadoEnum
     *            enumerado que indica o estado da federação
     * @param esferaEnum
     *            enumerado que indica o tipo de esfera politica.
     */
    public void salvarNormas(
	    final HashMap<TipoDeNormaEnum, List<Norma>> normas,
	    final EstadoEnum estadoEnum, final EsferaEnum esferaEnum) {
	normaDao.salvar(normas, estadoEnum, esferaEnum);
    }

    /**
     * @param federal
     * @param tipo
     * @param numero
     * @param inicio
     * @param limite
     */
    public List<Norma> buscarNormas(final EsferaEnum esferaEnum,
	    final Integer tipo, final String numero, Integer limite,
	    Integer inicio) {
	return normaDao.buscarNormas(esferaEnum, tipo, numero, limite, inicio);
    }

    /**
     * @param federal
     * @param tipo
     * @param numero
     * @return
     */
    public int total(EsferaEnum esferaEnum, Integer tipo, String numero) {
	return normaDao.total(esferaEnum, tipo, numero);
    }

    /**
     * @param federal
     * @return
     */
    public List<Norma> buscarAnos(EsferaEnum esferaEnum) {
	return normaDao.buscarAnos(esferaEnum);
    }

}
