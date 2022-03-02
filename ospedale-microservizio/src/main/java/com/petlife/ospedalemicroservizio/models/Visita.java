package com.petlife.ospedalemicroservizio.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Visita implements Evento{
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Long id;
   private LocalDateTime data;
   private Integer durataInMinuti;
   private String note;
   @Enumerated(EnumType.STRING) private TipoVisita tipoVisita;
   private Long idAnimale;

   //getters and setters
   @Override
   public Long getId() {
      return id;
   }

   public void setData(LocalDateTime data) {
      this.data = data;
   }

   public Integer getDurataInMinuti() {
      return durataInMinuti;
   }

   public void setDurataInMinuti(int durata) {
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

   public Long getIdAnimale() {
      return idAnimale;
   }

   public void setIdAnimale(Long idAnimale) {
      this.idAnimale = idAnimale;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Visita visita = (Visita) o;
      return id.equals(visita.id);
   }

   @Override
   public int hashCode() {
      return Objects.hash(id);
   }

   @Override
   public LocalDateTime getData() {
      return data;
   }
}
