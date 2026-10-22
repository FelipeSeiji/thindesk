package com.pfc.thindesk.dto;

import jakarta.validation.constraints.NotBlank;

public record AberturaChamadoRequest(
        @NotBlank String titulo,
        @NotBlank String descricao) {
}
