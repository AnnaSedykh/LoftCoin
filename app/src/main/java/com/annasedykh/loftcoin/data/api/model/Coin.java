package com.annasedykh.loftcoin.data.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class Coin {

    @SerializedName("id")
    public int coinId;

    public String name;

    public String symbol;

    @SerializedName("website_slug")
    public String slug;

    public int rank;

    @SerializedName("last_updated")
    public long updated;

    public Map<String, Quote> quotes;
}
