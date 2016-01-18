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
import br.com.bibliotecaedt.modelo.Estado;
import br.com.bibliotecaedt.modelo.Norma;
import br.com.bibliotecaedt.persistencia.EstadoDao;
import br.com.bibliotecaedt.persistencia.NormaDao;

/**
 * Classe responsável pelas regras e operações de normas
 *
 */
public class ControleNorma {

    /**
     * Instância de {@link NormaDao}.
     */
    private final NormaDao normaDao;

    /**
     * Instância de {@link EstadoDao}.
     */
    private final EstadoDao estadoDao;

    /**
     * Construtor
     */
    public ControleNorma() {
	normaDao = NormaDao.getInstancia();
	estadoDao = EstadoDao.getInstancia();
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
     * Busca norma federais
     * 
     * @param federal
     * @param tipoDeNorma
     * @param numero
     * @param inicio
     * @param limite
     * @param estado 
     */
    public List<Norma> buscarNormas(final EsferaEnum esferaEnum,
	    final TipoDeNormaEnum tipoDeNorma, final String numero,
	    final Integer limite, final Integer inicio, final Integer estado) {
	return normaDao.buscarNormas(esferaEnum, tipoDeNorma, numero, limite,
		inicio, estado);
    }

    /**
     * Busca quantidade total de normas
     * 
     * @param federal
     * @param tipoDeNorma
     * @param numero
     * @param estado 
     * @return
     */
    public int total(final EsferaEnum esferaEnum,
	    final TipoDeNormaEnum tipoDeNorma, final String numero, final Integer estado) {
	return normaDao.total(esferaEnum, tipoDeNorma, numero, estado);
    }

    /**
     * Busca todos os anos que possuem normas cadastradas
     * 
     * @param estado
     * 
     * @param federal
     * @return
     */
    public List<Norma> buscarAnos(final EsferaEnum esferaEnum,
	    final Integer estado) {
	return normaDao.buscarAnos(esferaEnum, estado);
    }

    /**
     * Busca normas de um determinado ano
     * 
     * @param federal
     * @param ano
     * @param estado
     * @return
     */
    public List<Norma> buscarPorAno(final EsferaEnum esferaEnum,
	    final String ano, final Integer estado) {
	return normaDao.buscarPorAno(esferaEnum, ano, estado);
    }

    /**
     * Lista todos os estados que possuem normas cadastradas.
     * 
     * @return
     */
    public List<Estado> buscarEstados() {
	return estadoDao.buscarEstados();
    }

}
