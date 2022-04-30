package com.petlife.ospedalemicroservizio;

import com.petlife.ospedalemicroservizio.controllers.OspedaleController;
import com.petlife.ospedalemicroservizio.models.Indirizzo;
import com.petlife.ospedalemicroservizio.models.Ospedale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;

@SpringBootApplication
public class OspedaleMicroservizioApplication {
   Logger logger = LoggerFactory.getLogger(OspedaleMicroservizioApplication.class);

   public static void main(String[] args) {
      SpringApplication.run(OspedaleMicroservizioApplication.class, args);
   }

   @Bean
   public CommandLineRunner atStart(OspedaleController ospedaleController) {
      return (args) -> {
         ResponseEntity<Ospedale> ospedaleResponse = ospedaleController.getInfo();
         Ospedale ospedale = null;
         if (ospedaleResponse.getStatusCode() != HttpStatus.OK) {
            logger.info("Ospedale non trovato, creo un nuovo ospedale");

            ospedale = new Ospedale();
            ospedale.setNome("Ospedale PetLife");
            ospedale.setVisite(new ArrayList<>());
            Indirizzo ind = new Indirizzo();
            ind.setCitta("Torino");
            ind.setVia("Via Rivalta");
            ind.setNumeroCivico(7);
            ospedale.setIndirizzo(ind);
            ospedaleController.createOspedale(ospedale);
         }
         else logger.info("Ospedale già esistente");
      };
   }
}
