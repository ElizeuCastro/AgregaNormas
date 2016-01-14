package br.com.bibliotecaedt.recurso;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.com.bibliotecaedt.controle.ControleUsuario;
import br.com.bibliotecaedt.enumerado.ErroDeUsuario;
import br.com.bibliotecaedt.modelo.Usuario;
import br.com.bibliotecaedt.recurso.resposta.RespostaDeErro;
import br.com.bibliotecaedt.utilitario.Util;

import com.google.gson.Gson;

@Path("/usuario")
public class UsuarioRecursos {

    private ControleUsuario controle;
    private Gson gson;

    public UsuarioRecursos() {
	controle = new ControleUsuario();
	gson = new Gson();
    }

    @POST
    @Path("/registrar")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registrar(@FormParam("nome") String nome,
	    @FormParam("email") String email, @FormParam("login") String login,
	    @FormParam("senha") String senha) {
	if (Util.estaVazio(nome, email, login, senha)) {
	    return Response.status(Status.BAD_REQUEST).build();
	}
	if (controle.verificarEmailExiste(email)) {
	    final RespostaDeErro erro = new RespostaDeErro(
		    ErroDeUsuario.EMAIL_DUPLICADO.getErro(),
		    ErroDeUsuario.EMAIL_DUPLICADO.getMensagem());
	    return Response.status(Status.CONFLICT).entity(gson.toJson(erro))
		    .build();
	}
	if (controle.verificarLoginExiste(login)) {
	    final RespostaDeErro erro = new RespostaDeErro(
		    ErroDeUsuario.LOGIN_DUPLICADO.getErro(),
		    ErroDeUsuario.LOGIN_DUPLICADO.getMensagem());
	    return Response.status(Status.CONFLICT).entity(gson.toJson(erro))
		    .build();
	}
	Response response = Response.status(Status.BAD_REQUEST).build();
	final Usuario usuario = controle.salvar(new Usuario(nome, email, login,
		senha));
	if (usuario.estaCadastrado()) {
	    response = Response.ok(gson.toJson(usuario)).build();
	}
	return response;
    }

    @POST
    @Path("/autenticar")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response autenticar(@FormParam("login") String login,
	    @FormParam("senha") String senha) {
	if (Util.estaVazio(login, senha)) {
	    return Response.status(Status.BAD_REQUEST).build();
	}
	Response response = Response.status(Status.BAD_REQUEST).build();
	final Usuario usuario = controle.autenticar(new Usuario(login, senha));
	if (usuario.estaCadastrado()) {
	    response = Response.ok(gson.toJson(usuario)).build();
	} else {
	    final RespostaDeErro erro = new RespostaDeErro(
		    ErroDeUsuario.LOGIN_SENHA_INVALIDO.getErro(),
		    ErroDeUsuario.LOGIN_SENHA_INVALIDO.getMensagem());
	    response = Response.status(Status.CONFLICT)
		    .entity(gson.toJson(erro)).build();
	}
	return response;
    }

}
