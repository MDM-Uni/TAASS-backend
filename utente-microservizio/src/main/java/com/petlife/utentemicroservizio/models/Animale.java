package com.petlife.utentemicroservizio.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@ToString
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
@AllArgsConstructor
public class Animale implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name="nome", nullable = false)
    private String nome;
    @Column(name = "dataNascita")
    private Date dataDiNascita;
    @ElementCollection(fetch=FetchType.EAGER)
    @CollectionTable(name = "patologie")
    private List<String> patologie;
    @Column(name = "razza")
    private String razza;
    @Column(name = "peso")
    private Float peso;
    @Column(name = "peloLungo")
    private boolean peloLungo; //0 se corto
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<EventoPersonalizzato> storia;

    //<editor-fold desc="equals and hashcode" defaultstate="collapsed">
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animale animale = (Animale) o;
        return id.equals(animale.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    //</editor-fold>
    public void addEventoAllaStoria(EventoPersonalizzato eventoPersonalizzato) {
        storia.add(eventoPersonalizzato);
    }
}
