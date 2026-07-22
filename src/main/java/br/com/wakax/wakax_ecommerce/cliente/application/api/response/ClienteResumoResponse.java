package br.com.wakax.wakax_ecommerce.cliente.application.api.response;

import br.com.wakax.wakax_ecommerce.pessoa.domain.StatusPessoa;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.UUID;

public record ClienteResumoResponse(

        String nome,
        String email,
        StatusPessoa status,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime dataCadastro,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime dataUltimaAtualizacao


) {
}
