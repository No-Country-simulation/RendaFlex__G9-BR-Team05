<div align="center">

# RendaFlex

### Financial guidance designed for variable-income realities

[![Status](https://img.shields.io/badge/status-MVP%20Completed%20%2F%20Production%20Ready-16A34A?style=flat-square)](#current-progress)

**Programa ONE Hackathon · Oracle Next Education**

</div>

> [!NOTE]
> RendaFlex is 100% integrated, functional, and running in production. The end-to-end solution operates from the Vercel frontend through the Java Spring Boot backend on OCI Compute to the Python/ML microservice on OCI Compute, with saved-model persistence and artifact support in OCI Object Storage.

## Live Prototype

[Open the RendaFlex frontend prototype](https://rendaflex.vercel.app/)

## About

RendaFlex is a financial decision-support project for people whose income changes from month to month. The completed MVP turns financial history and transaction data into a financial-profile analysis, expense simulations, classified transactions, financial indicators, recommendations, and a simple dashboard through production REST APIs.

## Problem

Traditional personal-finance tools often assume a predictable monthly salary. This assumption does not represent freelancers, gig workers, commission-based professionals, informal workers, and others whose income varies over time. These users need guidance that considers income volatility, spending patterns, and existing commitments before they make new financial decisions.

## Current Progress

- [x] Problem, target audience, and MVP scope defined
- [x] Original transaction dataset evaluated and prepared
- [x] Synthetic financial-profile dataset created and explored
- [x] Financial feature engineering developed in the Data/ML notebook
- [x] Transaction-classification model trained and evaluated
- [x] Financial-profile model trained and evaluated
- [x] Model serialization prepared and validated
- [x] Expense-simulation logic implemented
- [x] Repository, architecture, and API documentation created
- [x] Spring Boot foundation, public API, validation, CORS, and standardized errors
- [x] Concrete Python gateway and integrated backend orchestration
- [x] React application foundation and responsive financial-analysis flow
- [x] Complete replacement of mocks with the real production API
- [x] Expense simulation and current-versus-projected result
- [x] Light and dark theme support, semantic icons, and UX/UI refinements
- [x] SPA configuration and production deployment on Vercel
- [x] Final public API and Spring Boot–Python internal contracts
- [x] Spring Boot–Python HTTP integration and error handling
- [x] Automated unit and integration test suites
- [x] Integrated and end-to-end validation
- [x] Containerized Docker deployment on OCI Compute with OCI Object Storage artifacts
- [x] Demo Day presentation and demonstration preparation

## Current Technical Decisions

The application boundary is deployed in Docker containers on Oracle Cloud Infrastructure:

```text
React + TypeScript (Vercel) → Spring Boot (OCI Compute / Docker) → Python ML Service (OCI Compute / Docker)
                                                    ↘ OCI Object Storage
```

### Frontend

- collects raw financial data and performs user-interface validation;
- consumes the real production Spring Boot REST API with `VITE_USE_MOCK_API=false`;
- displays financial analysis and expense-simulation responses;
- does not calculate financial features, classify transactions, or call Python directly.

### Spring Boot backend

- validates public requests, adapts fields, enums, and scales, and orchestrates use cases;
- communicates with Python through the implemented `FinancialAnalysisGateway` HTTP integration;
- handles Python communication failures and timeouts and composes final public responses;
- performs backend-specific calculations, including expense-simulation support;
- does not duplicate the feature engineering owned by Python.

### Python service

- receives the internal data required for analysis and calculates model features;
- runs the financial-profile model and transaction classifier;
- returns predictions, probabilities, classified transactions, and derived data required by the backend;
- is containerized and deployed on OCI Compute with the integrated application stack.

### Model Loading Decision

For the MVP, the serialized `.pkl` AI models are kept and packaged directly in the Python Docker image through `COPY`. This engineering decision ensures autonomous initialization, high availability, and predictable zero-risk startup behavior. Uploading the same artifacts to OCI Object Storage provides a proof of concept and an artifact repository; dynamic runtime download from Object Storage is intentionally mapped as a post-MVP evolution.

The frontend never sends the public payload directly to Python.

## Frontend Status

The production frontend uses React 19, TypeScript, Vite, React Router, and Vercel. A shared layout and responsive Home present financial analysis and expense simulation as sequential stages:

```text
Financial analysis → analysis result → expense simulation → current-versus-projected result
```

The financial-analysis form supports three to six income-history months, saving frequency, dynamic transactions, `monthlyDebtPayments`, and `otherFixedMonthlyExpenses`. Local validation covers required values, duplicate months, amounts, dates, transaction types, the analysis period, and monthly commitments. With `VITE_USE_MOCK_API=false`, the production frontend uses `VITE_API_BASE_URL` to consume the real Spring Boot REST API without a mock fallback.

The real financial-analysis request path is:

```text
FinancialAnalysisPage → financialAnalysisService → financialAnalysisApi → apiClient → POST /api/v1/financial-analyses
```

The API client handles the public `ApiError` format defined by contract version `2.0.0`. Analysis and simulation use separate Contexts and independently persist results in `sessionStorage`.

The interface includes semantic `lucide-react` icons, fade-slide-up animations, semantic profile progress bars, and tabular numerals for currency values. It provides empty, loading, submission-error, and result states; responsive behavior; accessibility-oriented labels; keyboard focus; validation feedback; and live status messages. Light and dark themes are managed by `ThemeProvider` and `ThemeToggle`, use system preference on the first visit, and persist the selection in `localStorage` under `rendaflex-theme`.

The Vercel deployment supports SPA fallback for direct access to and refreshes on React Router routes. The production flow is fully validated against the real backend and ML service.

## Backend Status

The Spring Boot production API includes:

- `FinancialAnalysisController`, `FinancialAnalysisService`, `FinancialAnalysisValidator`, and `FinancialAnalysisGateway`;
- public version `2.0.0` request and response DTOs and enums;
- Bean Validation, financial-context business rules, and standardized error responses;
- global CORS configuration for the Vercel frontend;
- HTTP integration from Spring Boot to the Python FastAPI service, including timeout and error handling;
- expense-simulation calculation and integrated response composition;
- successful automated unit and integration test suites, including 132 unit tests;
- a public request example at `docs/api/examples/financial-analysis-request.json`.

The backend is containerized with Docker and deployed on OCI Compute. Financial analysis, expense simulation, and the Python-backed transaction-classification flow are operational end to end.

## Data and ML Status

The versioned notebooks and generated sample data document:

- evaluation and preparation of the original transaction dataset;
- a synthetic financial-profile dataset and exploratory analysis;
- preprocessing and feature engineering;
- a TF-IDF and Multinomial Naive Bayes transaction-classification model;
- a Random Forest financial-profile model, evaluation, and feature-importance analysis;
- validated `joblib` serialization for the models, vectorizer, and feature list;
- integrated analysis and expense-simulation functions;
- calculations for average income, income variation, debt ratio, fixed commitment, and category spending summaries.

The trained Random Forest profile model and Multinomial Naive Bayes/TF-IDF transaction classifier are serialized, validated, and served by the Python microservice in the OCI ecosystem. Their `.pkl` artifacts are packaged in the production Docker image and managed as artifacts in OCI Object Storage.

The final public classifier categories are `FOOD`, `TRANSPORT`, `HEALTH`, `HOUSING`, `EDUCATION`, `ENTERTAINMENT`, `SERVICES`, and `OTHER`. Dataset categories are normalized to this contract enum. Internal notebook variables may remain in Portuguese, but attributes exchanged across service boundaries use English.

`categoryPercentages` is not an input feature for the Random Forest model. It is informative output derived from `categorySummary`, calculated on a `0.0` to `1.0` internal scale and adapted by the backend to the public `0` to `100` scale.

## API Contract

The final public MVP contract is version `2.0.0`, with status `FINAL_FOR_MVP`, and is stored at:

```text
docs/api/rendaflex_api_contract_v2.0.0.json
```

It defines English technical identifiers, Portuguese human-readable messages, camel-case JSON fields, the `/api/v1` base path, public and internal schemas, validation rules, enums, errors, integration policies, and examples. Version `1.0.0` is retained only as the previous, incompatible contract version.

Version `2.0.0` establishes:

- Python ownership of model feature engineering;
- replacement of `monthlyDebts` with `monthlyDebtPayments` and `otherFixedMonthlyExpenses`;
- English names across service boundaries;
- the final classifier category list;
- the informative role and public representation of `categoryPercentages`;
- recommendation and expense-simulation response formats;
- integration errors, timeout behavior, and internal Python endpoints.

Version `2.0.0` defines these public operations:

- `POST /api/v1/financial-analyses`
- `POST /api/v1/expense-simulations`
- `POST /api/v1/transactions/classify`

All three operations are implemented and validated through the integrated production stack.

## Technology Stack

- **Frontend:** React 19, TypeScript, Vite, React Router, lucide-react, and CSS Custom Properties
- **Backend:** Java and Spring Boot
- **Data and ML:** Python, FastAPI, Pandas, and Scikit-learn
- **Modeling:** Random Forest, TF-IDF, and Multinomial Naive Bayes
- **Hosting:** Vercel and Oracle Cloud Infrastructure (OCI Compute + Object Storage)
- **Containerization:** Docker
- **Collaboration:** GitHub, Discord, and Trello

## Testing and Quality

- Frontend changes have been checked with ESLint, TypeScript, the Vite production build, and `git diff --check`.
- Manual validation covers the real analysis and simulation flows, direct SPA routes, light and dark themes, independent `sessionStorage` result persistence, `localStorage` theme persistence, and responsive behavior.
- Accessibility-oriented checks cover labels, visible keyboard focus, validation feedback, and live status messages, alongside explicit loading, error, and empty states.
- Automated backend unit and integration suites pass successfully, including 132 unit tests.
- Integrated Spring Boot–Python tests and frontend-to-model end-to-end validation pass successfully.
- Dockerized services on OCI Compute and saved ML artifacts in OCI Object Storage complete the Hackathon OCI requirement.

## Next Steps

1. Present the completed MVP at Demo Day.
2. Record and publish the final demonstration video.
3. Evolve model loading with dynamic OCI Object Storage downloads at runtime.
4. Add authenticated, persistent user history and continuous financial tracking.
5. Extend observability, analytics, and post-MVP product capabilities.

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

RendaFlex was developed for the **Programa ONE Hackathon**, part of **Oracle Next Education**, in collaboration with **Alura**. The project applies the program's software-development and data-learning tracks to a practical financial-inclusion challenge.

The MVP is complete, integrated, and production-ready: the Vercel frontend consumes the Spring Boot API deployed in Docker on OCI Compute, which integrates with the Python/ML service on OCI Compute. The AI artifacts are packaged for autonomous service startup and supported by OCI Object Storage. The remaining roadmap focuses on Demo Day delivery and post-MVP evolution.
