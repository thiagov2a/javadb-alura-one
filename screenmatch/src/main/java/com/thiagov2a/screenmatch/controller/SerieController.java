package com.thiagov2a.screenmatch.controller;

import com.thiagov2a.screenmatch.dto.EpisodioDTO;
import com.thiagov2a.screenmatch.dto.SerieDTO;
import com.thiagov2a.screenmatch.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/series")
public class SerieController {

    @Autowired
    private SerieService serieService;

    @GetMapping()
    public List<SerieDTO> obtenerSeries() {
        return serieService.obtenerSeries();
    }

    @GetMapping("/top5")
    public List<SerieDTO> obtenerTop5() {
        return serieService.obtenerTop5();
    }

    @GetMapping("/lanzamientos")
    public List<SerieDTO> obtenerSeriesPorEpisodiosRecientes() {
        return serieService.obtenerSeriesPorEpisodiosRecientes();
    }

    @GetMapping("/{id}")
    public SerieDTO obtenerSeriePorId(@PathVariable Long id) {
        return serieService.obtenerSeriePorId(id);
    }

    @GetMapping("/{id}/temporadas/todas")
    public List<EpisodioDTO> obtenerTemporadasDeSerie(@PathVariable Long id) {
        return serieService.obtenerTemporadasDeSerie(id);
    }

    @GetMapping("/{id}/temporadas/{temporada}")
    public List<EpisodioDTO> obtenerEpisodiosDeLaTemporada(@PathVariable Long id, @PathVariable Integer temporada) {
        return serieService.obtenerEpisodiosDeLaTemporada(id, temporada);
    }

    @GetMapping("/{id}/temporadas/top")
    public List<EpisodioDTO> obtenerTop5Episodios(@PathVariable Long id) {
        return serieService.obtenerTop5Episodios(id);
    }

    @GetMapping("/categoria/{nombre}")
    public List<SerieDTO> obtenerSeriesPorCategoria(@PathVariable String nombre) {
        return serieService.obtenerSeriesPorCategoria(nombre);
    }
}
