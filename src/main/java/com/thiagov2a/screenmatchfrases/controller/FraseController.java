package com.thiagov2a.screenmatchfrases.controller;

import com.thiagov2a.screenmatchfrases.dto.FraseDTO;
import com.thiagov2a.screenmatchfrases.service.FraseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FraseController {

    @Autowired
    private FraseService fraseService;

    @GetMapping("/series/frases")
    public FraseDTO getFrase() {
        return fraseService.obtenerFraseAleatoria();
    }

}
