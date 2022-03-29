package com.petlife.negoziomicroservizio.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
    @JoinTable(name = "utente_indirizzo", inverseJoinColumns = {@JoinColumn(name = "indirizzo_id")})
    @ToString.Exclude
    private List<Indirizzo> indirizzi = new ArrayList<>();

    @OneToMany(orphanRemoval = true)
    @JoinTable(name = "utente_animale", inverseJoinColumns = {@JoinColumn(name = "animale_id")})
    @ToString.Exclude
    private List<Animale> animali = new ArrayList<>();

    public boolean haAnimale(Animale animale) {
        return animali.contains(animale);
    }

    public void aggiungiAnimale(Animale animale) {
        this.animali.add(animale);
    }

    public List<Map<String,Object>> getOrdini() {
        return animali.stream()
                .flatMap(animale -> animale.getOrdini().stream()
                        .map(ordine -> Map.of("animale", animale, "ordine", ordine)))
                .collect(Collectors.toList());
    }

    public void aggiungiIndirizzo(Indirizzo indirizzo) {
        indirizzi.add(indirizzo);
    }

    public void rimuoviIndirizzo(Indirizzo indirizzo) {
        indirizzi.remove(indirizzo);
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
