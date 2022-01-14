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
@Table(name = "prodotto")
public class Prodotto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "prezzo", nullable = false)
    private double prezzo;

    @Column(name = "categoria")
    private String categoria;

    //<editor-fold desc="equals and hashCode" defaultstate="collapsed">
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prodotto prodotto = (Prodotto) o;
        return id == prodotto.id && Double.compare(prodotto.prezzo, prezzo) == 0 && nome.equals(prodotto.nome) && Objects.equals(categoria, prodotto.categoria);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, prezzo, categoria);
    }
    //</editor-fold>
}
