# Simulador de Rede de Filas

Este projeto implementa um simulador de uma rede de filas utilizando Java e Spring Boot. O simulador lê as configurações de um arquivo YAML e executa uma simulação de eventos em filas.

## Requisitos

- Java 21 ou superior
- Maven 3.6 ou superior

## Como Rodar o Simulador

1. **Clone o Repositório**:
   ```bash
   git clone <URL_DO_REPOSITORIO>
   cd T1_SMA
   ```

2. **Compile o Projeto**:
   Execute o comando abaixo para compilar o projeto:
   ```bash
   mvn clean compile
   ```

3. **Execute a Aplicação**:
   Para rodar o simulador, execute:
   ```bash
   mvn spring-boot:run
   ```

4. **Resultados**:
   Após a execução, os resultados da simulação serão exibidos no console, incluindo o número de clientes perdidos e o tempo ocupado para cada fila.

## Modificando os Valores de Configuração

As configurações do simulador são definidas no arquivo `config.yml`, que está localizado na pasta `src/main/resources`. Você pode modificar os seguintes parâmetros:

### Exemplo de `config.yml`

```yaml
arrivals: 
   Q1: 2.0  # Tempo de chegada do primeiro cliente

queues: 
   Q1: 
      servers: 1
      minArrival: 2.0
      maxArrival: 4.0
      minService: 1.0
      maxService: 2.0
   Q2: 
      servers: 2
      capacity: 5  # Ajuste este valor se necessário
      minService: 4.0
      maxService: 8.0
   Q3: 
      servers: 2
      capacity: 10  # Ajuste este valor se necessário
      minService: 5.0
      maxService: 15.0

network: 
-  source: Q1
   target: Q2
   probability: 0.8  # 80% dos clientes vão para Q2
-  source: Q1
   target: Q3
   probability: 0.2  # 20% dos clientes vão para Q3

rndnumbers: 
- 0.2176
- 0.0103
- 0.1109
- 0.3456
- 0.9910
- 0.2323
- 0.9211
- 0.0322
- 0.1211
- 0.5131
- 0.7208
- 0.9172
- 0.9922
- 0.8324
- 0.5011
- 0.2931

rndnumbersPerSeed: 100000
seeds: 
- 1
- 2
- 3
- 4
- 5
```

### Parâmetros a Modificar

- **Chegadas**: O tempo de chegada do primeiro cliente pode ser alterado na seção `arrivals`.
- **Filas**: Para cada fila, você pode ajustar:
  - **`servers`**: Número de servidores disponíveis.
  - **`capacity`**: Capacidade máxima da fila.
  - **`minService`** e **`maxService`**: Tempos de serviço mínimo e máximo.
- **Rede de Filas**: Ajuste as probabilidades de transição na seção `network` para controlar o fluxo de clientes entre as filas.
