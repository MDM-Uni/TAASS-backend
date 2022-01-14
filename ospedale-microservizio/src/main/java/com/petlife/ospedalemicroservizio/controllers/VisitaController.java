package com.petlife.ospedalemicroservizio.controllers;

import com.petlife.ospedalemicroservizio.models.Visita;
import com.petlife.ospedalemicroservizio.repositories.VisitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("visita")
public class VisitaController {
    @Autowired
    private VisitaRepository visitaRepository;

    public static boolean createVisita(Visita visita) {

    }

}
