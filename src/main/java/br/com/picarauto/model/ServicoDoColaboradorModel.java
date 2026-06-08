package br.com.picarauto.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;

/**
 * Representa o vínculo entre um colaborador e os serviços internos
 * que ele executou, registrando a data de execução.
 *
 * Tabela com chave composta (idColaborador + idServicoInterno + dataServico).
 * Não estende BaseModel pois não possui PK serial, ativo nem data_hora_criacao.
 * O acesso é feito exclusivamente via
 * {@link br.com.picarauto.repository.ServicoDoColaboradorRepository}
 * usando EntityManager com queries nativas.
 *
 * @author Gabriel
 */
@Data
@EqualsAndHashCode
@Entity
@Table(name = "servicosDoColaborador")
@IdClass(ServicoDoColaboradorModel.ChaveComposta.class)
public class ServicoDoColaboradorModel {

    @Id
    @Column(name = "idColaborador", nullable = false)
    private Long idColaborador;

    @Id
    @Column(name = "idServicoInterno", nullable = false)
    private Long idServicoInterno;

    @Id
    @Column(name = "dataServico", nullable = false)
    private LocalDate dataServico;

    /**
     * Classe auxiliar que representa a chave composta.
     * Necessária para o JPA identificar registros unicamente.
     */
    public static class ChaveComposta implements java.io.Serializable {
        private Long idColaborador;
        private Long idServicoInterno;
        private LocalDate dataServico;

        public ChaveComposta() {}

        public ChaveComposta(Long idColaborador, Long idServicoInterno, LocalDate dataServico) {
            this.idColaborador    = idColaborador;
            this.idServicoInterno = idServicoInterno;
            this.dataServico      = dataServico;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ChaveComposta that)) return false;
            return java.util.Objects.equals(idColaborador, that.idColaborador)
                && java.util.Objects.equals(idServicoInterno, that.idServicoInterno)
                && java.util.Objects.equals(dataServico, that.dataServico);
        }

        @Override
        public int hashCode() {
            return java.util.Objects.hash(idColaborador, idServicoInterno, dataServico);
        }
    }
}