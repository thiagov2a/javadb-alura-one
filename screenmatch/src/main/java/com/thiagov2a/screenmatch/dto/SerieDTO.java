package com.thiagov2a.screenmatch.dto;

import com.thiagov2a.screenmatch.model.Categoria;

public record SerieDTO(Long id,
                       String titulo,
                       Integer totalDeTemporadas,
                       Double evaluacion,
                       String poster,
                       Categoria categoria,
                       String actores,
                       String sinopsis) {
}
