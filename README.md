<div align="center">

# RendaFlex

### Financial guidance designed for variable-income realities

[![Status](https://img.shields.io/badge/status-early%20development-F59E0B?style=flat-square)](#current-progress)

**Programa ONE Hackathon · Oracle Next Education**

</div>

> [!NOTE]
> RendaFlex is in early development. The problem, MVP scope, data work, conceptual architecture, and public API contract have been defined; the application modules are now beginning to be implemented.

## About

RendaFlex is a financial decision-support project for people whose income changes from month to month. The MVP is designed to turn financial history and transaction data into a financial-profile analysis, expense simulations, classified transactions, financial indicators, recommendations, and a simple dashboard, with public REST API responses represented as JSON.

## Problem

Traditional personal-finance tools often assume a predictable monthly salary. This assumption does not represent freelancers, gig workers, commission-based professionals, informal workers, and others whose income varies over time. These users need guidance that considers income volatility, spending patterns, and existing commitments before they make new financial decisions.

## Current Progress

- [x] Problem, target audience, and MVP scope defined
- [x] Initial dataset evaluated by the Data team
- [x] Original dataset selected for category and consumption-pattern analysis
- [x] Synthetic income and financial-profile dataset created
- [x] Exploratory data analysis completed in the Data notebook
- [x] Data preprocessing and feature engineering prepared
- [x] Transaction-classification model trained and evaluated
- [x] Financial-profile model trained and evaluated
- [x] Model serialization prepared in the Data notebook
- [x] Expense-simulation logic prepared in the Data notebook
- [x] Public API contract version `1.0.0` consolidated and approved for the MVP
- [x] Repository and initial project documentation created
- [ ] Backend foundation
- [ ] Public DTOs, validation, and controllers
- [ ] Frontend prototype
- [x] Frontend foundation created
- [ ] Backend–ML service integration
- [ ] Automated application tests
- [ ] OCI deployment

## Current Technical Decisions

The conceptual application boundary has been agreed upon:

```text
React + TypeScript → Spring Boot → Python ML Service
```

- The frontend will collect and submit raw financial data and present API responses.
- The Spring Boot backend will own public validation, business rules, model-feature calculation, orchestration, and response composition.
- The Python service will execute the machine-learning models and return predictions and probabilities.
- The frontend will not calculate model features or assign transaction categories.
- The Python service will not receive public API payloads directly.

These decisions define implementation responsibilities; they do not indicate that the application services are already operational.

## Frontend Status

The initial frontend foundation has been implemented with React, TypeScript, Vite, and React Router.

The current prototype includes:

- a shared application layout;
- basic navigation;
- a provisional home page;
- a provisional financial-analysis page;
- a provisional financial-analysis result page;
- initial global styles;
- production build and lint validation.

The next frontend step is to implement the financial-analysis form based on the approved API contract.

## API Contract

The approved public API contract is the current reference for MVP implementation. Version `1.0.0` uses English technical identifiers, Portuguese human-readable messages, camel-case JSON fields, and the `/api/v1` base path.

The contract defines three public operations:

- `POST /api/v1/financial-analyses`
- `POST /api/v1/expense-simulations`
- `POST /api/v1/transactions/classify`

The canonical documentation path is:

```text
docs/api/rendaflex_api_contract_v1.0.0.json
```

The contract covers schemas, enums, validation rules, error responses, examples, architecture boundaries, and versioning policy. It is a specification for implementation; the endpoints are not yet presented as available services.

## Technology Direction

The following stack represents the approved technical direction for the project, not a claim that every module has already been implemented:

- **Frontend:** React, TypeScript, and Vite
- **Backend:** Java and Spring Boot
- **Data and ML:** Python, Pandas, and Scikit-learn
- **Modeling:** Random Forest, TF-IDF, and Multinomial Naive Bayes
- **Cloud target:** Oracle Cloud Infrastructure
- **Collaboration:** GitHub and Trello

## Next Steps

1. Create the Spring Boot backend foundation.
2. Implement public DTOs and contract-aligned validation.
3. Implement the three approved API endpoints.
4. Continue the React prototype with the financial-analysis form and mocked results.
5. Expose the prepared ML models through an internal Python service.
6. Integrate the Spring Boot backend with the ML service.
7. Add contract, unit, integration, and model-boundary tests.
8. Prepare OCI deployment after the application flow is stable.

## Team

- **Deane Carvalho** — Data
- **Gabriel Nunes** — Backend
- **Gabriel Soares** — Data
- **Junior Ribeiro** — Data
- **Millena Belo** — Backend
- **Paulo Emilio** — Full Stack
- **Raul Oliveira** — Backend

The areas above describe the team's current general contribution domains and do not imply formal organizational titles.

## Project Context

RendaFlex is being developed for the **Programa ONE Hackathon**, part of **Oracle Next Education**, in collaboration with **Alura**. The project applies the program's software-development and data-learning tracks to a practical financial-inclusion challenge.

The repository currently records the approved product direction, API decisions, and initial documentation while implementation begins.
