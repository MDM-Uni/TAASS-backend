package com.petlife.ospedalemicroservizio.models;

public enum TipoEvento {
    TUTTI(""), VISITA("visita"), ORDINE("ordine");
    final String nome;

    TipoEvento(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}
