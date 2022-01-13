package com.petlife.ospedalemicroservizio.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Struttura {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    private Ospedale ospedale;
    @OneToOne
    private Indirizzo indirizzo;


    //getters
    public Long getId() {
        return id;
    }
    public Ospedale getOspedale() {
        return ospedale;
    }
    public Indirizzo getIndirizzo() {
        return indirizzo;
    }

    //setters
    public void setId(Long id) {
        this.id = id;
    }
    public void setOspedale(Ospedale ospedale) {
        this.ospedale = ospedale;
    }
    public void setIndirizzo(Indirizzo indirizzo) {
        this.indirizzo = indirizzo;
    }

}
