package com.annasedykh.loftcoin.data.db.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.annasedykh.loftcoin.data.model.Fiat;

@Entity(tableName = "Coin")
public class CoinEntity {

    @PrimaryKey
    public int coinId;

    public String name;

    public String symbol;

    public String slug;

    public int rank;

    public long updated;

    @Embedded(prefix = "usd_")
    public QuoteEntity usd;

    @Embedded(prefix = "rub_")
    public QuoteEntity rub;

    @Embedded(prefix = "eur_")
    public QuoteEntity eur;

    public QuoteEntity getQuote(Fiat fiat) {
        QuoteEntity quote = null;

        switch (fiat) {
            case USD:
                quote = usd;
                break;

            case RUB:
                quote = rub;
                break;

            case EUR:
                quote = eur;
                break;
        }
        if (quote == null) {
            return usd;
        } else {
            return quote;
        }
    }
}
