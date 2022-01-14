package com.petlife.utentemicroservizio.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Utente")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Utente implements Evento{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private String cognome;
    @JsonIgnore
    private String password;
    @NotNull
    private String email;
    @NotNull
    private String providerId;
    @Transient
    private List<Animale> animali = new ArrayList<>();

    @Override
    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
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

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }
}
