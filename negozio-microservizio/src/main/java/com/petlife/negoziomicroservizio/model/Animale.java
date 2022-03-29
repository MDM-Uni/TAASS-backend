package com.petlife.negoziomicroservizio.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "animale")
public class Animale {
    @Id
    @Column(name = "id", nullable = false)
    private long id;

    @OneToMany(orphanRemoval = true)
    @JoinTable(name = "animale_ordine", inverseJoinColumns = {@JoinColumn(name = "ordine_id")})
    @ToString.Exclude
    private List<Ordine> ordini = new ArrayList<>();

    public Animale(long id) {
        this.id = id;
    }

    public void aggiungiOrdine(Ordine ordine) {
        ordini.add(ordine);
    }

    public void annullaOrdine(Ordine ordine) {
        ordini.remove(ordine);
    }
}
