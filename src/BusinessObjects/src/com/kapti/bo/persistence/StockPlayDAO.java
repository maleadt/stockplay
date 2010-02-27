/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.bo.persistence;

import com.kapti.bo.*;

/**
 *
 * @author Thijs
 */
public interface StockPlayDAO {

    public GenericDAO<Exchange, String> getExchangeDAO();
    public GenericDAO<Security, String> getSecurityDAO();
    public GenericDAO<SharePrice, SharePrice.SharePricePK> getSharePriceDAO();

}
