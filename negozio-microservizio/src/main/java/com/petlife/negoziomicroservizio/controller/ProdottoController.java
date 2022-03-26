package com.petlife.negoziomicroservizio.controller;

import com.petlife.negoziomicroservizio.model.Prodotto;
import com.petlife.negoziomicroservizio.repo.ProdottoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/prodotti")
public class ProdottoController {

    @Autowired
    private ProdottoRepository repository;

    @GetMapping
    public ResponseEntity<?> getAllProdotti() {
        List<Prodotto> prodotti = new ArrayList<>();
        repository.findAll().forEach(prodotti::add);
        return ResponseEntity.ok().body(prodotti);
    }

    @PostMapping(value = "/crea", consumes = { "multipart/form-data" })
    public ResponseEntity<?> salvaProdotto(@RequestParam String nome,
                              @RequestParam double prezzo,
                              @RequestParam String categoria,
                              @ModelAttribute MultipartFile immagine) throws IOException {
        Prodotto prodotto = new Prodotto();
        prodotto.setNome(nome);
        prodotto.setPrezzo(prezzo);
        prodotto.setCategoria(categoria);
        prodotto.setImmagine(immagine.getBytes());
        repository.save(prodotto);
        return ResponseEntity.ok().body(prodotto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProdotto(@PathVariable long id) {
        Optional<Prodotto> prodottoOpt = repository.findById(id);
        if (prodottoOpt.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Prodotto id=" + id + " non trovato");
        return ResponseEntity.status(HttpStatus.OK).body(prodottoOpt.get());
    }

    @ResponseBody
    @GetMapping(value = "/{id}/immagine", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<?> getImmagineProdotto(@PathVariable long id) {
        Optional<Prodotto> prodottoOpt = repository.findById(id);
        if (prodottoOpt.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Prodotto id=" + id + " non trovato");
        return ResponseEntity.status(HttpStatus.OK).body(prodottoOpt.get().getImmagine());
    }
}
