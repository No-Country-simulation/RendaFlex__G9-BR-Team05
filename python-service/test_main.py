from fastapi.testclient import TestClient

from main import app


client = TestClient(app)


PAYLOAD_ANALISE = {
    "incomeHistory": [3200.0, 3400.0, 3300.0],
    "monthlyDebtPayments": 600.0,
    "otherFixedMonthlyExpenses": 900.0,
    "savingFrequency": "OFTEN",
    "transactions": [
        {"sourceIndex": 0, "description": "Uber", "amount": 51.0}
    ],
}


def test_financial_analysis():
    response = client.post("/internal/v1/financial-analyses", json=PAYLOAD_ANALISE)

    assert response.status_code == 200
    body = response.json()
    assert "financialProfile" in body
    assert "probability" in body
    assert "metrics" in body
    assert "classifiedTransactions" in body
    assert "categorySummary" in body
    assert "categoryPercentages" in body
    assert "recommendations" in body
    assert body["classifiedTransactions"][0]["sourceIndex"] == 0


def test_expense_simulation():
    payload = {
        **PAYLOAD_ANALISE,
        "newExpense": {
            "description": "Notebook",
            "totalAmount": 3600.0,
            "installmentCount": 12,
            "installmentAmount": 300.0,
        },
    }

    response = client.post("/internal/v1/expense-simulations", json=payload)

    assert response.status_code == 200
    body = response.json()
    assert "currentScenario" in body
    assert "projectedScenario" in body
    assert "quantitativeImpact" in body
    assert "recommendations" in body
    assert set(body["quantitativeImpact"]["metricVariations"]) == {
        "debtRatio",
        "fixedCommitment",
    }


def test_transaction_classification():
    payload = {
        "transactions": [
            {"sourceIndex": 0, "description": "Netflix"},
            {"sourceIndex": 1, "description": "Posto de gasolina"},
        ]
    }

    response = client.post("/internal/v1/transactions/classify", json=payload)

    assert response.status_code == 200
    body = response.json()
    assert len(body["transactions"]) == 2
    assert [item["sourceIndex"] for item in body["transactions"]] == [0, 1]
