package com.petlife.ospedalemicroservizio.controllers;

import com.petlife.ospedalemicroservizio.models.Ospedale;
import com.petlife.ospedalemicroservizio.models.TipoEvento;
import com.petlife.ospedalemicroservizio.models.TipoVisita;
import com.petlife.ospedalemicroservizio.models.Visita;
import com.petlife.ospedalemicroservizio.repositories.IndirizzoRepository;
import com.petlife.ospedalemicroservizio.repositories.OspedaleRepository;
import com.petlife.ospedalemicroservizio.repositories.VisitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("ospedale")
@CrossOrigin(origins = "*")
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

    private boolean loadOspedale() {
        if (ospedale == null) {
            List<Ospedale> ospedali = ospedaleRepository.findAll();
            try {
                OspedaleController.ospedale = ospedali.get(0);
            }
            catch (IndexOutOfBoundsException e) {
                System.out.println("Nessun ospedale trovato");
                return false;
            }
        }
        return true;
    }

    /**
     * per creare le informazioni dell'Ospedale o cambiarle
     * @param osp
     */
    @PostMapping("/pushInfo")
    @ResponseStatus(HttpStatus.OK)
    public void createOspedale(@RequestBody Ospedale osp) {
        boolean esisteOspedale = loadOspedale();
        if (!esisteOspedale) { //creazione ex-novo
            ospedale = osp;
            if (osp.getVisite() != null) visitaRepository.saveAll(osp.getVisite());
            else osp.setVisite(new ArrayList<>());
        } else { //aggiornamento
            ospedale.setNome(osp.getNome());
            ospedale.setIndirizzo(osp.getIndirizzo());
        }
        //in comune con i due rami dell'if
        if (osp.getIndirizzo() != null) indirizzoRepository.save(osp.getIndirizzo());
        ospedaleRepository.save(osp);
    }

    @GetMapping({"/getVisite", "/getVisite/{idVisita}"})
    public List<Visita> getVisite(
            @PathVariable(value = "idVisita") Optional<Long> idVisita,
            @RequestParam(value = "idAnimale") Optional<Long> idAnimale,
            @RequestParam(value = "tipoVisita") Optional<String> tipoVisita
    ) {
        loadOspedale();
        Stream<Visita> streamVisite = OspedaleController.ospedale.getVisite().stream();
        //nel caso sia un URL del tipo /getVisite/{idVisita}
        if (idVisita.isPresent()) {
            streamVisite = streamVisite.filter(visita -> idVisita.get().equals(visita.getId()));
            assert(streamVisite.count() <= 1);
        } else { //nel caso sia un URL del tipo /getVisite
            if (idAnimale.isPresent() && idAnimale.get() != 0)
                streamVisite = streamVisite.filter(v -> idAnimale.get().equals(v.getIdAnimale()));
            if (tipoVisita.isPresent() && !tipoVisita.get().equals(""))
                streamVisite = streamVisite.filter(v -> tipoVisita.get().equals(v.getTipoVisita().toString()));
        }
        return streamVisite.collect(Collectors.toList());
    }

    @PostMapping("/pushVisita")
    @ResponseStatus(HttpStatus.OK)
    public void createVisita(@RequestBody Visita visita) {
        try {
            loadOspedale();
            OspedaleController.ospedale.getVisite().add(visita);
            visitaRepository.save(visita);
            ospedaleRepository.save(OspedaleController.ospedale);
        }
        catch (NoSuchElementException e) {
            System.out.println("Nessun ospedale trovato");
        }
    }

    @PostMapping("/deleteVisita")
    @ResponseStatus(HttpStatus.OK)
    public void deleteVisita(@RequestBody Visita visita) {
        loadOspedale();
        OspedaleController.ospedale.getVisite().remove(visita);
        ospedaleRepository.save(OspedaleController.ospedale);
        visitaRepository.delete(visita);

    }
}
