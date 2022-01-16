package com.petlife.utentemicroservizio.models;

import java.nio.FloatBuffer;
import java.util.Date;
import java.util.List;

public class Animale {

    private Long id;
    private String nome;
    private Date dataDiNascita;
    private List<String> patologie;
    private String razza;
    private Float peso;
    private boolean peloLungo; //0 se corto

    public Animale(Long id, String nome, Date dataDiNascita, List<String> patologie, String razza, Float peso, boolean peloLungo) {
        this.id = id;
        this.nome = nome;
        this.dataDiNascita = dataDiNascita;
        this.patologie = patologie;
        this.razza = razza;
        this.peso = peso;
        this.peloLungo = peloLungo;
    }

    public String getNome() {
        return nome;
    }

    public Date getDataDiNascita() {
        return dataDiNascita;
    }

    public List<String> getPatologie() {
        return patologie;
    }

    public String getRazza() {
        return razza;
    }

    public Float getPeso() {
        return peso;
    }

    public boolean getPeloLungo() {
        return peloLungo;
    }

    public void setPatologie(List<String> patologie) {
        this.patologie = patologie;
    }

    public void setPeso(Float peso) {
        this.peso = peso;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDataDiNascita(Date dataDiNascita) {
        this.dataDiNascita = dataDiNascita;
    }

    public void setRazza(String razza) {
        this.razza = razza;
    }

    public void setPeloLungo(boolean peloLungo) {
        this.peloLungo = peloLungo;
    }



}
