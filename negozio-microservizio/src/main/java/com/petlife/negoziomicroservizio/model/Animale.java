package com.petlife.negoziomicroservizio.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long id;

    @OneToMany(orphanRemoval = true)
    @JoinTable(name = "animale_ordine", inverseJoinColumns = {@JoinColumn(name = "ordine_id")})
    @ToString.Exclude
    private List<Ordine> ordini = new ArrayList<>();

    public void aggiungiOrdine(Ordine ordine) {
        ordini.add(ordine);
    }

    public void annullaOrdine(Ordine ordine) {
        ordini.remove(ordine);
    }
}
