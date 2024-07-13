package com.thiagov2a.screenmatchfrases.service;

import com.thiagov2a.screenmatchfrases.dto.FraseDTO;
import com.thiagov2a.screenmatchfrases.model.Frase;
import com.thiagov2a.screenmatchfrases.repository.FraseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FraseService {

    @Autowired
    private FraseRepository fraseRepository;

    public FraseDTO obtenerFraseAleatoria() {
        Frase frase = fraseRepository.obtenerFraseAleatoria();
        return new FraseDTO(frase.getTitulo(), frase.getFrase(), frase.getPersonaje(), frase.getPoster());
    }
}
