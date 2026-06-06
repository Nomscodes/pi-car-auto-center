package br.com.picarauto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Ponto de entrada da aplicação.
 *
 * @SpringBootApplication ativa:
 *   - @Configuration (configuração da classe)
 *   - @EnableAutoConfiguration (configura JPA, DataSource automaticamente)
 *   - @ComponentScan (escaneia todos os beans do pacote br.com.picarauto)
 *
 * spring.main.headless=false no application.properties é obrigatório
 * para que o Swing consiga abrir janelas gráficas.
 */
@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        // Inicializa o contexto Spring (JPA, repositórios, serviços)
        SpringApplication.run(Main.class, args);

        // Aqui você abre a janela principal do Swing após o contexto estar pronto.
        // Exemplo: MainFrame.getInstance().setVisible(true);
        System.out.println("AV CAR AUTO CENTER — sistema iniciado.");
    }
}
