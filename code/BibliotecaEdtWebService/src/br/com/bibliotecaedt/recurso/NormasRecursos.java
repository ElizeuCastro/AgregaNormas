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
    public Response getAnos() {
	final List<Norma> normas = controle.buscarAnos(EsferaEnum.FEDERAL);
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
		    EsferaEnum.FEDERAL, ano);
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

	final int quantidade = controle.total(EsferaEnum.FEDERAL, tipoDeNorma, numero);

	if (limite == null || limite > MAX_LIMITE) {
	    limite = MAX_LIMITE;
	}

	if (inicio == null || inicio < 0) {
	    inicio = 0;
	}

	final List<Norma> normas = controle.buscarNormas(EsferaEnum.FEDERAL,
		tipoDeNorma, numero, limite, inicio);

	final RespostaNorma respostaNorma = new RespostaNorma(quantidade,
		normas);

	return Response.ok(gson.toJson(respostaNorma)).build();

    }
}
