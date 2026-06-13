package br.com.picarauto;

import br.com.picarauto.view.MainFrame;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import javax.swing.SwingUtilities;

/**
 * @author Caio4breu
 */
@SpringBootApplication
public class Main {

    public static void main(String[] args) throws Exception {
        // Sobe o Spring com headless=false (necessário para Swing)
        new SpringApplicationBuilder(Main.class)
            .headless(false)
            .run(args);

        // Abre o MainFrame na thread do Swing
        // invokeAndWait bloqueia a thread principal, mantendo a JVM viva
        SwingUtilities.invokeAndWait(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
