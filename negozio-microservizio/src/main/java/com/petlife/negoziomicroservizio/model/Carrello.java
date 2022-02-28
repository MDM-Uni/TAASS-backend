package com.petlife.negoziomicroservizio.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "carrello")
public class Carrello {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long id;

    @ElementCollection
    @CollectionTable(name = "carrello_prodotto", joinColumns = @JoinColumn(name = "carrello_id"))
    @Column(name = "quantita")
    @MapKeyJoinColumn(name = "prodotto_id")
    private Map<Prodotto, Integer> prodotti = new HashMap<>();

    public int getNumeroArticoli() {
        return prodotti.size();
    }

    public double getTotale() {
        return prodotti.entrySet().parallelStream().mapToDouble(e -> e.getKey().getPrezzo() * e.getValue()).sum();
    }

    public void aggiungiProdotto(Prodotto prodotto, int quantita) {
        int numProdotto = prodotti.getOrDefault(prodotto, 0);
        prodotti.put(prodotto, numProdotto + quantita);
    }

    public boolean rimuoviProdotto(Prodotto prodotto, int quantita) {
        if (!prodotti.containsKey(prodotto))
            return false;
        else {
            int numProdotto = prodotti.get(prodotto);
            if (numProdotto <= quantita)
                prodotti.remove(prodotto);
            else
                prodotti.put(prodotto, numProdotto - quantita);
            return true;
        }
    }

    //<editor-fold desc="equals and hashCode" defaultstate="collapsed">
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Carrello carrello = (Carrello) o;
        return id == carrello.id && prodotti.equals(carrello.prodotti);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, prodotti);
    }
    //</editor-fold>
}
