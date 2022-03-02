package com.petlife.ospedalemicroservizio.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Indirizzo {
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Long id;
   private String citta;
   private String via;
   private int numeroCivico;
   private String interno;

   //getters
   public Long getId() {
      return id;
   }
   public String getCitta() {
      return citta;
   }
   public String getVia() {
      return via;
   }
   public int getNumeroCivico() {
      return numeroCivico;
   }
   public String getInterno() {
      return interno;
   }
   //setters
   public void setId(Long id) {
      this.id = id;
   }
   public void setCitta(String citta) {
      this.citta = citta;
   }
   public void setVia(String via) {
      this.via = via;
   }
   public void setNumeroCivico(int numeroCivico) {
      this.numeroCivico = numeroCivico;
   }
   public void setInterno(String interno) {
      this.interno = interno;
   }

}
