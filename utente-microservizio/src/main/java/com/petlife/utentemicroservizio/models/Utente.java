package com.petlife.utentemicroservizio.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Utente")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Utente implements Evento{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String nome;
    @JsonIgnore
    private String password;
    @NotNull
    private String email;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Animale> animali;

    public Utente(String nome, String email, List<Animale> animali) {
        this.nome = nome;
        this.email = email;
        this.animali = animali;
    }

    public Utente(){}

    @Override
    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }


    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
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

}
