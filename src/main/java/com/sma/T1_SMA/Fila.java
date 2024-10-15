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
    int clientesAtendidos = 0;
    double totalTempoFila = 0.0;
    double[] tempoEmCadaEstado;

    public Fila(int servidores, int capacidade, double minService, double maxService) {
        this.servidores = servidores;
        this.capacidade = capacidade;
        this.minService = minService;
        this.maxService = maxService;
        this.tempoEmCadaEstado = new double[capacidade + 1]; 
    }

    public boolean estaCheia() {
        return clientesNaFila >= capacidade;
    }

    public void processarChegada(double tempoAtual) {
        atualizarTempoEstado(tempoAtual);
        
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
        atualizarTempoEstado(tempoAtual);

        if (clientesNaFila > 0) {
            clientesNaFila--;
            clientesAtendidos++;
            if (clientesNaFila < servidores) {
                tempoOcupado += tempoAtual - ultimoTempoEvento;
            }
        }
        ultimoTempoEvento = tempoAtual;
    }

    public double gerarTempoAtendimento(Random aleatorio) {
        return minService + (maxService - minService) * aleatorio.nextDouble();
    }

    private void atualizarTempoEstado(double tempoAtual) {
        double deltaTempo = tempoAtual - ultimoTempoEvento;
        tempoEmCadaEstado[clientesNaFila] += deltaTempo;
        totalTempoFila += (clientesNaFila * deltaTempo);  
    }

    public int getPerdas() {
        return perdas;
    }

    public double getTempoOcupado() {
        return tempoOcupado;
    }

    public int getClientesAtendidos() {
        return clientesAtendidos;
    }

    public double getTotalTempoFila() {
        return totalTempoFila;
    }

    public double[] getTempoEmCadaEstado() {
        return tempoEmCadaEstado;
    }
}
