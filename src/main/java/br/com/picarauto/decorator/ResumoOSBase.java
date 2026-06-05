package br.com.picarauto.decorator;

import br.com.picarauto.model.OrdemServicoModel;

/**
 * Componente base do Padrão Decorator: gera o cabeçalho fixo da OS.
 *
 * Padrão de Projeto: Decorator (componente concreto)
 * Fornece as informações essenciais da OS — número, cliente, veículo,
 * status e total. Os decoradores constroem sobre este texto.
 *
 * @author Caio4breu
 */
public class ResumoOSBase implements IResumoOS {

    private final OrdemServicoModel os;

    public ResumoOSBase(OrdemServicoModel os) {
        this.os = os;
    }

    @Override
    public String gerar() {
        StringBuilder sb = new StringBuilder();
        sb.append("========== ORDEM DE SERVIÇO ==========\n");
        sb.append("OS Nº: ").append(os.getNumero()).append("\n");
        sb.append("Status: ").append(os.getStatusOrdemServico()).append("\n");

        if (os.getCliente() != null) {
            sb.append("Cliente: ").append(os.getCliente().getNomeCompleto()).append("\n");
        }
        if (os.getVeiculo() != null) {
            sb.append("Veículo: ").append(os.getVeiculo().getPlaca()).append("\n");
        }
        if (os.getDataAbertura() != null) {
            sb.append("Abertura: ").append(os.getDataAbertura()).append("\n");
        }

        sb.append("Problema: ").append(os.getDescricaoProblema()).append("\n");
        sb.append("Total: R$ ").append(os.calcularTotal()).append("\n");
        return sb.toString();
    }
}