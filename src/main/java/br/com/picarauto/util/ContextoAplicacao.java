package br.com.picarauto.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Utilitário de acesso ao contexto da aplicação.
 *
 * Padrão de Projeto: Singleton
 * Garante que apenas uma instância de ContextoAplicacao exista em toda a
 * aplicação, centralizando o ponto de acesso a qualquer bean gerenciado
 * pelo Spring — services, repositories, validators — sem necessidade de
 * injeção de dependência em classes fora do ciclo Spring (ex: views Swing).
 *
 * Os três elementos do padrão estão presentes:
 *   1. Atributo estático privado (instancia) — controla a instância da classe
 *   2. Construtor privado — ninguém instancia com new de fora
 *   3. Método estático getInstancia() — único ponto de acesso, aciona o new
 *
 * O Spring chama setApplicationContext() uma única vez na inicialização,
 * populando o atributo contextoSpring. A partir daí, qualquer classe pode
 * chamar ContextoAplicacao.getBean(ClienteService.class) para obter um bean.
 *
 * @author Cassiano
 */
@Component
public class ContextoAplicacao implements ApplicationContextAware {

    // 1. Atributo estático privado — controla a instância desta classe (Singleton)
    private static ContextoAplicacao instancia;

    // Contexto do Spring armazenado separadamente (populado via setApplicationContext)
    private static ApplicationContext contextoSpring;

    // 2. Construtor privado — ninguém cria ContextoAplicacao com new
    private ContextoAplicacao() {}

    /**
     * 3. Método estático que controla o new — ponto único de acesso.
     * Cria a instância na primeira chamada (lazy initialization).
     */
    public static ContextoAplicacao getInstancia() {
        if (instancia == null) {
            instancia = new ContextoAplicacao();
        }
        return instancia;
    }

    /**
     * Chamado automaticamente pelo Spring uma única vez na inicialização.
     * Popula o contexto Spring para uso posterior via getBean().
     */
    @Override
    public void setApplicationContext(ApplicationContext contexto) throws BeansException {
        contextoSpring = contexto;
    }

    /**
     * Atalho para obter qualquer bean gerenciado pelo Spring.
     * Lança IllegalStateException se chamado antes da inicialização do Spring.
     *
     * Uso: ContextoAplicacao.getBean(ClienteService.class)
     *
     * @param tipo Classe do bean desejado
     * @return Instância gerenciada pelo Spring
     */
    public static <T> T getBean(Class<T> tipo) {
        if (contextoSpring == null) {
            throw new IllegalStateException(
                "Contexto da aplicação ainda não foi inicializado pelo Spring."
            );
        }
        return contextoSpring.getBean(tipo);
    }
}