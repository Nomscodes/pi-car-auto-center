package br.com.picarauto.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;

/**
 * Representa o vínculo entre um fornecedor e o serviço externo que ele prestou,
 * registrando a data de execução.
 *
 * Tabela com chave composta (idFornecedor + idItemPedidoServicoExterno + dataExecucao).
 * Não estende BaseModel pois não possui PK serial, ativo nem data_hora_criacao.
 * O acesso é feito exclusivamente via
 * {@link br.com.picarauto.repository.ItemFornecedorRepository}
 * usando EntityManager com queries nativas.
 *
 * @author Gabriel
 */
@Data
@EqualsAndHashCode
@Entity
@Table(name = "itemFornecedor")
@IdClass(ItemFornecedorModel.ChaveComposta.class)
public class ItemFornecedorModel {

    @Id
    @Column(name = "idFornecedor", nullable = false)
    private Long idFornecedor;

    @Id
    @Column(name = "idItemPedidoServicoExterno", nullable = false)
    private Long idItemPedidoServicoExterno;

    @Id
    @Column(name = "dataExecucao", nullable = false)
    private LocalDate dataExecucao;

    /**
     * Classe auxiliar que representa a chave composta.
     * Necessária para o JPA identificar registros unicamente.
     */
    public static class ChaveComposta implements java.io.Serializable {
        private Long idFornecedor;
        private Long idItemPedidoServicoExterno;
        private LocalDate dataExecucao;

        public ChaveComposta() {}

        public ChaveComposta(Long idFornecedor, Long idItemPedidoServicoExterno, LocalDate dataExecucao) {
            this.idFornecedor                  = idFornecedor;
            this.idItemPedidoServicoExterno    = idItemPedidoServicoExterno;
            this.dataExecucao                  = dataExecucao;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ChaveComposta that)) return false;
            return java.util.Objects.equals(idFornecedor, that.idFornecedor)
                && java.util.Objects.equals(idItemPedidoServicoExterno, that.idItemPedidoServicoExterno)
                && java.util.Objects.equals(dataExecucao, that.dataExecucao);
        }

        @Override
        public int hashCode() {
            return java.util.Objects.hash(idFornecedor, idItemPedidoServicoExterno, dataExecucao);
        }
    }
}