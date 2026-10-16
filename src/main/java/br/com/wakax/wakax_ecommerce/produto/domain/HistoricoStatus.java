package br.com.wakax.wakax_ecommerce.produto.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "historico_status")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoricoStatus {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    @NotNull
    private StatusProduto status;

    @Column(name = "data_evento", nullable = false)
    @NotNull
    private LocalDateTime dataEvento;

    @Column(nullable = false)
    @NotBlank
    private String motivo;

    @Column(nullable = false)
    @NotBlank
    private String usuario;

    @PrePersist
    protected void onCreate() {
        dataEvento = LocalDateTime.now();
    }


}
