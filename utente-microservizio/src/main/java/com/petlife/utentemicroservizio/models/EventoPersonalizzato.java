package com.petlife.utentemicroservizio.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EventoPersonalizzato implements Evento {
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Long id;
   private String testo;
   private Date data;
   @Lob
   @JsonIgnore
   @Type(type="org.hibernate.type.ImageType")
   private byte[] immagine;

   //<editor-fold desc="equals and hashCode" defaultstate="collapsed">
   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      EventoPersonalizzato that = (EventoPersonalizzato) o;
      return id.equals(that.id);
   }

   @Override
   public int hashCode() {
      return Objects.hash(id);
   }
   //</editor-fold>
}
