package com.kapti.data;

public class Security  {
    private String symbol = "";
    private String name = "";

    private String exchange = "";

    public Security(){
    }

    public Security(String symbol){
        this.symbol = symbol;
    }

    public Security(String symbol, String name, String exchange){
        this.symbol = symbol;
        this.name = name;
        this.exchange = exchange;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    /**
     * Haalt beurskoers op van effect die het dichtst bij de opgegeven datum zit
     * @param time
     * @return
     */
    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }
}