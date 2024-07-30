CREATE TABLE ExchangeRates
(
    ID               INTEGER PRIMARY KEY AUTOINCREMENT,
    BaseCurrencyId   INTEGER       NOT NULL,
    TargetCurrencyId INTEGER       NOT NULL,
    Rate             DECIMAL(6, 4) NOT NULL,
    FOREIGN KEY (BaseCurrencyId) REFERENCES Currencies(ID),
    FOREIGN KEY (TargetCurrencyId) REFERENCES Currencies(ID),
    UNIQUE (BaseCurrencyId, TargetCurrencyId)
);

CREATE UNIQUE INDEX idx_unique_currency_pair ON ExchangeRates (BaseCurrencyId, TargetCurrencyId);
