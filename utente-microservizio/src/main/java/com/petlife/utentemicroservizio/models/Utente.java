package com.petlife.utentemicroservizio.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name="Utente")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Utente implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "nome",nullable = false)
    private String nome;
    @NotNull
    @Column(name = "email",nullable = false)
    private String email;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Animale> animali;

    public Utente(String nome, String email, List<Animale> animali) {
        this.nome = nome;
        this.email = email;
        this.animali = animali;
    }

    public Utente(){}

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Animale> getAnimali() {
        return animali;
    }

    public void setAnimali(List<Animale> animali) {
        this.animali = animali;
    }

    public void addAnimale(Animale animale){
        this.animali.add(animale);
    }

    public void removeAnimale(Animale animale){
        this.animali.remove(animale);
    }

    public Optional<Animale> getAnimale(Long idAnimale) {
        return animali.stream().filter(animale -> animale.getId().equals(idAnimale)).findFirst();
    }
}
