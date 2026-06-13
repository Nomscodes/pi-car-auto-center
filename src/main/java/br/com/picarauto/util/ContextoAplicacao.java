package br.com.picarauto.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ContextoAplicacao implements ApplicationContextAware {

    // 1. Atributo estático privado — instância única da classe
    private static ContextoAplicacao instancia;

    // 2. Contexto do Spring armazenado separadamente
    private static ApplicationContext contextoSpring;

    // 3. Construtor público necessário para o Spring instanciar via @Component
    //    mas o controle de instância única é feito pelo próprio Spring (escopo singleton por padrão)
    public ContextoAplicacao() {
        instancia = this;
    }

    // 4. Método estático de acesso à instância
    public static ContextoAplicacao getInstancia() {
        return instancia;
    }

    @Override
    public void setApplicationContext(ApplicationContext contexto) throws BeansException {
        contextoSpring = contexto;
    }

    public static boolean isReady() {
        return contextoSpring != null;
    }

    public static <T> T getBean(Class<T> tipo) {
        if (contextoSpring == null) {
            throw new IllegalStateException(
                "Contexto da aplicação ainda não foi inicializado pelo Spring."
            );
        }
        return contextoSpring.getBean(tipo);
    }
}
