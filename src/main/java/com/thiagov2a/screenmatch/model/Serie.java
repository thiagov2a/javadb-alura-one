package com.thiagov2a.screenmatch.model;

import com.theokanning.openai.OpenAiHttpException;
import com.thiagov2a.screenmatch.service.ConsultaChatGPT;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "series")
public class Serie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    private Integer totalDeTemporadas;
    private Double evaluacion;
    private String poster;
    @Enumerated(EnumType.STRING)
    private Categoria categoria;
    private String actores;
    private String sinopsis;
    @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Episodio> episodios;

    public Serie() {
    }

    public Serie(DatosSerie datosSerie) {
        this.titulo = datosSerie.titulo();
        try {
            this.totalDeTemporadas = Integer.parseInt(datosSerie.totalDeTemporadas());
        } catch (NumberFormatException e) {
            this.totalDeTemporadas = 0;  // Valor por defecto sí ocurre un error
        }
        try {
            this.evaluacion = Double.parseDouble(datosSerie.evaluacion());
        } catch (NumberFormatException e) {
            this.evaluacion = 0.0;  // Valor por defecto sí ocurre un error
        }
        this.poster = datosSerie.poster();
        this.categoria = Categoria.fromString(datosSerie.genero().split(",")[0].trim());
        this.actores = datosSerie.actores();
        try {
            this.sinopsis = ConsultaChatGPT.obtenerTraduccion(datosSerie.sinopsis());
        } catch (OpenAiHttpException e) {
            // System.err.println("Error al obtener la traducción de la sinopsis: " + e.getMessage());
            this.sinopsis = datosSerie.sinopsis();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getTotalDeTemporadas() {
        return totalDeTemporadas;
    }

    public void setTotalDeTemporadas(Integer totalDeTemporadas) {
        this.totalDeTemporadas = totalDeTemporadas;
    }

    public Double getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(Double evaluacion) {
        this.evaluacion = evaluacion;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public String getActores() {
        return actores;
    }

    public void setActores(String actores) {
        this.actores = actores;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public List<Episodio> getEpisodios() {
        return episodios;
    }

    public void setEpisodios(List<Episodio> episodios) {
        episodios.forEach(e -> e.setSerie(this));
        this.episodios = episodios;
    }

    @Override
    public String toString() {
        return "Título: " + titulo +
               " | Temporadas: " + totalDeTemporadas +
               " | Evaluación: " + evaluacion +
               " | Poster: " + poster +
               " | Género: " + categoria.getNombreEspanol() +
               " | Actores: " + actores +
               " | Sinopsis: " + sinopsis;
    }
}
