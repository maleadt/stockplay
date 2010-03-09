package com.kapti.data;

import java.util.Date;

public class Quote {

    public class QuotePK {

        private String security = null;
        private Date time = null;

        public QuotePK() {
        }

        public QuotePK(String security, Date time){
            this.security = security;
            this.time = time;
        }

        public String getSecurity() {
            return security;
        }

        public Date getTime() {
            return time;
        }
    }

    private QuotePK pk = null;
    private double price = 0.0;
    private int volume = 0;
    private double bid = 0, ask = 0;
    private double low = 0, high = 0;
    private double open = 0;

    public Quote(String security, Date time, double price, int volume, double bid, double ask, double low, double high, double open) {
        this.pk = new QuotePK(security, time);
        this.price = price;
        this.volume = volume;
        this.bid = bid;
        this.ask = ask;
        this.low = low;
        this.high = high;
        this.open = open;
    }

    public String getSecurity() {
        return pk.getSecurity();
    }

    public double getBid() {
        return bid;
    }

    public double getHigh() {
        return high;
    }

    public double getLow() {
        return low;
    }

    public double getPrice() {
        return price;
    }

    public double getAsk() {
        return ask;
    }

    public Date getTime() {
        return pk.getTime();
    }

    public int getVolume() {
        return volume;
    }

    public void setBuy(double buy) {
        this.bid = buy;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setSell(double sell) {
        this.ask = sell;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public QuotePK getPk() {
        return pk;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }
    
}