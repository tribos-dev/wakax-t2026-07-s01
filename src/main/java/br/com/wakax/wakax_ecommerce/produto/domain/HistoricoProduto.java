package br.com.wakax.wakax_ecommerce.produto.domain;

import br.com.wakax.wakax_ecommerce.auth.usuario.domain.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "historico_produto")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class HistoricoProduto {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(name = "data_registro", nullable = false)
    @NotNull
    private LocalDateTime dataRegistro;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false, referencedColumnName = "idUsuario")
    @NotNull
    private Usuario usuario;


}
