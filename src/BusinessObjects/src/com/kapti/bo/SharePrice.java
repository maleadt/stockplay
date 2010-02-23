/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.bo;

import com.kapti.bo.interfaces.ISharePrice;
import java.util.Date;

/**
 *
 * @author Thijs
 */
public class SharePrice implements ISharePrice {

    private Date time;
    private double price;
    private int volume;
    private double buy, sell;
    private double low, high;

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
        return time;
    }

    public int getVolume() {
        return volume;
    }



}
