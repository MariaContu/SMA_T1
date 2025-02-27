package com.sma.T1_SMA;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.yaml.snakeyaml.Yaml;

import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class T1SmaApplication {

    public static void main(String[] args) {
        SpringApplication.run(T1SmaApplication.class, args);
        executarSimulacao();
    }

    private static void executarSimulacao() {
        Yaml yaml = new Yaml();
        try {
            // Leitura do arquivo YAML
            FileReader reader = new FileReader("src/main/resources/config.yml");
            Map<String, Object> data = yaml.load(reader);

            // Lendo o tempo de chegada do exterior (campo "arrivals" do .yml)
            double chegadaQ1 = (double) ((Map<String, Object>) data.get("arrivals")).get("Q1");
        
            // Configurando as filas
            Map<String, Fila> filas = new HashMap<>();
            Map<String, Map<String, Object>> filasConfig = (Map<String, Map<String, Object>>) data.get("queues");
            for (Map.Entry<String, Map<String, Object>> entry : filasConfig.entrySet()) {
                String key = entry.getKey();
                Map<String, Object> config = entry.getValue();
                filas.put(key, new Fila(
                    (int) config.get("servers"),
                    config.containsKey("capacity") ? (int) config.get("capacity") : Integer.MAX_VALUE,
                    (double) config.get("minService"),
                    (double) config.get("maxService")
                ));
            }

            // Configurando as transições de rede (roteamento)
            Map<String, Map<String, Double>> transicoes = new HashMap<>();
            List<Map<String, Object>> networkConfig = (List<Map<String, Object>>) data.get("network");
            for (Map<String, Object> transition : networkConfig) {
                String source = (String) transition.get("source");
                String target = (String) transition.get("target");
                double probability = (double) transition.get("probability");

                transicoes.putIfAbsent(source, new HashMap<>());
                transicoes.get(source).put(target, probability);
            }

            // Inicializando o simulador com as filas, transições e o tempo de chegada
            SimuladorFila simulador = new SimuladorFila(filas, chegadaQ1, transicoes, chegadaQ1);
            simulador.executarSimulacao(100000); // Simulando 100.000 eventos

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
