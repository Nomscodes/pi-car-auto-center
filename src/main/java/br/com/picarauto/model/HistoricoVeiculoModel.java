package br.com.picarauto.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;

/**
 * Representa o histórico de proprietários de um veículo ao longo do tempo.
 *
 * Tabela com chave composta (idPessoa + idVeiculo + dataInicio).
 * Não estende BaseModel pois não possui PK serial, ativo nem data_hora_criacao.
 * O acesso é feito exclusivamente via {@link br.com.picarauto.repository.HistoricoVeiculoRepository}
 * usando EntityManager com queries nativas.
 *
 * @author Gabriel
 */
@Data
@EqualsAndHashCode
@Entity
@Table(name = "historicoVeiculo")
@IdClass(HistoricoVeiculoModel.ChaveComposta.class)
public class HistoricoVeiculoModel {

    @Id
    @Column(name = "idPessoa", nullable = false)
    private Long idPessoa;

    @Id
    @Column(name = "idVeiculo", nullable = false)
    private Long idVeiculo;

    @Id
    @Column(name = "dataInicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "dataFim")
    private LocalDate dataFim;

    /**
     * Classe auxiliar que representa a chave composta.
     * Necessária para o JPA identificar registros unicamente.
     */
    public static class ChaveComposta implements java.io.Serializable {
        private Long idPessoa;
        private Long idVeiculo;
        private LocalDate dataInicio;

        public ChaveComposta() {}

        public ChaveComposta(Long idPessoa, Long idVeiculo, LocalDate dataInicio) {
            this.idPessoa   = idPessoa;
            this.idVeiculo  = idVeiculo;
            this.dataInicio = dataInicio;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ChaveComposta that)) return false;
            return java.util.Objects.equals(idPessoa, that.idPessoa)
                && java.util.Objects.equals(idVeiculo, that.idVeiculo)
                && java.util.Objects.equals(dataInicio, that.dataInicio);
        }

        @Override
        public int hashCode() {
            return java.util.Objects.hash(idPessoa, idVeiculo, dataInicio);
        }
    }
}