package com.thiagov2a.screenmatch.repository;

import com.thiagov2a.screenmatch.model.Categoria;
import com.thiagov2a.screenmatch.model.Episodio;
import com.thiagov2a.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {

    Optional<Serie> findByTituloIgnoreCase(String titulo);

    List<Serie> findByTituloContainsIgnoreCase(String titulo);

    List<Serie> findTop5ByOrderByEvaluacionDesc();

    List<Serie> findByCategoria(Categoria categoria);

    @Query("SELECT s FROM Serie s WHERE s.totalDeTemporadas <= :totalDeTemporadas")
    List<Serie> buscarPorTemporadaMenorIgual(int totalDeTemporadas);

    @Query("SELECT s FROM Serie s WHERE s.totalDeTemporadas >= :totalDeTemporadas")
    List<Serie> buscarPorTemporadaMayorIgual(int totalDeTemporadas);

    @Query("SELECT s FROM Serie s WHERE s.evaluacion <= :evaluacion")
    List<Serie> buscarPorEvaluacionMenorIgual(double evaluacion);

    @Query("SELECT s FROM Serie s WHERE s.evaluacion >= :evaluacion")
    List<Serie> buscarPorEvaluacionMayorIgual(double evaluacion);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE e.titulo = :tituloEpisodio")
    Optional<Episodio> buscarEpisodioPorTitulo(String tituloEpisodio);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE e.titulo ILIKE %:tituloEpisodio%")
    List<Episodio> buscarEpisodiosPorNombre(String tituloEpisodio);

    @Query("SELECT e FROM Serie s JOIN s.episodios e ORDER BY e.evaluacion DESC LIMIT 5")
    List<Episodio> buscarTop5Episodios();

    @Query("SELECT s FROM Serie s JOIN s.episodios e GROUP BY s ORDER BY MAX(e.fechaDeLanzamiento) DESC LIMIT 5")
    List<Serie> buscarPorEpisodiosRecientes();
}
