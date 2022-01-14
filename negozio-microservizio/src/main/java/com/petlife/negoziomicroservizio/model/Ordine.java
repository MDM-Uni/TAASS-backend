package com.petlife.negoziomicroservizio.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Ordine {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long id;

    @Temporal(TemporalType.DATE)
    @Column(name = "data_acquisto", nullable = false)
    private Date dataAcquisto;

    @Temporal(TemporalType.DATE)
    @Column(name = "data_consegna")
    private Date dataConsegna;

    @ElementCollection
    @CollectionTable(name = "ordine_prodotto", joinColumns = @JoinColumn(name = "ordine_id"))
    @Column(name = "quantita")
    @MapKeyJoinColumn(name = "prodotto_id")
    private Map<Prodotto, Integer> prodotti = new HashMap<>();

    @OneToOne(optional = false, orphanRemoval = true)
    @JoinColumn(name = "indirizzo_consegna_id", nullable = false)
    private Indirizzo indirizzoConsegna;

    public int getNumeroArticoli() {
        return prodotti.size();
    }

    public double getTotale() {
        return prodotti.entrySet().parallelStream().mapToDouble(e -> e.getKey().getPrezzo() * e.getValue()).sum();
    }

    //<editor-fold desc="equals and hashCode" defaultstate=collapsed>
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ordine ordine = (Ordine) o;
        return id == ordine.id && dataAcquisto.equals(ordine.dataAcquisto) && Objects.equals(dataConsegna, ordine.dataConsegna) && prodotti.equals(ordine.prodotti) && indirizzoConsegna.equals(ordine.indirizzoConsegna);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dataAcquisto, dataConsegna, prodotti, indirizzoConsegna);
    }
    //</editor-fold>
}
