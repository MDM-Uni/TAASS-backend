package com.petlife.utentemicroservizio.controllers;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.petlife.utentemicroservizio.models.Animale;
import com.petlife.utentemicroservizio.models.Evento;
import com.petlife.utentemicroservizio.models.EventoPersonalizzato;
import com.petlife.utentemicroservizio.repositories.AnimaleRepository;
import com.petlife.utentemicroservizio.repositories.EventoPersonalizzatoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/storia")
@CrossOrigin(origins = "*")
public class StoriaController {
   @Autowired
   private AnimaleRepository animaleRepository;
   @Autowired
   private EventoPersonalizzatoRepository eventoPersonalizzatoRepository;

   final Logger logger = LoggerFactory.getLogger(this.getClass());

   @GetMapping("/getStoria/{idAnimale}")
   public ResponseEntity<List<Evento>> getStoria(@PathVariable("idAnimale") long idAnimale) {
      logger.info("chiamo getStoria");
      Optional<Animale> animale = animaleRepository.findById(idAnimale);

      if (animale.isPresent()) {
         List<EventoPersonalizzato> eventiPersonalizzati = animale.get().getStoria();
         List<Evento> eventi = new ArrayList<>(eventiPersonalizzati);
         logger.info("Storia restituita");
         return ResponseEntity.ok(eventi);
      } else {
         logger.info("animale non trovato");
         return ResponseEntity.internalServerError().build();
      }
   }

   @PostMapping(value ="/pushEventoPersonalizzato/{idAnimale}", consumes = { "multipart/form-data" })
   public ResponseEntity<Long> postEventoPersonalizzato(
         @PathVariable("idAnimale") long idAnimale,
         @RequestParam("testo") String testo,
         @RequestParam("data")
         @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
         @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
         Date data,
         @ModelAttribute MultipartFile immagine) throws IOException {
      logger.info("chiamo postEventoPersonalizzato");
      logger.info("idAnimale: " + idAnimale + "\n" +
            "testo: " + testo + "\n" +
            "data: " + data + "\n" +
            "immagine nulla: " + (immagine==null));
      Optional<Animale> animale = animaleRepository.findById(idAnimale);

      if (animale.isPresent()) {
         EventoPersonalizzato eventoPersonalizzato = new EventoPersonalizzato();
         eventoPersonalizzato.setTesto(testo);
         eventoPersonalizzato.setData(data);
         if (immagine != null) {
            eventoPersonalizzato.setImmagine(immagine.getBytes());
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

   @GetMapping(value="/getImmagineEventoPersonalizzato/{idEvento}")
   public ResponseEntity<byte[]> getImmagineEventoPersonalizzato(@PathVariable("idEvento") long idEvento) {
      logger.info("chiamo getImmagineEventoPersonalizzato");
      Optional<EventoPersonalizzato> eventoPersonalizzato = eventoPersonalizzatoRepository.findById(idEvento);

      if (eventoPersonalizzato.isPresent()) {
         logger.info("immagine restituita");
         return ResponseEntity.ok(eventoPersonalizzato.get().getImmagine());
      } else {
         logger.info("eventoPersonalizzato non trovato");
         return ResponseEntity.internalServerError().build();
      }
   }
}
