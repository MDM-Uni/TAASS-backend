package com.petlife.negoziomicroservizio;

import com.petlife.negoziomicroservizio.controller.ProdottoController;
import com.petlife.negoziomicroservizio.model.Prodotto;
import com.petlife.negoziomicroservizio.repo.ProdottoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@SpringBootApplication
public class NegozioMicroservizioApplication {
   Logger logger = LoggerFactory.getLogger(NegozioMicroservizioApplication.class);

   public static void main(String[] args) {
      SpringApplication.run(NegozioMicroservizioApplication.class, args);
   }

   @Bean
   public CommandLineRunner atStart(ProdottoController prodottoController, ProdottoRepository prodottoRepository) {
      return (args) -> {
         ResponseEntity<List<Prodotto>> resp = prodottoController.getAllProdotti();
         if (resp.getStatusCode() != HttpStatus.OK) {
            logger.error("Errore nel controllo dei prodotti");
            return;
         }
         List<Prodotto> prodotti = resp.getBody();
         if (prodotti == null) {
            logger.error("Lista prodotti nulla");
            return;
         }
         logger.info("Presenti " + prodotti.size() + " prodotti. Aggiungo quelli nuovi, se presenti");

         BufferedReader br = Files.newBufferedReader(Path.of("products.csv"), StandardCharsets.UTF_8);
         String line;
         int prodottiAggiunti = 0;
         while ((line = br.readLine()) != null) {
            String[] elems = line.trim().split(",");
            byte[] img = Files.readAllBytes(Path.of("img/" + elems[3] + ".jpg"));
            Prodotto prodotto = new Prodotto();
            prodotto.setNome(elems[0]);
            prodotto.setPrezzo(Double.parseDouble(elems[1]));
            prodotto.setCategoria(elems[2]);
            prodotto.setImmagine(img);
            boolean giaPresente = prodotti.parallelStream().anyMatch(p ->
                    p.getNome().equals(prodotto.getNome())
                            && p.getPrezzo() == prodotto.getPrezzo()
                            && p.getCategoria().equals(prodotto.getCategoria())); // se già presente non viene aggiunto
            if (!giaPresente) {
               logger.info("Salvato: " + prodottoRepository.save(prodotto));
               prodottiAggiunti++;
            }
         }
         logger.info("Inizizalizzazione prodotti completata: aggiunti " + prodottiAggiunti + " prodotti");
      };
   }
}
