package com.sma.T1_SMA;

import java.util.*;
import java.io.FileReader;
import org.yaml.snakeyaml.Yaml;

class SimuladorFila {
    private PriorityQueue<Evento> filaEventos;
    private Random aleatorio = new Random();
    private Map<String, Fila> filas = new HashMap<>();
    private double tempoTotal = 0;
    private int totalEventos = 0;

    public SimuladorFila(Map<String, Fila> filas, double tempoInicial) {
        this.filas = filas;
        this.filaEventos = new PriorityQueue<>();
        this.tempoTotal = tempoInicial;
    }

    public void executarSimulacao(int maxEventos) {
        
        filaEventos.add(new Evento(tempoTotal, Evento.CHEGADA, "Q1"));

        while (totalEventos < maxEventos && !filaEventos.isEmpty()) {
            Evento eventoAtual = filaEventos.poll();
            tempoTotal = eventoAtual.tempo;
            totalEventos++;

            if (eventoAtual.tipo.equals(Evento.CHEGADA)) {
                Fila filaAtual = filas.get(eventoAtual.fila);
                filaAtual.processarChegada(tempoTotal);

                // Agendar próximo evento de chegada
                double proximaChegada = tempoTotal + 2.0; // Exemplo fixo, pode ser dinâmico
                filaEventos.add(new Evento(proximaChegada, Evento.CHEGADA, eventoAtual.fila));

                // Agendar tempo de atendimento
                double tempoAtendimento = filaAtual.gerarTempoAtendimento(aleatorio);
                filaEventos.add(new Evento(tempoTotal + tempoAtendimento, Evento.SAIDA, eventoAtual.fila));

            } else if (eventoAtual.tipo.equals(Evento.SAIDA)) {
                Fila filaAtual = filas.get(eventoAtual.fila);
                filaAtual.processarSaida(tempoTotal);
            }
        }

        imprimirResultados();
    }

    private void imprimirResultados() {
        System.out.println("Simulação finalizada com " + totalEventos + " eventos.");
        System.out.println("Tempo total da simulação: " + tempoTotal + " minutos.");
        for (Map.Entry<String, Fila> entry : filas.entrySet()) {
            String key = entry.getKey();
            Fila fila = entry.getValue();
            System.out.println("Fila " + key + " - Clientes perdidos: " + fila.getPerdas());
            System.out.println("Fila " + key + " - Tempo ocupado: " + fila.getTempoOcupado() + " minutos.");
        }
    }
}
