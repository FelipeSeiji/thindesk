package com.pfc.thindesk.dto;

import jakarta.validation.constraints.NotBlank;

public record ResolucaoChamadoRequest(@NotBlank String solucao) {
}
