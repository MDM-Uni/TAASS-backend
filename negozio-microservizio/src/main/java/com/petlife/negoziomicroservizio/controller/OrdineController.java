package com.petlife.negoziomicroservizio.controller;

import com.petlife.negoziomicroservizio.model.*;
import com.petlife.negoziomicroservizio.repo.*;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Optional;

@CrossOrigin(origins = "*")
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
    @Autowired
    private AnimaleRepository animaleRepository;

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrdini(@PathVariable long id) {
        Optional<Utente> utenteOpt = utenteRepository.findById(id);
        if (utenteOpt.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utente id=" + id + " non trovato");
        Utente utente = utenteOpt.get();

        return ResponseEntity.status(HttpStatus.OK).body(utente.getOrdini());
    }

    @GetMapping("/{idUtente}/{idAnimale}")
    public ResponseEntity<?> getOrdiniAnimale(@PathVariable long idUtente, @PathVariable long idAnimale) {
        Optional<Utente> utenteOpt = utenteRepository.findById(idUtente);
        if (utenteOpt.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utente id=" + idUtente + " non trovato");
        Utente utente = utenteOpt.get();

        Optional<Animale> animaleOpt = animaleRepository.findById(idAnimale);
        if (animaleOpt.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Animale id=" + idAnimale + " non trovato");
        Animale animale = animaleOpt.get();

        if (!utente.haAnimale(animale))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("L'utente id=" + utente.getId() + " non ha l'animale id=" + idAnimale);

        return ResponseEntity.status(HttpStatus.OK).body(animale.getOrdini());
    }

    @PostMapping("/crea")
    public ResponseEntity<?> creaOrdine(@RequestParam long idCarrello,
                                        @RequestParam long idIndirizzo,
                                        @RequestParam long idAnimale) {
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

        Optional<Animale> animaleOpt = animaleRepository.findById(idAnimale);
        if (animaleOpt.isEmpty())
            animaleOpt = Optional.of(animaleRepository.save(new Animale(idAnimale))); // aggiungi nuovo animale
        Animale animale = animaleOpt.get();

        Utente utente = utenteRepository.findByCarrelloId(idCarrello).orElseThrow();

        if (!utente.haAnimale(animale)) { // aggiungi animale all'utente
            utente.aggiungiAnimale(animale);
            utenteRepository.save(utente);
        }

        Ordine ordine = ordineRepository.save(new Ordine(carrello.getProdottoQuantita(),indirizzoConsegna));

        animale.aggiungiOrdine(ordine);
        animaleRepository.save(animale);

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

        Animale animale = animaleRepository.findByOrdiniId(idOrdine).orElseThrow();
        animale.annullaOrdine(ordine);
        animaleRepository.save(animale);

        ordineRepository.delete(ordine);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
