/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.bo.persistence.oracle;

import com.kapti.bo.Exchange;
import com.kapti.bo.Security;
import com.kapti.bo.SharePrice;
import com.kapti.bo.SharePrice.SharePricePK;
import com.kapti.bo.persistence.GenericDAO;
import com.kapti.bo.persistence.StockPlayDAO;

/**
 *
 * @author Thijs
 */
public class OracleStockPlayDAO implements StockPlayDAO{


    public GenericDAO<Exchange, String> getExchangeDAO() {
        return ExchangeDAO.getInstance();
    }

    public GenericDAO<Security, String> getSecurityDAO() {
        return SecurityDAO.getInstance();
    }

    public GenericDAO<SharePrice, SharePricePK> getSharePriceDAO() {
        return SharePriceDAO.getInstance();
    }

}
