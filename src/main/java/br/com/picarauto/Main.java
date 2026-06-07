package br.com.picarauto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import javax.swing.SwingUtilities;

/**
 *
 * @author Caio4breu
 */
@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        var ctx = SpringApplication.run(Main.class, args);

        SwingUtilities.invokeLater(() -> {
            // aqui você vai chamar a sua tela principal futuramente
            // ex: ctx.getBean(MainView.class).setVisible(true);
            System.out.println("Spring Boot iniciado! Swing pronto para uso.");
        });
    }
}