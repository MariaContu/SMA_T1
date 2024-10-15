package com.sma.T1_SMA;

import java.util.*;
import java.io.FileReader;
import org.yaml.snakeyaml.Yaml;

class SimuladorFila {
    private PriorityQueue<Evento> filaEventos;
    private Random aleatorio = new Random();
    private Map<String, Fila> filas = new HashMap<>();
    private Map<String, Map<String, Double>> transicoes = new HashMap<>();
    private double tempoTotal = 0;
    private int totalEventos = 0;
    private double chegadaExterna; // Variável para armazenar o valor de chegada externa

    public SimuladorFila(Map<String, Fila> filas, double tempoInicial, Map<String, Map<String, Double>> transicoes, double chegadaExterna) {
        this.filas = filas;
        this.filaEventos = new PriorityQueue<>();
        this.tempoTotal = tempoInicial;
        this.transicoes = transicoes;
        this.chegadaExterna = chegadaExterna; // Valor de chegada lido do .yml
    }

    public void executarSimulacao(int maxEventos) {
        // Primeira chegada externa é de Q1, com base no valor lido do .yml
        filaEventos.add(new Evento(tempoTotal, Evento.CHEGADA, "Q1"));

        while (totalEventos < maxEventos && !filaEventos.isEmpty()) {
            Evento eventoAtual = filaEventos.poll();
            tempoTotal = eventoAtual.tempo;
            totalEventos++;

            if (eventoAtual.tipo.equals(Evento.CHEGADA)) {
                Fila filaAtual = filas.get(eventoAtual.fila);
                filaAtual.processarChegada(tempoTotal);

                // Próxima chegada é baseada no valor de "arrivals" do .yml
                double proximaChegada = tempoTotal + chegadaExterna;  // Usando o valor lido do .yml
                filaEventos.add(new Evento(proximaChegada, Evento.CHEGADA, eventoAtual.fila));

                // Tempo de atendimento da fila
                double tempoAtendimento = filaAtual.gerarTempoAtendimento(aleatorio);
                filaEventos.add(new Evento(tempoTotal + tempoAtendimento, Evento.SAIDA, eventoAtual.fila));

            } else if (eventoAtual.tipo.equals(Evento.SAIDA)) {
                Fila filaAtual = filas.get(eventoAtual.fila);
                filaAtual.processarSaida(tempoTotal);

                // Roteamento após saída
                if (transicoes.containsKey(eventoAtual.fila)) {
                    Map<String, Double> probabilidades = transicoes.get(eventoAtual.fila);
                    double probabilidade = aleatorio.nextDouble();
                    double somaProbabilidades = 0.0;

                    for (Map.Entry<String, Double> entry : probabilidades.entrySet()) {
                        somaProbabilidades += entry.getValue();
                        if (probabilidade <= somaProbabilidades) {
                            String destino = entry.getKey();
                            if (!destino.equals("exit")) {  // Verifica se o destino é "exit" (sair do sistema)
                                filaEventos.add(new Evento(tempoTotal, Evento.CHEGADA, destino));
                            }
                            break;
                        }
                    }
                }
            }
        }

        imprimirResultados();
    }

    private void imprimirResultados() {
        System.out.println("Simulação finalizada com " + totalEventos + " eventos.");
        System.out.println("Tempo total da simulação: " + tempoTotal + " minutos.");
        
        for (Map.Entry<String, Fila> entry : filas.entrySet()) {
            String nomeFila = entry.getKey();
            Fila fila = entry.getValue();

            // Índices de desempenho
            double lambda = fila.getClientesAtendidos() / tempoTotal; // Vazão (λ)
            double utilizacao = fila.getTempoOcupado() / tempoTotal; // Utilização (ρ)
            double populacaoMedia = fila.getTotalTempoFila() / tempoTotal; // População média (L)
            double tempoResposta = populacaoMedia / lambda; // Tempo de resposta (W)

            System.out.println("\nFila " + nomeFila + ":");
            System.out.println("Clientes atendidos: " + fila.getClientesAtendidos());
            System.out.println("Clientes perdidos: " + fila.getPerdas());
            System.out.println("Utilização: " + String.format("%.2f", utilizacao * 100) + "%");
            System.out.println("População média: " + String.format("%.2f", populacaoMedia) + " clientes");
            System.out.println("Vazão: " + String.format("%.2f", lambda) + " clientes/minuto");
            System.out.println("Tempo de resposta médio: " + String.format("%.2f", tempoResposta) + " minutos");

            // Probabilidade dos estados
            System.out.println("Probabilidades dos estados da fila:");
            double[] tempoEmCadaEstado = fila.getTempoEmCadaEstado();
            for (int i = 0; i < tempoEmCadaEstado.length; i++) {
                double probabilidadeEstado = (tempoEmCadaEstado[i] / tempoTotal) * 100;
                System.out.println(i + " clientes: " + String.format("%.2f", probabilidadeEstado) + "%");
            }
        }
    }
}
