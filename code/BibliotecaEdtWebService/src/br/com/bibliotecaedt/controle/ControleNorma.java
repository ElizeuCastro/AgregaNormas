package br.com.bibliotecaedt.controle;

import java.util.List;

import br.com.bibliotecaedt.enumerado.Tipo;
import br.com.bibliotecaedt.modelo.Norma;
import br.com.bibliotecaedt.modelo.TipoDeNorma;
import br.com.bibliotecaedt.persistencia.NormaDao;

public class ControleNorma {

	NormaDao normaDao = new NormaDao();

	public void salvarLeis(final List<Norma> normas) {
		final TipoDeNorma tipoDeNorma = new TipoDeNorma(Tipo.LEI.getId(), Tipo.LEI.getTexto());
		for (Norma norma : normas) {
			norma.setTipoDeNorma(tipoDeNorma);
			normaDao.salvar(norma);
		}
	}
	
	public void salvarDecretos(final List<Norma> normas) {
		final TipoDeNorma tipoDeNorma = new TipoDeNorma(Tipo.DECRETO.getId(), Tipo.DECRETO.getTexto());
		for (Norma norma : normas) {
			norma.setTipoDeNorma(tipoDeNorma);
			normaDao.salvar(norma);
		}
	}

}
