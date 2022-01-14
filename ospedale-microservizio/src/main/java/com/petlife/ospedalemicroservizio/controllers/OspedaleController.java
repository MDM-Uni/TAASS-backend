package com.petlife.ospedalemicroservizio.controllers;

import com.petlife.ospedalemicroservizio.models.Ospedale;
import com.petlife.ospedalemicroservizio.models.Visita;
import com.petlife.ospedalemicroservizio.repositories.OspedaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("ospedale")
public class OspedaleController {
    //singleton
    public static Ospedale ospedale;
    @Autowired
    private OspedaleRepository ospedaleRepository;

    @GetMapping("/getInfo")
    public Ospedale getInfo() {
        List<Ospedale> ospedali = ospedaleRepository.findAll();
        if (ospedali != null && ospedali.size() > 0) {
            OspedaleController.ospedale = ospedali.get(0);
            return OspedaleController.ospedale;
        } else {
            return null;
        }
    }

    @GetMapping("/getVisite")
    public List<Visita> getVisite() {
        return ospedale.getVisite();
    }

    @PostMapping("/pushInfo")
    @ResponseStatus(HttpStatus.OK)
    public void createOspedale(@RequestBody Ospedale osp) {
        ospedaleRepository.save(osp);
    }


    @PostMapping("/pushVisita")
    @ResponseStatus(HttpStatus.OK)
    public void createVisita(@RequestBody Visita visita) {
        boolean risultato = VisitaController.createVisita(visita);
        OspedaleController.ospedale.getVisite().add(visita);
        ospedaleRepository.
    }
}
