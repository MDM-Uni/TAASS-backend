package com.petlife.utentemicroservizio.controllers;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.petlife.utentemicroservizio.models.Animale;
import com.petlife.utentemicroservizio.models.EventoPersonalizzato;
import com.petlife.utentemicroservizio.repositories.AnimaleRepository;
import com.petlife.utentemicroservizio.repositories.EventoPersonalizzatoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/storia")
@CrossOrigin(origins = "http://localhost:4200")
public class StoriaController {
   @Autowired
   private AnimaleRepository animaleRepository;
   @Autowired
   private EventoPersonalizzatoRepository eventoPersonalizzatoRepository;

   final Logger logger = LoggerFactory.getLogger(this.getClass());

   @GetMapping(value = {"/getStoria", "/getStoria/{idAnimale}"})
   public ResponseEntity<List<EventoPersonalizzato>> getStoria(
         @PathVariable("idAnimale") Optional<Long> idAnimale) {
      logger.info("chiamo getStoria");
      List<EventoPersonalizzato> eventiPersonalizzati = new ArrayList<>();
      if (idAnimale.isPresent()) {
         logger.info("chiamo getStoria specificando idAnimale");
         Optional<Animale> animale = animaleRepository.findById(idAnimale.get());
         if (animale.isPresent()) {
            eventiPersonalizzati = animale.get().getStoria();
         } else {
            logger.info("animale non trovato");
            return ResponseEntity.internalServerError().build();
         }
      } else {
         logger.info("chiamo getStoria senza idAnimale");
         eventiPersonalizzati = new ArrayList<>();
         for (Animale animale : animaleRepository.findAll()) {
            eventiPersonalizzati.addAll(animale.getStoria());
         }
      }
      logger.info("eventi personalizzati restituiti");
      return ResponseEntity.ok(eventiPersonalizzati);
   }

   @PostMapping(value = "/pushEventoPersonalizzato/{idAnimale}", consumes = {"multipart/form-data"})
   public ResponseEntity<Long> postEventoPersonalizzato(
         @PathVariable("idAnimale") long idAnimale,
         @RequestParam("testo") String testo,
         @RequestParam("data")
         @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
         @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
               Date data,
         @ModelAttribute("immagine") Optional<MultipartFile> immagine) throws IOException {
      logger.info("chiamo postEventoPersonalizzato");
      logger.info("idAnimale: " + idAnimale + "\n" +
            "testo: " + testo + "\n" +
            "data: " + data + "\n" +
            "immagine nulla: " + (immagine.isEmpty()));
      Optional<Animale> animale = animaleRepository.findById(idAnimale);

      if (animale.isPresent()) {
         EventoPersonalizzato eventoPersonalizzato = new EventoPersonalizzato();
         eventoPersonalizzato.setTesto(testo);
         eventoPersonalizzato.setData(data);
         eventoPersonalizzato.setIdAnimale(idAnimale);
         if (immagine.isPresent()) {
            eventoPersonalizzato.setImmagine(immagine.get().getBytes());
         } else logger.info("immagine nulla");
         eventoPersonalizzatoRepository.save(eventoPersonalizzato);

         Animale animale_ = animale.get();
         animale_.addEventoAllaStoria(eventoPersonalizzato);
         animaleRepository.save(animale_);

         logger.info("eventoPersonalizzato inserito");
         return ResponseEntity.ok(eventoPersonalizzato.getId());
      } else {
         logger.info("animale non trovato");
         return ResponseEntity.internalServerError().build();
      }
   }

   @PostMapping(value = "/deleteEventoPersonalizzato") // url: http://localhost:8081/storia/deleteEventoPersonalizzato
   public ResponseEntity<Void> deleteEventoPersonalizzato(@RequestBody EventoPersonalizzato eventoPersonalizzato) {
      logger.info("chiamo deleteEventoPersonalizzato");
      Optional<EventoPersonalizzato> eventoDaEliminare = eventoPersonalizzatoRepository.findById(eventoPersonalizzato.getId());
      if (eventoDaEliminare.isPresent()) {
         Optional<Animale> animale = animaleRepository.findById(eventoDaEliminare.get().getIdAnimale());
         if (animale.isPresent()) {
            Animale animale_ = animale.get();
            animale_.getStoria().remove(eventoPersonalizzato);
            animaleRepository.save(animale_);
            eventoPersonalizzatoRepository.delete(eventoPersonalizzato);
            logger.info("eventoPersonalizzato eliminato");
            return ResponseEntity.ok().build();
         } else {
            logger.info("animale non trovato");
            return ResponseEntity.internalServerError().build();
         }
      } else {
         logger.info("eventoPersonalizzato non trovato");
         return ResponseEntity.notFound().build();
      }
   }

   @ResponseBody
   @GetMapping(value = "/getImmagineEventoPersonalizzato/{idEvento}", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
   public ResponseEntity<byte[]> getImmagineEventoPersonalizzato(@PathVariable("idEvento") long idEvento) {
      logger.info("chiamo getImmagineEventoPersonalizzato");
      Optional<EventoPersonalizzato> eventoPersonalizzato = eventoPersonalizzatoRepository.findById(idEvento);

      if (eventoPersonalizzato.isPresent()) {
         if (eventoPersonalizzato.get().getImmagine() != null) {
            logger.info("immagine restituita");
            return ResponseEntity.ok(eventoPersonalizzato.get().getImmagine());
         } else {
            logger.info("immagine non presente");
            return ResponseEntity.notFound().build();
         }
      } else {
         logger.info("eventoPersonalizzato non trovato");
         return ResponseEntity.internalServerError().build();
      }
   }
}
