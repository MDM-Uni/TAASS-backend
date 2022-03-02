package com.petlife.utentemicroservizio.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Animale implements Evento, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name="nome")
    private String nome;
    @Column(name = "dataNascita")
    private Date dataDiNascita;
    @ElementCollection(fetch=FetchType.EAGER)
    @CollectionTable(name = "patologie")
    private List<String> patologie;
    private String razza;
    private Float peso;
    private boolean peloLungo; //0 se corto


    public Animale(String nome) {
        this.nome = nome;
    }

    public Animale(String nome, Date dataDiNascita, List<String> patologie, String razza, Float peso, boolean peloLungo) {
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


    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Animale{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", dataDiNascita=" + dataDiNascita +
                ", patologie=" + patologie +
                ", razza='" + razza + '\'' +
                ", peso=" + peso +
                ", peloLungo=" + peloLungo +
                '}';
    }
}
