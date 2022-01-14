package com.petlife.negoziomicroservizio.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Indirizzo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "citta", nullable = false)
    private String citta;

    @Column(name = "via", nullable = false)
    private String via;

    @Column(name = "numero_civico", nullable = false)
    private int numeroCivico;

    @Column(name = "interno")
    private String interno;

    //<editor-fold desc="equals and hashCode" defaultstate=collapsed>
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Indirizzo indirizzo = (Indirizzo) o;
        return id == indirizzo.id && numeroCivico == indirizzo.numeroCivico && citta.equals(indirizzo.citta) && via.equals(indirizzo.via) && Objects.equals(interno, indirizzo.interno);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, citta, via, numeroCivico, interno);
    }
    //</editor-fold>
}
