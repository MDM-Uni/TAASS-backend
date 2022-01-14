package com.petlife.ospedalemicroservizio.controllers;

import com.petlife.ospedalemicroservizio.models.Ospedale;
import com.petlife.ospedalemicroservizio.models.Visita;
import com.petlife.ospedalemicroservizio.repositories.IndirizzoRepository;
import com.petlife.ospedalemicroservizio.repositories.OspedaleRepository;
import com.petlife.ospedalemicroservizio.repositories.VisitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("ospedale")
public class OspedaleController {
    //singleton
    public static Ospedale ospedale;
    @Autowired
    private OspedaleRepository ospedaleRepository;
    @Autowired
    private VisitaRepository visitaRepository;
    @Autowired private IndirizzoRepository indirizzoRepository;

    @GetMapping("/getInfo")
    public Ospedale getInfo() {
        loadOspedale();
        return OspedaleController.ospedale;
    }

    private void loadOspedale() {
        if (ospedale == null) {
            List<Ospedale> ospedali = ospedaleRepository.findAll();
            OspedaleController.ospedale = ospedali.get(0);
        }
    }

    @PostMapping("/pushInfo")
    @ResponseStatus(HttpStatus.OK)
    public void createOspedale(@RequestBody Ospedale osp) {
        if (osp.getIndirizzo() != null) indirizzoRepository.save(osp.getIndirizzo());
        if (osp.getVisite() != null) visitaRepository.saveAll(osp.getVisite());
        else osp.setVisite(new ArrayList<>());
        ospedaleRepository.save(osp);
    }

    @GetMapping("/getVisite")
    public List<Visita> getVisite() {
        loadOspedale();
        return OspedaleController.ospedale.getVisite();
    }

    @PostMapping("/pushVisita")
    @ResponseStatus(HttpStatus.OK)
    public void createVisita(@RequestBody Visita visita) {
        try {
            loadOspedale();
            OspedaleController.ospedale.getVisite().add(visita);
            visitaRepository.save(visita);
            ospedaleRepository.save(OspedaleController.ospedale);
            System.out.println();
        }
        catch (NoSuchElementException e) {
            System.out.println("Nessun ospedale trovato");
        }
    }
}
