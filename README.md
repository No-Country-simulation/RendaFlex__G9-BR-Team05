<div align="center">

# RendaFlex

### Financial guidance designed for variable-income realities

[![Status](https://img.shields.io/badge/status-MVP%20implementation-F59E0B?style=flat-square)](#current-progress)

**Programa ONE Hackathon · Oracle Next Education**

</div>

> [!NOTE]
> RendaFlex is in active MVP implementation. The functional frontend prototype is complete and demonstrates both financial analysis and new-expense simulation through a first public Vercel deployment. It can use mocks or the public API, and the Spring Boot public layer for financial analysis is implemented. The concrete Spring Boot–Python gateway, complete integrated testing, and OCI deployment are still pending; the public deployment is intended only for flow demonstration and validation and must not be considered an operational financial service.

## Live Prototype

[Open the RendaFlex frontend prototype](https://rendaflex.vercel.app/)

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
- [x] Public DTOs, enums, validation, and standardized errors for financial analysis
- [x] Financial-analysis controller, service, business validation, CORS, and public-layer tests
- [ ] Concrete Python gateway and integrated backend orchestration
- [x] React application foundation and responsive home page
- [x] Financial-analysis form and local validation
- [x] Mocked asynchronous financial-analysis flow
- [x] Complete mocked financial-analysis result page
- [x] Monthly financial commitments fields
- [x] Mocked expense-simulation frontend
- [x] Current-versus-projected expense-simulation result
- [x] Light and dark theme support
- [x] Centralized CSS color tokens
- [x] SPA configuration for Vercel
- [x] First public frontend prototype deployment
- [x] Final public API contract revision
- [x] Spring Boot–Python internal contract defined for the MVP
- [ ] Spring Boot–Python integration
- [ ] Real frontend–backend integration
- [x] Financial-analysis public-layer automated test suite
- [ ] Automated test expansion for the remaining integrated flows
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

The Spring Boot–Python request/response interface, internal endpoints, timeout behavior, and error mapping are defined by the final MVP contract. Their concrete implementation is still pending. The frontend never sends the public payload directly to Python.

## Frontend Status

The functional prototype uses React, TypeScript, Vite, and React Router. A shared layout and responsive Home present financial analysis and expense simulation as sequential stages. The current flow is:

```text
Financial analysis → analysis result → expense simulation → current-versus-projected result
```

The financial-analysis form supports three to six income-history months, saving frequency, dynamic transactions, `monthlyDebtPayments`, and `otherFixedMonthlyExpenses`. These monthly commitment fields capture debt payments separately from other fixed monthly expenses. Local validation covers required values, duplicate months, amounts, dates, transaction types, the analysis period, and monthly commitments. Service adapters allow the flow to use mocks or the real public API according to `VITE_USE_MOCK_API`; when disabled, `VITE_API_BASE_URL` defines the Spring Boot base URL and there is no silent fallback to mocks.

The real financial-analysis request path is:

```text
FinancialAnalysisPage → financialAnalysisService → financialAnalysisApi → apiClient → POST /api/v1/financial-analyses
```

The API client also handles the final public `ApiError` format defined by contract version `2.0.0`.

Analysis and simulation use separate Contexts and independently persist their results in `sessionStorage`. The analysis result presents the financial profile and probability, financial metrics, category summary, classified transactions, and recommendations. Expense simulation requires an existing analysis and collects only a new expense description, total amount, and installment count before presenting a mocked current-versus-projected comparison.

Analysis transactions represent the user's current or already occurred financial activity. The simulated expense represents a hypothetical future decision and does not modify the current analysis. Results remain available only temporarily in the browser session; the prototype does not provide authentication, a database, continuous financial tracking, or persistent history.

The frontend includes empty, loading, submission-error, and result states; responsive behavior; and accessibility-oriented labels, keyboard focus, validation feedback, and live status messages. Its visual identity uses the base palette `#061E29`, `#1D546D`, `#5F9598`, and `#F3F4F4`. Light and dark themes are managed by `ThemeProvider` and `ThemeToggle`, use system preference on the first visit, and persist the selection in `localStorage` under `rendaflex.theme`. Semantic colors are centralized through CSS Custom Properties, with no relevant hardcoded colors outside the global theme stylesheet.

The frontend is deployed to Vercel as a demonstrable prototype. The SPA fallback supports direct access to and refreshes on React Router routes. Mocked analysis and simulation remain available, and the frontend is prepared to consume the public API. The real end-to-end flow has not been validated because the concrete Spring Boot–Python gateway is not yet implemented.

## Backend Status

The Spring Boot public layer for `POST /api/v1/financial-analyses` includes:

- `FinancialAnalysisController`, `FinancialAnalysisService`, and `FinancialAnalysisValidator`;
- public version `2.0.0` request and response DTOs and enums;
- Bean Validation and public financial-context business rules;
- `GlobalExceptionHandler` and standardized responses for HTTP 400, 422, 500, 502, and 503;
- global CORS configuration for the local frontend;
- `FinancialAnalysisGateway` as the integration boundary;
- 26 passing public-layer tests;
- a public request example at `docs/api/examples/financial-analysis-request.json`.

The application compiles and reaches Spring Boot and Tomcat startup. The financial-analysis flow is not operational end to end because no concrete Spring bean implements `FinancialAnalysisGateway`. The integration still requires internal DTOs, an HTTP client, `sourceIndex` correlation, timeout and retry handling, the internal Python call, and response adaptation. Textual recommendations remain a backend responsibility; `categorySummary` and `categoryPercentages` are calculated in the Python flow and adapted by the backend.

The public expense-simulation and standalone transaction-classification endpoints are defined by the contract but are not implemented in the current backend layer. No production-ready integrated backend is claimed.

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

The final public classifier categories are `FOOD`, `TRANSPORT`, `HEALTH`, `HOUSING`, `EDUCATION`, `ENTERTAINMENT`, `SERVICES`, and `OTHER`. Dataset categories must be normalized to this contract enum. Internal notebook variables may remain in Portuguese, but attributes exchanged across service boundaries use English.

`categoryPercentages` is not an input feature for the Random Forest model, and the model is not retrained solely to include it. It is informative output derived from `categorySummary`, calculated on a `0.0` to `1.0` internal scale and adapted by the backend to the public `0` to `100` scale.

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

Separating `monthlyDebtPayments` from `otherFixedMonthlyExpenses` allows the system to distinguish the debt ratio from the broader fixed financial commitment. Both fields are present in the frontend analysis form, the public financial-analysis DTOs, and the revised Data/ML flow.

Version `2.0.0` defines these public operations:

- `POST /api/v1/financial-analyses`
- `POST /api/v1/expense-simulations`
- `POST /api/v1/transactions/classify`

The public layer for `POST /api/v1/financial-analyses` is implemented, but its end-to-end flow awaits the concrete Spring Boot–Python gateway. The expense-simulation and standalone transaction-classification operations remain contracted but are not implemented as public backend endpoints.

## Technology Stack

- **Frontend:** React, TypeScript, Vite, React Router, and CSS Custom Properties
- **Backend:** Java and Spring Boot
- **Data and ML:** Python, Pandas, and Scikit-learn
- **Modeling:** Random Forest, TF-IDF, and Multinomial Naive Bayes
- **Current frontend hosting:** Vercel
- **Final integrated cloud target:** Oracle Cloud Infrastructure
- **Collaboration:** GitHub, Discord, and Trello

## Testing and Quality

- Frontend changes have been checked with ESLint, TypeScript, the Vite production build, and `git diff --check`.
- Manual validation covers the analysis and simulation flows, direct SPA routes, light and dark themes, independent `sessionStorage` result persistence, `localStorage` theme persistence, and responsive behavior.
- Accessibility-oriented checks cover labels, visible keyboard focus, validation feedback, and live status messages, alongside explicit loading, error, and empty states.
- The financial-analysis public backend layer has 26 passing tests covering context, controller behavior, CORS, service delegation, validation, and global error handling.
- Broader coverage for the remaining operations, Spring Boot–Python integration tests, and complete frontend-to-model end-to-end validation remain planned for the final Hackathon phase.
- No coverage percentage or completed end-to-end suite is claimed.

## Next Steps

1. Implement the concrete `FinancialAnalysisGateway` and its internal DTOs, HTTP client, correlation, timeout, retry, and error mapping.
2. Integrate Spring Boot with the Python service and validate the real financial-analysis flow.
3. Implement and consolidate the public expense-simulation and transaction-classification operations.
4. Progressively replace mocked responses with real service responses.
5. Expand automated tests and perform integrated and end-to-end validation.
6. Prepare OCI deployment and stabilize the final application.
7. Produce the Demo Day presentation video.

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

The functional frontend prototype is complete and publicly deployed, the version `2.0.0` MVP contract is final, and the financial-analysis public backend layer is ready. The concrete Spring Boot–Python gateway is the main integration dependency, while complete integrated validation, OCI deployment, final stabilization, and Demo Day preparation remain pending. The team expects final delivery in the penultimate week of August.
