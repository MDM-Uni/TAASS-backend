package com.petlife.negoziomicroservizio.controller;

import com.petlife.negoziomicroservizio.model.Prodotto;
import com.petlife.negoziomicroservizio.model.Utente;
import com.petlife.negoziomicroservizio.repo.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/utenti")
public class UtenteController {

    @Autowired
    UtenteRepository repository;

    @GetMapping("/{id}/carrello")
    public ResponseEntity<?> getCarrello(@PathVariable long id) {
        Optional<Utente> utenteOpt = repository.findById(id);
        if (utenteOpt.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utente id=" + id + " non trovato");
        Utente utente = utenteOpt.get();

        return ResponseEntity.status(HttpStatus.OK).body(utente.getCarrello());
    }

    @GetMapping("/{id}/ordini")
    public ResponseEntity<?> getOrdini(@PathVariable long id) {
        Optional<Utente> utenteOpt = repository.findById(id);
        if (utenteOpt.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utente id=" + id + " non trovato");
        Utente utente = utenteOpt.get();

        return ResponseEntity.status(HttpStatus.OK).body(utente.getOrdini());
    }
}
