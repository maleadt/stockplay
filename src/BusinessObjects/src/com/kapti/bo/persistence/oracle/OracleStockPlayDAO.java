/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.bo.persistence.oracle;

import com.kapti.bo.Exchange;
import com.kapti.bo.Index;
import com.kapti.bo.Order;
import com.kapti.bo.Security;
import com.kapti.bo.Quote;
import com.kapti.bo.Quote.QuotePK;
import com.kapti.bo.Transaction;
import com.kapti.bo.User;
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

    public GenericDAO<Quote, QuotePK> getQuoteDAO() {
        return QuoteDAO.getInstance();
    }

    public GenericDAO<User, Integer> getUserDAO() {
        return UserDAO.getInstance();
    }

    public GenericDAO<Index, Integer> getIndexDAO() {
        return IndexDAO.getInstance();
    }

    public GenericDAO<Order, Integer> getOrderDAO() {
        return OrderDAO.getInstance();
    }

    public GenericDAO<Transaction, Integer> getTransactionDAO() {
       return TransactionDAO.getInstance();
    }

}
