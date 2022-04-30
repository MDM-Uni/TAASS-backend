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
public class Negozio {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @OneToMany(orphanRemoval = true)
    @JoinTable(name = "negozio_prodotto", inverseJoinColumns = {@JoinColumn(name = "prodotto_id")})
    @ToString.Exclude
    private List<Prodotto> prodotti = new ArrayList<>();

    @OneToMany(orphanRemoval = true)
    @JoinTable(name = "negozio_ordine", inverseJoinColumns = {@JoinColumn(name = "ordine_id")})
    @ToString.Exclude
    private List<Ordine> ordini = new ArrayList<>();

    //<editor-fold desc="equals and hashCode" defaultstate="collapsed">
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Negozio negozio = (Negozio) o;
        return id == negozio.id && nome.equals(negozio.nome) && prodotti.equals(negozio.prodotti) && ordini.equals(negozio.ordini);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, prodotti, ordini);
    }
    //</editor-fold>
}
