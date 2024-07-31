CREATE TABLE exchangeRates
(
    ID               INTEGER PRIMARY KEY AUTOINCREMENT,
    BaseCurrencyId   INTEGER       NOT NULL,
    TargetCurrencyId INTEGER       NOT NULL,
    Rate             DECIMAL(6, 4) NOT NULL,
    FOREIGN KEY (BaseCurrencyId) REFERENCES currencies(ID),
    FOREIGN KEY (TargetCurrencyId) REFERENCES currencies(ID),
    UNIQUE (BaseCurrencyId, TargetCurrencyId)
);

CREATE UNIQUE INDEX idx_unique_currency_pair ON exchangeRates (BaseCurrencyId, TargetCurrencyId);
create table currencies
(
    ID integer,
    ID integer
);

