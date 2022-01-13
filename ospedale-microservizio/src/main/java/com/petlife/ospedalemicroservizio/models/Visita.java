package com.petlife.ospedalemicroservizio.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Visita implements Evento{
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Long id;
   private LocalDateTime data;
   private Duration durata;
   private String note;
   @Enumerated(EnumType.STRING)
   private TipoVisita tipoVisita;

   @Override
   public Long getId() {
      return id;
   }

   @Override
   public LocalDateTime getData() {
      return data;
   }
}
