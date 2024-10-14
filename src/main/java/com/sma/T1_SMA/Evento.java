package com.sma.T1_SMA;

import java.util.*;

class Evento implements Comparable<Evento> {
    static final String CHEGADA = "chegada";
    static final String SAIDA = "saida";

    double tempo;
    String tipo;
    String fila;

    public Evento(double tempo, String tipo, String fila) {
        this.tempo = tempo;
        this.tipo = tipo;
        this.fila = fila;
    }

    @Override
    public int compareTo(Evento outro) {
        return Double.compare(this.tempo, outro.tempo);
    }
}
