package com.petlife.negoziomicroservizio.controller;

import com.petlife.negoziomicroservizio.controller.utils.UtenteChecker;
import com.petlife.negoziomicroservizio.model.Carrello;
import com.petlife.negoziomicroservizio.model.Prodotto;
import com.petlife.negoziomicroservizio.model.Utente;
import com.petlife.negoziomicroservizio.repo.CarrelloRepository;
import com.petlife.negoziomicroservizio.repo.ProdottoRepository;
import com.petlife.negoziomicroservizio.repo.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/carrelli")
public class CarrelloController {
    @Autowired
    private CarrelloRepository carrelloRepository;
    @Autowired
    private ProdottoRepository prodottoRepository;
    @Autowired
    UtenteChecker utenteChecker;

    @GetMapping("/{id}")
    public ResponseEntity<?> getCarrello(@PathVariable long id) {
        Optional<Utente> utenteOpt = utenteChecker.getUtente(id);

        if (utenteOpt.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utente con id=" + id + " non trovato");

        Utente utente = utenteOpt.get();
        Carrello carrello = utente.getCarrello();

        return ResponseEntity.status(HttpStatus.OK).body(carrello);
    }

    @PostMapping("/{idCarrello}/aggiungi")
    public ResponseEntity<?> aggiungiProdotto(
            @PathVariable long idCarrello,
            @RequestParam long idProdotto,
            @RequestParam int quantita) {
        Optional<Carrello> carrelloOpt = carrelloRepository.findById(idCarrello);
        if (carrelloOpt.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Carrello con id=" + idCarrello + " non trovato");
        Carrello carrello = carrelloOpt.get();

        Optional<Prodotto> prodottoOpt = prodottoRepository.findById(idProdotto);
        if (prodottoOpt.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Prodotto con id=" + idProdotto + " non trovato");
        Prodotto prodotto = prodottoOpt.get();

        if (quantita <= 0)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La quantità deve essere >= 0 (ora " + quantita + ")");

        carrello.aggiungiProdotto(prodotto, quantita);
        carrelloRepository.save(carrello);
        return ResponseEntity.status(HttpStatus.OK).body(carrello);
    }

    @PostMapping("/{idCarrello}/rimuovi")
    public ResponseEntity<?> rimuoviProdotto(
            @PathVariable long idCarrello,
            @RequestParam long idProdotto,
            @RequestParam int quantita) {
        Optional<Carrello> carrelloOpt = carrelloRepository.findById(idCarrello);
        if (carrelloOpt.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Carrello con id=" + idCarrello + " non trovato");
        Carrello carrello = carrelloOpt.get();

        Optional<Prodotto> prodottoOpt = prodottoRepository.findById(idProdotto);
        if (prodottoOpt.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Prodotto con id=" + idProdotto + " non trovato");
        Prodotto prodotto = prodottoOpt.get();

        if (!carrello.rimuoviProdotto(prodotto, quantita))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Prodotto id=" + idProdotto + " non presente nel carrello id=" + idCarrello);

        if (quantita <= 0)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La quantità deve essere >= 0 (ora " + quantita + ")");

        carrelloRepository.save(carrello);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
