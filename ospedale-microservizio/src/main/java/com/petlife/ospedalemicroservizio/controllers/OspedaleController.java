package com.petlife.ospedalemicroservizio.controllers;

import com.petlife.ospedalemicroservizio.models.Animale;
import com.petlife.ospedalemicroservizio.models.Ospedale;
import com.petlife.ospedalemicroservizio.models.Visita;
import com.petlife.ospedalemicroservizio.repositories.IndirizzoRepository;
import com.petlife.ospedalemicroservizio.repositories.OspedaleRepository;
import com.petlife.ospedalemicroservizio.repositories.VisitaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("ospedale")
@CrossOrigin(origins = "*")
public class OspedaleController {
   //singleton
   public static Ospedale ospedale;
   @Autowired
   private OspedaleRepository ospedaleRepository;
   @Autowired
   private VisitaRepository visitaRepository;
   @Autowired private IndirizzoRepository indirizzoRepository;

   final Logger logger = LoggerFactory.getLogger(OspedaleController.class);

   @GetMapping("/getInfo")
   public ResponseEntity<Ospedale> getInfo() {
      if (loadOspedale()) {
         return new ResponseEntity<>(ospedale, HttpStatus.OK);
      }
      else {
         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
   }

   boolean loadOspedale() {
      List<Ospedale> ospedali = ospedaleRepository.findAll();
      try {
         OspedaleController.ospedale = ospedali.get(0);
      }
      catch (IndexOutOfBoundsException e) {
         logger.info("Nessun ospedale trovato");
         return false;
      }
      return true;
   }

   /**
    * per creare le informazioni dell'Ospedale o cambiarle
    * @param osp
    */
   @PostMapping("/pushInfo")
   public ResponseEntity<Void> createOspedale(@RequestBody Ospedale osp) {
      try {
         boolean esisteOspedale = loadOspedale();
         if (!esisteOspedale) { //creazione ex-novo
            ospedale = osp;
            if (osp.getVisite() != null) visitaRepository.saveAll(osp.getVisite());
            else osp.setVisite(new ArrayList<>());
            logger.info("Creazione ospedale ex-novo");
         } else { //aggiornamento
            ospedale.setNome(osp.getNome());
            ospedale.setIndirizzo(osp.getIndirizzo());
            logger.info("Aggiornamento ospedale");
         }
         //in comune con i due rami dell'if
         if (osp.getIndirizzo() != null) indirizzoRepository.save(osp.getIndirizzo());
         ospedaleRepository.save(osp);
         logger.info("createOspedale eseguita con successo");
         return new ResponseEntity<>(HttpStatus.OK);
      } catch (Exception e) {
         logger.info("Errore nella creazione dell'ospedale");
         logger.info(e.getStackTrace().toString());
         return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
   }

   @GetMapping({"/getVisite", "/getVisite/{idVisita}"})
   public ResponseEntity<List<Visita>> getVisite(
         @PathVariable(value = "idVisita") Optional<Long> idVisita,
         @RequestParam(value = "idAnimale") Optional<Long> idAnimale,
         @RequestParam(value = "tipoVisita") Optional<String> tipoVisita
   ) {
      try {
         logger.info("chiamo getVisite");
         loadOspedale();
         Stream<Visita> streamVisite = OspedaleController.ospedale.getVisite().stream();
         //nel caso sia un URL del tipo /getVisite/{idVisita}
         if (idVisita.isPresent()) {
            streamVisite = streamVisite.filter(visita -> idVisita.get().equals(visita.getId()));
            assert (streamVisite.count() <= 1);
         } else { //nel caso sia un URL del tipo /getVisite
            if (idAnimale.isPresent() && idAnimale.get() != 0)
               streamVisite = streamVisite.filter(v -> idAnimale.get().equals(v.getIdAnimale()));
            if (tipoVisita.isPresent() && !tipoVisita.get().equals(""))
               streamVisite = streamVisite.filter(v -> tipoVisita.get().equals(v.getTipoVisita().toString()));
         }
         logger.info("Visite restituite");
         return new ResponseEntity<>(streamVisite.collect(Collectors.toList()), HttpStatus.OK);
      }
      catch (Exception e) {
         logger.error("Errore nella richiesta di visite", e);
         return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
   }

   @PostMapping(value = {"/getVisiteAnimali"})
   public ResponseEntity<List<Visita>> getVisiteAnimali(
         @RequestBody List<Animale> animali,
         @RequestParam(value = "tipoVisita") Optional<String> tipoVisita) {
      try {
         logger.info("chiamo getVisiteAnimali");
         logger.info("animali: " + animali.toString());
         logger.info("tipoVisita: " + tipoVisita.toString());
         loadOspedale();
         Stream<Visita> streamVisite = OspedaleController.ospedale.getVisite().stream();
         streamVisite = streamVisite.filter(visita -> animali.contains(new Animale(visita.getIdAnimale())));
         if (tipoVisita.isPresent() && !tipoVisita.get().equals("")) {
            streamVisite = streamVisite.filter(visita -> tipoVisita.get().equals(visita.getTipoVisita().toString()));
         }
         List<Visita> visite = streamVisite.collect(Collectors.toList());
         logger.info(visite.size() +" visite restituite");
         return new ResponseEntity<>(visite, HttpStatus.OK);
      }
      catch (Exception e) {
         logger.error("Errore nella richiesta di visite", e);
         return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
   }


   @PostMapping("/pushVisita")
   public ResponseEntity<?> createVisita(@RequestBody Visita visita) {
      try {
         logger.info("pushVisita chiamata");
         var minData = LocalDateTime.now();
         if (visita.getData().isBefore(minData)) {
            logger.info("data della visita nel passato");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("data della visita nel passato");
         }
         if (visita.getDurataInMinuti() <= 0){
            logger.info("durata della visita minore o uguale a zero");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("durata della visita minore o uguale a zero");
         }
         loadOspedale();
         OspedaleController.ospedale.getVisite().add(visita);
         Long idVisita = visitaRepository.save(visita).getId();
         logger.info("ID visita: " + idVisita);
         ospedaleRepository.save(OspedaleController.ospedale);
         logger.info("Visita creata");
         return ResponseEntity.ok(idVisita);
      }
      catch (NoSuchElementException e) {
         logger.info("Errore in creazione visita");
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore in creazione visita");
      }
   }

   @PostMapping("/deleteVisita" )
   public ResponseEntity<Void> deleteVisita(@RequestBody Visita visita) {
      try {
         loadOspedale();
         OspedaleController.ospedale.getVisite().remove(visita);
         ospedaleRepository.save(OspedaleController.ospedale);
         visitaRepository.delete(visita);
         logger.info("Visita eliminata");
         return new ResponseEntity<>(HttpStatus.OK);
      }
      catch (NoSuchElementException e) {
         logger.info("Errore in eliminazione visita");
         return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
   }


}
