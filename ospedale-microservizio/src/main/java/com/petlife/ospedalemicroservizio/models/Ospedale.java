package com.petlife.ospedalemicroservizio.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Ospedale {
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Long id;
   private String nome;
   @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
   private List<Visita> visite = new ArrayList<>();
   @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
   private Indirizzo indirizzo;

   //getters
   public Long getId() {
      return id;
   }

   public String getNome() {
      return nome;
   }

   public List<Visita> getVisite() {
      return visite;
   }

   public Indirizzo getIndirizzo() {
      return indirizzo;
   }

   //setters
   public void setId(Long id) {
      this.id = id;
   }

   public void setNome(String nome) {
      this.nome = nome;
   }

   public void setVisite(List<Visita> visite) {
      this.visite = visite;
   }

   public void setIndirizzo(Indirizzo indirizzo) {
      this.indirizzo = indirizzo;
   }

   //<editor-fold desc="equals and hashcode" defaultstate="collapsed">
   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Ospedale ospedale = (Ospedale) o;
      return id.equals(ospedale.id);
   }

   @Override
   public int hashCode() {
      return Objects.hash(id);
   }
   //</editor-fold>
}
