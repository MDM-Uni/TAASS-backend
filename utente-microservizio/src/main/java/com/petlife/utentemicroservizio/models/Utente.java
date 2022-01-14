package com.petlife.utentemicroservizio.models;

public class Utente {

    private Long id;
    private String nome;
    private String cognome;
    private String password;
    private String email;

    public Utente(Long id, String nome, String cognome, String password, String email) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.password = password;
        this.email = email;
    }

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
}
