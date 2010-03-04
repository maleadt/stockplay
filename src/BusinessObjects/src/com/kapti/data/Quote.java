/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.data;

import java.util.Date;

/**
 *
 * @author Thijs
 */
public class Quote {

    public class QuotePK {

        private Security security = null;
        private Date time = null;

        public QuotePK() {
        }

        public QuotePK(Security security, Date time){
            this.security = security;
            this.time = time;
        }

        public Security getSecurity() {
            return security;
        }

        public Date getTime() {
            return time;
        }




    }

    private QuotePK pk = null;
    private double price = 0.0;
    private int volume = 0;
    private double buy = 0, sell = 0;
    private double low = 0, high = 0;

    public Quote(Security security, Date time, double price, int volume, double buy, double sell, double low, double high) {
        this.pk = new QuotePK(security, time);
        this.price = price;
        this.volume = volume;
        this.buy = buy;
        this.sell = sell;
        this.low = low;
        this.high = high;
    }

    public Security getSecurity() {
        return pk.getSecurity();
    }

    public double getBuy() {
        return buy;
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

    public double getSell() {
        return sell;
    }

    public Date getTime() {
        return pk.getTime();
    }

    public int getVolume() {
        return volume;
    }

    public void setBuy(double buy) {
        this.buy = buy;
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
        this.sell = sell;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public QuotePK getPk() {
        return pk;
    }

    
}
