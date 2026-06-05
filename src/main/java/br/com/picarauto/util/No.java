package br.com.picarauto.util;

import br.com.picarauto.model.OrdemServicoModel;

/**
 *
 * @author Caio4breu
 */
public class No {
    OrdemServicoModel dado;
    No proximo;
    
    No(OrdemServicoModel dado) {
        this.dado = dado;
        this.proximo = null;
    }
}
