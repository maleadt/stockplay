/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.bo.interfaces;

import java.util.Date;

/**
 *
 * @author Thijs
 */
public interface ISharePrice {

    double getBuy();

    double getHigh();

    double getLow();

    double getPrice();

    double getSell();

    Date getTime();

    int getVolume();

}
