package com.kapti.data;

import java.util.Date;
/**
 *
 * @author Thijs
 */
public class Security  {
    private String symbol = "";
    private String name = "";

    private Exchange exchange;

    public Security(){
        
    }

    public Security(String symbol){
        this.symbol = symbol;
    }

    public Security(String symbol, String name, Exchange exchange){
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

    public Quote getSharePrice(){
        return null;
    }

    /**
     * Haalt beurskoers op van effect die het dichtst bij de opgegeven datum zit
     * @param time
     * @return
     */
    public Quote getSharePrice(Date time){
        return null;
    }

        public Exchange getExchange() {
        return exchange;
    }

    public void setExchange(Exchange exchange) {
        this.exchange = exchange;
    }
}
