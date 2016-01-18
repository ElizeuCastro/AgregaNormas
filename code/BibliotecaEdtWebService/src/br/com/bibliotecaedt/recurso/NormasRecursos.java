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
package br.com.bibliotecaedt.recurso;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.bibliotecaedt.controle.ControleNorma;
import br.com.bibliotecaedt.enumerado.EsferaEnum;
import br.com.bibliotecaedt.enumerado.TipoDeNormaEnum;
import br.com.bibliotecaedt.modelo.Estado;
import br.com.bibliotecaedt.modelo.Norma;
import br.com.bibliotecaedt.recurso.resposta.RespostaNorma;

import com.google.gson.Gson;

@Path("/normas")
public class NormasRecursos {

    private final ControleNorma controle;
    private final Gson gson;

    private final static int MAX_LIMITE = 50;

    public NormasRecursos() {
	controle = new ControleNorma();
	gson = new Gson();
    }

    /**
     * Lista todos os anos que possuem normas.
     * 
     * @return
     */
    @GET
    @Path("/federais/anos")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNormasFederaisAnos() {
	final List<Norma> normas = controle
		.buscarAnos(EsferaEnum.FEDERAL, null);
	return Response.ok(gson.toJson(normas)).build();
    }

    /**
     * Lista normas de um ano específico
     * 
     * @param ano
     * @return
     */
    @GET
    @Path("/federais/ano")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNormasPorAno(@QueryParam("ano") final String ano) {
	if (ano != null && !ano.isEmpty()) {
	    final List<Norma> normas = controle.buscarPorAno(
		    EsferaEnum.FEDERAL, ano, null);
	    return Response.ok(gson.toJson(normas)).build();
	} else {
	    return Response.ok(gson.toJson(null)).build();
	}

    }

    /**
     * Lista normas
     * 
     * @param tipo
     *            lei ou decreto
     * @param numero
     *            da norma
     * @param limite
     *            máximo de retorna, se não for informa retorna no máximo 50
     *            normas
     * @param inicio
     *            index inicial de filtro
     * @return
     */
    @GET
    @Path("/federais")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNormas(@QueryParam("tipo") final Integer tipo,
	    @QueryParam("numero") final String numero,
	    @QueryParam("limite") Integer limite,
	    @QueryParam("inicio") Integer inicio) {

	final TipoDeNormaEnum tipoDeNorma = TipoDeNormaEnum.getTipoPorId(tipo);

	final int quantidade = controle.total(EsferaEnum.FEDERAL, tipoDeNorma,
		numero, null);

	if (limite == null || limite > MAX_LIMITE) {
	    limite = MAX_LIMITE;
	}

	if (inicio == null || inicio < 0) {
	    inicio = 0;
	}

	final List<Norma> normas = controle.buscarNormas(EsferaEnum.FEDERAL,
		tipoDeNorma, numero, limite, inicio, null);

	final RespostaNorma respostaNorma = new RespostaNorma(quantidade,
		normas);

	return Response.ok(gson.toJson(respostaNorma)).build();

    }

    // Normas Estaduais

    /**
     * Lista todos os anos que possuem normas.
     * 
     * @return
     */
    @GET
    @Path("/estados")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEstados() {
	final List<Estado> estados = controle.buscarEstados();
	return Response.ok(gson.toJson(estados)).build();
    }

    /**
     * Lista todos os anos que possuem normas.
     * 
     * @return
     */
    @GET
    @Path("/estaduais/anos")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNormasEstaduaisAnos(@QueryParam("estado") Integer estado) {
	final List<Norma> normas = controle.buscarAnos(EsferaEnum.ESTADUAL,
		estado);
	return Response.ok(gson.toJson(normas)).build();
    }

    /**
     * Lista normas de um ano específico
     * 
     * @param ano
     * @return
     */
    @GET
    @Path("/estaduais/ano")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNormasEstaduaisPorAno(
	    @QueryParam("estado") final Integer estado,
	    @QueryParam("ano") final String ano) {
	if (ano != null && !ano.isEmpty() && estado != null) {
	    final List<Norma> normas = controle.buscarPorAno(
		    EsferaEnum.ESTADUAL, ano, estado);
	    return Response.ok(gson.toJson(normas)).build();
	} else {
	    return Response.ok(gson.toJson(null)).build();
	}

    }

    /**
     * Lista normas estaduais
     * 
     * @param tipo
     *            lei ou decreto
     * @param numero
     *            da norma
     * @param limite
     *            máximo de retorna, se não for informa retorna no máximo 50
     *            normas
     * @param inicio
     *            index inicial de filtro
     * @return
     */
    @GET
    @Path("/estaduais")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNormasEstaduais(
	    @QueryParam("estado") final Integer estado,
	    @QueryParam("tipo") final Integer tipo,
	    @QueryParam("numero") final String numero,
	    @QueryParam("limite") Integer limite,
	    @QueryParam("inicio") Integer inicio) {

	final TipoDeNormaEnum tipoDeNorma = TipoDeNormaEnum.getTipoPorId(tipo);

	final int quantidade = controle.total(EsferaEnum.ESTADUAL, tipoDeNorma,
		numero, estado);

	if (limite == null || limite > MAX_LIMITE) {
	    limite = MAX_LIMITE;
	}

	if (inicio == null || inicio < 0) {
	    inicio = 0;
	}

	final List<Norma> normas = controle.buscarNormas(EsferaEnum.ESTADUAL,
		tipoDeNorma, numero, limite, inicio, estado);

	final RespostaNorma respostaNorma = new RespostaNorma(quantidade,
		normas);

	return Response.ok(gson.toJson(respostaNorma)).build();

    }
}
