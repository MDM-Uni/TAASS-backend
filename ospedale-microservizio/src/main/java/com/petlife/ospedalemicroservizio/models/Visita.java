package com.petlife.ospedalemicroservizio.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Visita implements Evento{
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Long id;
   private LocalDateTime data;
   private Integer durataInMinuti;
   private String note;
   @Enumerated(EnumType.STRING) private TipoVisita tipoVisita;

   //getters and setters
   @Override
   public Long getId() {
      return id;
   }

   public void setData(LocalDateTime data) {
      this.data = data;
   }

   public Integer getDurata() {
      return durataInMinuti;
   }

   public void setDurata(int durata) {
      this.durataInMinuti = durata;
   }

   public String getNote() {
      return note;
   }

   public void setNote(String note) {
      this.note = note;
   }

   public TipoVisita getTipoVisita() {
      return tipoVisita;
   }

   public void setTipoVisita(TipoVisita tipoVisita) {
      this.tipoVisita = tipoVisita;
   }

   @Override
   @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
   public LocalDateTime getData() {
      return data;
   }
}
