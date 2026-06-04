package br.com.picarauto.decorator;

/**
 * Decorator abstrato do Padrão Decorator para resumo de OS.
 *
 * Padrão de Projeto: Decorator (decorator abstrato)
 * Envolve um {@link ResumoOS} existente e delega a chamada base,
 * permitindo que subclasses adicionem seções ao resumo sem modificar
 * o componente original.
 *
 * @author Caio4breu
 */
public abstract class ResumoOSDecorator implements IResumoOS {

    protected final IResumoOS decorado;

    protected ResumoOSDecorator(IResumoOS decorado) {
        this.decorado = decorado;
    }

    @Override
    public String gerar() {
        return decorado.gerar();
    }
}