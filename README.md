<div align="center">

# RendaFlex

### Financial guidance designed for variable-income realities

[![Status](https://img.shields.io/badge/status-MVP%20implementation-F59E0B?style=flat-square)](#current-progress)

**Programa ONE Hackathon · Oracle Next Education**

</div>

> [!NOTE]
> RendaFlex is in active MVP implementation. The main modules are under development, and the mocked frontend already provides a demonstrable financial-analysis flow. Backend and Data/ML work is being aligned around the final Spring Boot–Python interface while the public contract is consolidated. Real service integration, integrated testing, and OCI deployment are still pending; the prototype should not yet be considered an operational financial service.

## About

RendaFlex is a financial decision-support project for people whose income changes from month to month. The MVP turns financial history and transaction data into a financial-profile analysis, expense simulations, classified transactions, financial indicators, recommendations, and a simple dashboard, with public REST API responses represented as JSON.

## Problem

Traditional personal-finance tools often assume a predictable monthly salary. This assumption does not represent freelancers, gig workers, commission-based professionals, informal workers, and others whose income varies over time. These users need guidance that considers income volatility, spending patterns, and existing commitments before they make new financial decisions.

## Current Progress

- [x] Problem, target audience, and MVP scope defined
- [x] Original transaction dataset evaluated and prepared
- [x] Synthetic financial-profile dataset created and explored
- [x] Financial feature engineering developed in the Data/ML notebook
- [x] Transaction-classification model trained and evaluated
- [x] Financial-profile model trained and evaluated
- [x] Model serialization prepared in the Data/ML notebook
- [x] Expense-simulation logic prepared in the Data/ML notebook
- [x] Repository and initial architecture/API documentation created
- [x] Spring Boot foundation created
- [ ] Public DTOs, enums, validation, and standardized errors (in progress on backend branches)
- [ ] Backend orchestration, calculators, controllers, and Python gateway
- [x] React application foundation and responsive home page
- [x] Financial-analysis form and local validation
- [x] Mocked asynchronous analysis flow with session-scoped state
- [x] Complete mocked financial-analysis result page
- [ ] Final public API contract revision (in progress)
- [ ] Expense-simulation frontend
- [ ] Spring Boot–Python internal contract (under alignment)
- [ ] Spring Boot–Python integration
- [ ] Real frontend–backend integration
- [ ] Automated application test suite (basic backend tests exist; expansion pending)
- [ ] Integrated and end-to-end validation
- [ ] OCI deployment
- [ ] Demo Day video

## Current Technical Decisions

The application boundary is:

```text
React + TypeScript → Spring Boot → Python ML Service
```

### Frontend

- collects raw financial data and performs user-interface validation;
- is designed to submit public API requests to Spring Boot and display analysis and simulation responses;
- does not calculate financial features, classify transactions, or call Python directly.

### Spring Boot backend

- validates public requests, adapts fields, enums, and scales, and orchestrates use cases;
- will handle Python communication failures and timeouts and compose the final public response;
- may perform backend-specific calculations such as scale conversion, response-derived information, and simulation support;
- does not duplicate the feature engineering owned by Python.

### Python service

- receives the internal data required for analysis and calculates model features;
- runs the financial-profile model and transaction classifier;
- returns predictions, probabilities, and derived data required by the backend.

The Spring Boot–Python request/response interface, internal endpoints, timeout behavior, and error mapping are still being finalized. The frontend never sends the public payload directly to Python.

## Frontend Status

The functional mocked prototype uses React, TypeScript, Vite, and React Router. It currently includes:

- a shared application layout, navigation, and responsive home page;
- a financial-analysis form with three to six income-history months, saving-frequency selection, and dynamic transaction registration;
- local validation for required values, duplicate months, amounts, dates, transaction types, and the analysis period;
- a mock asynchronous analysis service;
- Context-based analysis state with temporary `sessionStorage` persistence;
- a result page with financial profile and probability, financial metrics, category spending summary, classified transactions with probabilities, and recommendations;
- loading, submission-error, empty-result, and empty-section states;
- responsive styling and accessible labels, focus states, validation feedback, and live status messages;
- ESLint and TypeScript/Vite production-build scripts, validated during frontend development.

The displayed results are simulated and are not calculated from the values entered in the form. Real integration with Spring Boot is pending. The expense-simulation experience remains the next main frontend feature.

## Backend Status

The repository contains the Spring Boot application foundation on `main`. Backend development visible in the repository history and backend branches also includes:

- an initial layered/package architecture and component documentation;
- public request DTOs for financial analysis and simulation;
- `FinancialAnalysisResponse`, `FinancialMetrics`, `ClassifiedTransaction`, and recommendation response structures;
- shared enums;
- Bean Validation constraints and a cross-field financial-analysis validator;
- standardized API errors, custom exceptions, and global exception handling;
- a preliminary simulation validator;
- basic context, validation, exception-handler, and response-DTO tests.

These backend pieces are still being consolidated. Controllers, orchestration services, calculators, profilers, a Python client/gateway, production simulation logic, and public endpoints are not present as complete implementations. Python integration is therefore pending, not operational.

## Data and ML Status

The versioned notebooks and generated sample data document:

- evaluation and preparation of the original transaction dataset;
- a synthetic financial-profile dataset and exploratory analysis;
- preprocessing and feature engineering;
- a TF-IDF and Multinomial Naive Bayes transaction-classification experiment;
- a Random Forest financial-profile model, evaluation, and feature-importance analysis;
- `joblib` serialization preparation for the models, vectorizer, and feature list;
- analysis and expense-simulation functions prepared in the notebook;
- calculations for average income, income variation, debt ratio, fixed commitment, and category spending summaries;
- separate monthly debt payments and other fixed monthly expenses in the revised notebook flow.

The final classifier category list is still being aligned with the public contract. Dataset categories must not be treated as the final classifier enum. Internal notebook variables may remain in Portuguese, but attributes exchanged across service boundaries must use English.

`categoryPercentages` will not be an input feature for the Random Forest model, and the model will not be retrained solely to include it. It is intended as informative output derived from the category spending summary; its final public location, structure, and scale remain under contract review.

## API Contract

Public contract version `1.0.0` remains the initial MVP reference and is stored at:

```text
docs/api/rendaflex_api_contract_v1.0.0.json
```

It defines English technical identifiers, Portuguese human-readable messages, camel-case JSON fields, the `/api/v1` base path, schemas, validation rules, enums, errors, and examples. It is being revised rather than treated as final and immutable.

The final revision covers:

- Python ownership of model feature engineering;
- replacement of `monthlyDebts` with `monthlyDebtPayments` and `otherFixedMonthlyExpenses`;
- English names across service boundaries;
- the final classifier category list;
- the informative role and public representation of `categoryPercentages`;
- recommendation and expense-simulation response formats;
- integration errors, timeout behavior, and internal Python endpoints.

Separating `monthlyDebtPayments` from `otherFixedMonthlyExpenses` allows the system to distinguish the debt ratio from the broader fixed financial commitment. This split exists in the revised Data/ML flow but is not yet consistently operational across the public contract, frontend, and backend DTOs.

Version `1.0.0` documents these public operations:

- `POST /api/v1/financial-analyses`
- `POST /api/v1/expense-simulations`
- `POST /api/v1/transactions/classify`

They are documented operations, not currently available endpoints. The standalone transaction-classification operation remains under final MVP contract review.

## Technology Stack

- **Frontend:** React, TypeScript, Vite, and React Router
- **Backend:** Java and Spring Boot
- **Data and ML:** Python, Pandas, and Scikit-learn
- **Modeling:** Random Forest, TF-IDF, and Multinomial Naive Bayes
- **Cloud target:** Oracle Cloud Infrastructure
- **Collaboration:** GitHub, Discord, and Trello

## Testing and Quality

- The frontend provides ESLint and TypeScript/Vite production-build checks; recent frontend work records successful lint and production-build validation.
- The frontend implements accessible validation feedback and explicit loading, error, and empty states for manual flow checks.
- Backend branches include basic context, DTO, validation, and global-error-handler tests.
- Broader unit coverage, contract tests, service integration tests, and end-to-end validation remain planned for the final Hackathon phase.
- No coverage percentage or completed end-to-end suite is claimed.

## Next Steps

1. Finalize the public API contract revision.
2. Finalize Spring Boot–Python request and response formats, internal endpoints, timeouts, and integration errors.
3. Complete the mocked expense-simulation frontend.
4. Consolidate backend DTOs, validation, errors, and response structures into the integration branch.
5. Implement backend orchestration, calculations, controllers, and the Python gateway.
6. Integrate the financial-analysis frontend with Spring Boot.
7. Integrate Spring Boot with the Python service and then integrate expense simulation.
8. Align enums, scales, categories, recommendations, and error handling across all modules.
9. Add and expand automated tests, then perform integrated and end-to-end validation.
10. Prepare OCI deployment and stabilize the application for the final Hackathon phase.
11. Produce the Demo Day presentation video.

## Team

- **Deane Carvalho** — Data
- **Gabriel Nunes** — Backend
- **Gabriel Soares** — Data
- **Junior Ribeiro** — Data
- **Millena Belo** — Backend
- **Paulo Emilio** — Full Stack
- **Raul Oliveira** — Backend

The areas above describe the team's general contribution domains and do not imply formal organizational titles.

## Project Context

RendaFlex is being developed for the **Programa ONE Hackathon**, part of **Oracle Next Education**, in collaboration with **Alura**. The project applies the program's software-development and data-learning tracks to a practical financial-inclusion challenge.

The mocked frontend prototype is close to completion, and the final Hackathon phase will focus primarily on contract consolidation, real service integration, bug fixing, testing, OCI deployment, and the Demo Day video. The team expects final delivery in the penultimate week of August.
