package com.thiagov2a.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosTemporada(@JsonAlias("Season") String numero,
                             @JsonAlias("Episodes") List<DatosEpisodio> episodios) {
}
