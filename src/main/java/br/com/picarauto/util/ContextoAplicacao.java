package br.com.picarauto.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Utilitário de acesso ao contexto da aplicação.
 *
 * Padrão de Projeto: Singleton
 * Garante que apenas uma instância do contexto Spring seja acessível
 * em toda a aplicação, centralizando o ponto de acesso a qualquer bean
 * gerenciado — services, repositories, validators — sem necessidade de
 * injeção de dependência em classes fora do ciclo Spring (ex: views Swing).
 *
 * O Spring chama setContexto() uma única vez na inicialização, populando
 * o atributo estático privado. A partir daí, qualquer classe do sistema
 * pode obter um bean via ContextoAplicacao.getBean().
 *
 * @author Cassiano
 */
@Component
public class ContextoAplicacao implements ApplicationContextAware {

    // Instância estática — ponto único de acesso, igual ao ConexaoBanco anterior
    private static ApplicationContext instancia = null;

    // Construtor privado — ninguém cria ContextoAplicacao com new
    private ContextoAplicacao() {}

    /**
     * Chamado automaticamente pelo Spring uma única vez na inicialização.
     * Popula a instância estática com o contexto ativo.
     */
    @Override
    public void setApplicationContext(ApplicationContext contexto) throws BeansException {
        instancia = contexto;
    }

    /**
     * Retorna o contexto Spring ativo.
     * Lança IllegalStateException se chamado antes da inicialização do Spring.
     */
    public static ApplicationContext getInstancia() {
        if (instancia == null) {
            throw new IllegalStateException(
                "Contexto da aplicação ainda não foi inicializado pelo Spring."
            );
        }
        return instancia;
    }

    /**
     * Atalho para obter qualquer bean gerenciado pelo Spring.
     *
     * Uso: ContextoAplicacao.getBean(ClienteService.class)
     *
     * @param tipo Classe do bean desejado
     * @return Instância gerenciada pelo Spring
     */
    public static <T> T getBean(Class<T> tipo) {
        return getInstancia().getBean(tipo);
    }
}
