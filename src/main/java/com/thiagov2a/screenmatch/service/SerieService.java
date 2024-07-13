package com.thiagov2a.screenmatch.service;

import com.thiagov2a.screenmatch.dto.EpisodioDTO;
import com.thiagov2a.screenmatch.dto.SerieDTO;
import com.thiagov2a.screenmatch.model.Categoria;
import com.thiagov2a.screenmatch.model.Episodio;
import com.thiagov2a.screenmatch.model.Serie;
import com.thiagov2a.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SerieService {

    @Autowired
    private SerieRepository serieRepository;

    public List<SerieDTO> obtenerSeries() {
        return convertirDatos(serieRepository.findAll());
    }

    public List<SerieDTO> obtenerTop5() {
        return convertirDatos(serieRepository.findTop5ByOrderByEvaluacionDesc());
    }

    public List<SerieDTO> obtenerSeriesPorEpisodiosRecientes() {
        return convertirDatos(serieRepository.buscarPorEpisodiosRecientes());
    }

    public SerieDTO obtenerSeriePorId(Long id) {
        Optional<Serie> serieOptional = serieRepository.findById(id);

        return serieOptional.map(s -> new SerieDTO(s.getId(),
                s.getTitulo(),
                s.getTotalDeTemporadas(),
                s.getEvaluacion(),
                s.getPoster(),
                s.getCategoria(),
                s.getActores(),
                s.getSinopsis())).orElse(null);
    }

    public List<EpisodioDTO> obtenerTemporadasDeSerie(Long id) {
        Optional<Serie> serieOptional = serieRepository.findById(id);

        return serieOptional.map(s -> s.getEpisodios().stream()
                .map(e -> new EpisodioDTO(e.getTemporada(), e.getTitulo(), e.getNumeroDeEpisodio()))
                .collect(Collectors.toList())).orElse(null);
    }

    public List<EpisodioDTO> obtenerEpisodiosDeLaTemporada(Long id, Integer temporada) {
        Optional<Serie> serieOptional = serieRepository.findById(id);

        return serieOptional.map(s -> s.getEpisodios().stream()
                .filter(e -> e.getTemporada().equals(temporada))
                .map(e -> new EpisodioDTO(e.getTemporada(), e.getTitulo(), e.getNumeroDeEpisodio()))
                .collect(Collectors.toList())).orElse(null);
    }

    public List<SerieDTO> obtenerSeriesPorCategoria(String nombre) {
        Categoria categoria = Categoria.fromEspanol(nombre);
        return convertirDatos(serieRepository.findByCategoria(categoria));
    }

    public List<EpisodioDTO> obtenerTop5Episodios(Long id) {
        List<Episodio> episodios = serieRepository.buscarTop5Episodios();

        return episodios.stream()
                .map(e -> new EpisodioDTO(e.getTemporada(), e.getTitulo(), e.getNumeroDeEpisodio()))
                .collect(Collectors.toList());
    }

    public List<SerieDTO> convertirDatos(List<Serie> series) {
        return series.stream()
                .map(s -> new SerieDTO(s.getId(),
                        s.getTitulo(),
                        s.getTotalDeTemporadas(),
                        s.getEvaluacion(),
                        s.getPoster(),
                        s.getCategoria(),
                        s.getActores(),
                        s.getSinopsis()))
                .collect(Collectors.toList());
    }
}
