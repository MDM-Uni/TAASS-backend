package com.petlife.negoziomicroservizio.controller;

import com.petlife.negoziomicroservizio.model.Carrello;
import com.petlife.negoziomicroservizio.model.Indirizzo;
import com.petlife.negoziomicroservizio.model.Ordine;
import com.petlife.negoziomicroservizio.model.Utente;
import com.petlife.negoziomicroservizio.repo.CarrelloRepository;
import com.petlife.negoziomicroservizio.repo.IndirizzoRepository;
import com.petlife.negoziomicroservizio.repo.OrdineRepository;
import com.petlife.negoziomicroservizio.repo.UtenteRepository;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/ordini")
public class OrdineController {
    @Autowired
    private UtenteRepository utenteRepository;
    @Autowired
    private CarrelloRepository carrelloRepository;
    @Autowired
    private OrdineRepository ordineRepository;
    @Autowired
    private IndirizzoRepository indirizzoRepository;

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrdini(@PathVariable long id) {
        Optional<Utente> utenteOpt = utenteRepository.findById(id);
        if (utenteOpt.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utente id=" + id + " non trovato");
        Utente utente = utenteOpt.get();

        return ResponseEntity.status(HttpStatus.OK).body(utente.getOrdini());
    }

    @PostMapping("/crea")
    public ResponseEntity<?> creaOrdine(@RequestParam long idCarrello, @RequestParam long idIndirizzo) {
        Optional<Carrello> carrelloOpt = carrelloRepository.findById(idCarrello);
        if (carrelloOpt.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Carrello id=" + idCarrello + " non trovato");
        Carrello carrello = carrelloOpt.get();

        if (carrello.getNumeroArticoli() == 0)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Carrello id=" + idCarrello + " vuoto");

        Optional<Indirizzo> indirizzoOpt = indirizzoRepository.findById(idIndirizzo);
        if (indirizzoOpt.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Indirizzo id=" + idIndirizzo + " non trovato");
        Indirizzo indirizzoConsegna = indirizzoOpt.get();

        Ordine ordine = ordineRepository.save(new Ordine(carrello.getProdottoQuantita(),indirizzoConsegna));

        Utente utente = utenteRepository.findByCarrelloId(idCarrello).orElseThrow();
        utente.aggiungiOrdine(ordine);
        utenteRepository.save(utente);

        carrello.svuota();
        carrelloRepository.save(carrello);
        return ResponseEntity.status(HttpStatus.OK).body(ordine);
    }

    @PostMapping("/annulla")
    public ResponseEntity<?> annullaOrdine(@RequestParam long idOrdine) {
        Optional<Ordine> ordineOpt = ordineRepository.findById(idOrdine);
        if (ordineOpt.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ordine id=" + idOrdine + " non trovato");
        Ordine ordine = ordineOpt.get();

        if (ordine.getDataConsegna() != null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ordine id=" + idOrdine + " già consegnato");

        Utente utente = utenteRepository.findByOrdiniId(idOrdine).orElseThrow();
        utente.annullaOrdine(ordine);
        utenteRepository.save(utente);

        ordineRepository.delete(ordine);
        return ResponseEntity.status(HttpStatus.OK).body(ordine);
    }
}
