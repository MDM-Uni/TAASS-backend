package com.petlife.negoziomicroservizio.controller;

import com.petlife.negoziomicroservizio.controller.utils.UtenteChecker;
import com.petlife.negoziomicroservizio.model.Indirizzo;
import com.petlife.negoziomicroservizio.model.Utente;
import com.petlife.negoziomicroservizio.repo.IndirizzoRepository;
import com.petlife.negoziomicroservizio.repo.OrdineRepository;
import com.petlife.negoziomicroservizio.repo.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/indirizzi")
public class IndirizzoController {
    @Autowired
    private IndirizzoRepository indirizzoRepository;
    @Autowired
    private UtenteRepository utenteRepository;
    @Autowired
    UtenteChecker utenteChecker;
    @Autowired
    private OrdineRepository ordineRepository;

    @GetMapping("/{id}")
    public ResponseEntity<?> getIndirizzi(@PathVariable long id) {
        Optional<Utente> utenteOpt = utenteChecker.getUtente(id);
        if (utenteOpt.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utente id=" + id + " non trovato");
        Utente utente = utenteOpt.get();

        return ResponseEntity.status(HttpStatus.OK).body(utente.getIndirizzi());
    }

    @PostMapping("/{id}/crea")
    public ResponseEntity<?> aggiungiIndirizzo(@PathVariable long id, @RequestBody Indirizzo indirizzo) {
        Optional<Utente> utenteOpt = utenteChecker.getUtente(id);
        if (utenteOpt.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utente id=" + id + " non trovato");
        Utente utente = utenteOpt.get();

        indirizzoRepository.save(indirizzo);

        utente.aggiungiIndirizzo(indirizzo);
        utenteRepository.save(utente);
        return ResponseEntity.status(HttpStatus.OK).body(indirizzo);
    }

    @PostMapping("/{id}/rimuovi")
    public ResponseEntity<?> rimuoviIndirizzo(@PathVariable long id, @RequestParam long idIndirizzo) {
        Optional<Utente> utenteOpt = utenteChecker.getUtente(id);
        if (utenteOpt.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utente id=" + id + " non trovato");
        Utente utente = utenteOpt.get();

        Optional<Indirizzo> indirizzoOpt = indirizzoRepository.findById(idIndirizzo);
        if (indirizzoOpt.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Indirizzo id=" + id + " non trovato");
        Indirizzo indirizzo = indirizzoOpt.get();

        utente.rimuoviIndirizzo(indirizzo);
        utenteRepository.save(utente);

        if (!ordineRepository.existsByIndirizzoConsegnaId(indirizzo.getId())) // cancella solo se non è referenziato
            indirizzoRepository.delete(indirizzo);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
