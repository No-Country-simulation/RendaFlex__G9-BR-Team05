# Arquitetura inicial do Back-end — RendaFlex

## 1. Objetivo

Documentar a arquitetura inicial do Back-end em Java com Spring Boot.

```text
Frontend React + TypeScript
        ↓
API pública Spring Boot
        ↓
Serviço interno Python
        ↓
Modelos de Machine Learning
```

Esta versão registra somente decisões estáveis. A proposta de ajuste enviada no canal `project_manager` permanece em avaliação e não é tratada como aprovada.

## 2. Responsabilidades do Back-end

O Back-end deverá:

- receber e validar requisições da API pública;
- aplicar regras de negócio;
- calcular as features definidas no contrato aprovado;
- orquestrar a comunicação com o serviço Python;
- tratar erros internos e de integração;
- montar as respostas públicas;
- impedir a exposição de detalhes internos.

O Back-end não deverá:

- treinar modelos;
- permitir acesso direto do Front-end ao serviço Python;
- expor notebooks, artefatos serializados ou detalhes de infraestrutura.

## 3. Operações públicas

| Operação | Método e rota | Responsabilidade |
|---|---|---|
| Análise financeira | `POST /api/v1/financial-analyses` | Validar o contexto, classificar despesas, calcular indicadores e retornar o perfil |
| Simulação de despesa | `POST /api/v1/expense-simulations` | Comparar o cenário atual com o cenário projetado |
| Classificação de transações | `POST /api/v1/transactions/classify` | Classificar descrições elegíveis e consolidar valores por categoria |

## 4. Camadas propostas

### Controller

Classes:

```text
FinancialAnalysisController
ExpenseSimulationController
TransactionClassificationController
```

Responsabilidades:

- receber payloads;
- acionar validações;
- chamar os serviços;
- retornar códigos HTTP;
- não conter regras financeiras.

### DTO

```text
dto/
├── request/
├── response/
└── internal/
```

- `request`: entrada pública;
- `response`: saída pública;
- `internal`: comunicação entre Spring Boot e Python.

### Service

```text
FinancialAnalysisService
ExpenseSimulationService
TransactionClassificationService
```

Responsabilidades:

- coordenar casos de uso;
- aplicar regras de negócio;
- calcular features;
- chamar o serviço Python;
- montar o resultado final.

### Client

```text
PythonModelClient
```

Responsabilidades:

- enviar descrições para classificação;
- enviar features para previsão;
- tratar timeout e respostas inválidas;
- converter falhas internas em exceções controladas.

### Mapper

```text
FinancialAnalysisMapper
ExpenseSimulationMapper
TransactionClassificationMapper
```

Responsável pela conversão entre DTOs públicos, objetos internos e respostas do serviço Python.

### Validation

Responsável por regras entre campos, como:

- meses não repetidos;
- transações no período analisado;
- coerência entre tipo e classificação;
- validações que envolvem mais de um campo.

### Exception

```text
ApiError
FieldError
BusinessRuleException
ModelServiceException
GlobalExceptionHandler
```

| Situação | HTTP |
|---|---:|
| Payload inválido | 400 |
| Regra de negócio violada | 422 |
| Falha inesperada ou de integração | 500 |

### Enums

```text
SavingFrequency
FinancialProfile
TransactionType
ImpactLevel
RecommendationPriority
TransactionCategory
```

## 5. Estrutura inicial de pacotes

```text
src/main/java/.../rendaflex/
├── client/
├── config/
├── controller/
├── dto/
│   ├── internal/
│   ├── request/
│   └── response/
├── enums/
├── exception/
├── mapper/
├── service/
│   └── impl/
└── validation/
```

`entity` e `repository` não são necessários enquanto a persistência não estiver definida para o MVP.

## 6. Fluxo de análise financeira

1. O Front-end envia o contexto financeiro.
2. O controller valida o payload.
3. O service valida as regras entre campos.
4. As despesas elegíveis são enviadas ao classificador Python.
5. O Spring Boot consolida as categorias.
6. O Spring Boot calcula as features.
7. As features são enviadas ao modelo de perfil.
8. O Python retorna perfil e probabilidade.
9. O Back-end monta métricas, transações e recomendações.
10. O controller retorna a resposta pública.

## 7. Fluxo de simulação

1. O Front-end envia o contexto e a nova despesa.
2. O Back-end valida os dados.
3. O service calcula o cenário atual.
4. O service calcula o cenário projetado.
5. O Python prevê os dois perfis.
6. O Back-end compara os resultados.
7. O Back-end calcula o impacto e monta recomendações.
8. O controller retorna a projeção.

## 8. Fluxo de classificação

1. O Front-end envia transações.
2. O controller valida os campos.
3. O service separa receitas e despesas elegíveis.
4. Apenas descrições elegíveis são enviadas ao Python.
5. O Python retorna categoria e probabilidade.
6. O Back-end recompõe a lista.
7. O Back-end soma somente despesas classificadas.
8. O controller retorna a resposta.

## 9. Ordem de implementação

```text
Documentação da arquitetura
        +
Fundação do Spring Boot
        ↓
Estruturas comuns
        ↓
DTOs e validações
        ↓
Services e controllers
        ↓
Integração com Python
        ↓
Testes
```

## 10. Decisões pendentes

- avaliação da proposta enviada no `project_manager`;
- persistência no MVP;
- formato e endereço do serviço Python;
- timeout e política de repetição;
- armazenamento dos modelos na OCI.

## 11. Critérios de conclusão

- [x] Responsabilidades documentadas;
- [x] Três fluxos descritos;
- [x] Estrutura de pacotes proposta;
- [x] Fronteira Spring Boot–Python registrada;
- [x] Decisões pendentes listadas;
- [x] Diagrama inicial criado;
- [ ] Revisão por outro membro do Back-end;
- [ ] Ajustes da revisão incorporados;
- [ ] Pull Request aberto.
