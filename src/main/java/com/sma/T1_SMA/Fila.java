package com.sma.T1_SMA;

import java.util.Random;

class Fila {
    int servidores;
    int capacidade;
    double minService;
    double maxService;
    int clientesNaFila = 0;
    int perdas = 0;
    double tempoOcupado = 0.0;
    double ultimoTempoEvento = 0.0;

    public Fila(int servidores, int capacidade, double minService, double maxService) {
        this.servidores = servidores;
        this.capacidade = capacidade;
        this.minService = minService;
        this.maxService = maxService;
    }

    public boolean estaCheia() {
        return clientesNaFila >= capacidade;
    }

    public void processarChegada(double tempoAtual) {
        if (clientesNaFila < servidores) {
            tempoOcupado += tempoAtual - ultimoTempoEvento;
        }
        if (!estaCheia()) {
            clientesNaFila++;
        } else {
            perdas++;
        }
        ultimoTempoEvento = tempoAtual;
    }

    public void processarSaida(double tempoAtual) {
        if (clientesNaFila > 0) {
            clientesNaFila--;
            if (clientesNaFila < servidores) {
                tempoOcupado += tempoAtual - ultimoTempoEvento;
            }
            ultimoTempoEvento = tempoAtual;
        }
    }

    public double gerarTempoAtendimento(Random aleatorio) {
        return minService + (maxService - minService) * aleatorio.nextDouble();
    }

    public int getPerdas() {
        return perdas;
    }

    public double getTempoOcupado() {
        return tempoOcupado;
    }
}
