package com.petlife.negoziomicroservizio.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
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
    @JoinColumn(unique = true)
    private Carrello carrello;

    @OneToMany(orphanRemoval = true)
    @JoinTable(name = "utente_ordine", inverseJoinColumns = {@JoinColumn(name = "ordine_id")})
    @ToString.Exclude
    private List<Ordine> ordini = new ArrayList<>();

    @OneToMany(orphanRemoval = true)
    @JoinTable(name = "utente_indirizzo", inverseJoinColumns = {@JoinColumn(name = "indirizzo_id")})
    @ToString.Exclude
    private List<Indirizzo> indirizzi = new ArrayList<>();

    public void aggiungiOrdine(Ordine ordine) {
        ordini.add(ordine);
    }

    public void annullaOrdine(Ordine ordine) {
        ordini.remove(ordine);
    }

    //<editor-fold desc="equals and hashCode" defaultstate="collapsed">
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
