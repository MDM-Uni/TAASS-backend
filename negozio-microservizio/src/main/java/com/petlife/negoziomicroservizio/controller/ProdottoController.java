package com.petlife.negoziomicroservizio.controller;

import com.petlife.negoziomicroservizio.model.Prodotto;
import com.petlife.negoziomicroservizio.repo.ProdottoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/prodotti")
public class ProdottoController {

    @Autowired
    private ProdottoRepository repository;

    @GetMapping
    public List<Prodotto> getAllProdotti() {
        List<Prodotto> prodotti = new ArrayList<>();
        repository.findAll().forEach(prodotti::add);
        return prodotti;
    }

    @PostMapping("/crea")
    public void salvaProdotto(@RequestBody Prodotto prodotto) {
        repository.save(prodotto);
    }
}