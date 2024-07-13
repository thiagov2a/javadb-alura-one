package com.thiagov2a.screenmatch.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Entity
@Table(name = "episodios")
public class Episodio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer temporada;
    private Integer numeroDeEpisodio;
    private String titulo;
    private Double evaluacion;
    private LocalDate fechaDeLanzamiento;
    @ManyToOne
    private Serie serie;

    public Episodio() {
    }

    public Episodio(String temporada, DatosEpisodio datosEpisodio) {
        try {
            this.temporada = Integer.parseInt(temporada);
        } catch (NumberFormatException e) {
            this.temporada = 0; // Valor por defecto si ocurre un error
        }
        this.titulo = datosEpisodio.titulo();
        try {
            this.numeroDeEpisodio = Integer.parseInt(datosEpisodio.numeroEpisodio());
        } catch (NumberFormatException e) {
            this.numeroDeEpisodio = 0; // Valor por defecto si ocurre un error
        }
        try {
            this.evaluacion = Double.parseDouble(datosEpisodio.evaluacion());
        } catch (NumberFormatException e) {
            this.evaluacion = 0.0;  // Valor por defecto sí ocurre un error
        }
        try {
            this.fechaDeLanzamiento = LocalDate.parse(datosEpisodio.fechaDeLanzamiento());
        } catch (DateTimeParseException e) {
            this.fechaDeLanzamiento = null; // Valor por defecto sí ocurre un error
        }
    }

    public Serie getSerie() {
        return serie;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
    }

    public Double getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(Double evaluacion) {
        this.evaluacion = evaluacion;
    }

    public LocalDate getFechaDeLanzamiento() {
        return fechaDeLanzamiento;
    }

    public void setFechaDeLanzamiento(LocalDate fechaDeLanzamiento) {
        this.fechaDeLanzamiento = fechaDeLanzamiento;
    }

    public Integer getNumeroDeEpisodio() {
        return numeroDeEpisodio;
    }

    public void setNumeroDeEpisodio(Integer numeroDeEpisodio) {
        this.numeroDeEpisodio = numeroDeEpisodio;
    }

    public Integer getTemporada() {
        return temporada;
    }

    public void setTemporada(Integer temporada) {
        this.temporada = temporada;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String fechaFormateada = (fechaDeLanzamiento != null) ? fechaDeLanzamiento.format(formatter) : "No disponible";

        return "Temporada: " + temporada +
               " | Episodio: " + numeroDeEpisodio +
               " | Título: " + titulo +
               " | Evaluación: " + evaluacion +
               " | Fecha de Lanzamiento: " + fechaFormateada;
    }
}
