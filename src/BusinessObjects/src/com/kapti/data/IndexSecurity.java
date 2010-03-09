package com.kapti.data;

public class IndexSecurity {
    private int index;
    private String symbol;

    public IndexSecurity(int index, String symbol) {
        this.index = index;
        this.symbol = symbol;
    }

    public int getIndex() {
        return index;
    }

    public String getSymbol() {
        return symbol;
    }
}