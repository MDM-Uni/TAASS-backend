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
public class Utente {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long id;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "carrello_id", unique = true)
    private Carrello carrello;

    //<editor-fold desc="equals and hashCode" defaultstate=collapsed>
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Utente utente = (Utente) o;
        return id == utente.id && Objects.equals(carrello, utente.carrello);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, carrello);
    }
    //</editor-fold>
}
