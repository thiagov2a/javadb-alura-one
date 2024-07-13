package com.thiagov2a.screenmatchfrases.repository;

import com.thiagov2a.screenmatchfrases.model.Frase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FraseRepository extends JpaRepository<Frase, Long> {

    @Query("SELECT f FROM Frase f ORDER BY RANDOM() LIMIT 1")
    public Frase obtenerFraseAleatoria();
}
