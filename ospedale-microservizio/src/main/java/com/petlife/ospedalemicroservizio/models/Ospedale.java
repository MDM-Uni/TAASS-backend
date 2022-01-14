package com.petlife.ospedalemicroservizio.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Ospedale {
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Long id;
   private String nome;
   @OneToMany private List<Visita> visite = new ArrayList<>();
   @OneToOne private Indirizzo indirizzo;

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

}
