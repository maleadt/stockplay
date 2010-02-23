/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.bo.interfaces;

import com.kapti.bo.SharePrice;
import java.util.Date;

/**
 *
 * @author Thijs
 */
public interface ISecurity {

    String getName();

    SharePrice getSharePrice();

    /**
     * Haalt beurskoers op van effect die het dichtst bij de opgegeven datum zit
     * @param time
     * @return
     */
    SharePrice getSharePrice(Date time);

    String getSymbol();

    void setName(String name);

    void setSymbol(String symbol);

}
