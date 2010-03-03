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

    public GenericDAO<Index, Integer> getIndexDAO();

    public GenericDAO<Order, Integer> getOrderDAO();

    public GenericDAO<Quote, Quote.QuotePK> getQuoteDAO();

    public GenericDAO<Security, String> getSecurityDAO();

    public GenericDAO<Transaction, Integer> getTransactionDAO();

    public GenericDAO<User, Integer> getUserDAO();
}
