package com.petlife.negoziomicroservizio.controller;

import com.petlife.negoziomicroservizio.model.Carrello;
import com.petlife.negoziomicroservizio.model.Prodotto;
import com.petlife.negoziomicroservizio.repo.CarrelloRepository;
import com.petlife.negoziomicroservizio.repo.ProdottoRepository;
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

    @GetMapping("/{idCarrello}/prodotti/{idProdotto}/aggiungi/{quantita}")
    public ResponseEntity<String> aggiungiProdotto(
            @PathVariable long idCarrello,
            @PathVariable long idProdotto,
            @PathVariable int quantita) {
        Optional<Carrello> carrelloOpt = carrelloRepository.findById(idCarrello);
        if (carrelloOpt.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Carrello con id=" + idCarrello + " non trovato");
        Carrello carrello = carrelloOpt.get();

        Optional<Prodotto> prodottoOpt = prodottoRepository.findById(idProdotto);
        if (prodottoOpt.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Prodotto con id=" + idProdotto + " non trovato");
        Prodotto prodotto = prodottoOpt.get();

        carrello.aggiungiProdotto(prodotto, quantita);
        carrelloRepository.save(carrello);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{idCarrello}/prodotti/{idProdotto}/rimuovi/{quantita}")
    public ResponseEntity<String> rimuoviProdotto(
            @PathVariable long idCarrello,
            @PathVariable long idProdotto,
            @PathVariable int quantita) {
        Optional<Carrello> carrelloOpt = carrelloRepository.findById(idCarrello);
        if (carrelloOpt.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Carrello con id=" + idCarrello + " non trovato");
        Carrello carrello = carrelloOpt.get();

        Optional<Prodotto> prodottoOpt = prodottoRepository.findById(idProdotto);
        if (prodottoOpt.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Prodotto con id=" + idProdotto + " non trovato");
        Prodotto prodotto = prodottoOpt.get();

        if (!carrello.rimuoviProdotto(prodotto, quantita))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Prodotto id=" + idProdotto +
                    " non presente nel carrello id=" + idCarrello);
        else {
            carrelloRepository.save(carrello);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
}
