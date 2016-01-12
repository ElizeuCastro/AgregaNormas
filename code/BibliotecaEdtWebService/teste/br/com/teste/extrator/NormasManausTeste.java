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
package br.com.teste.extrator;

import br.com.bibliotecaedt.gerenciador.ExtrairNormas;
import br.com.bibliotecaedt.gerenciador.ExtrairNormasManaus;

/**
 * @author elizeu
 *
 */
public class NormasManausTeste {
    
    public static void main(String[] args) {
	ExtrairNormas extrairNormas = new ExtrairNormasManaus();
	extrairNormas.extrairNormas();
    }

}
