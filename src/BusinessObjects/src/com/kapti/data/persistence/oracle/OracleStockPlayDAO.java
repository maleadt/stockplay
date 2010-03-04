/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.data.persistence.oracle;

import com.kapti.data.Exchange;
import com.kapti.data.Index;
import com.kapti.data.IndexSecurity;
import com.kapti.data.Order;
import com.kapti.data.Security;
import com.kapti.data.Quote;
import com.kapti.data.Quote.QuotePK;
import com.kapti.data.Transaction;
import com.kapti.data.User;
import com.kapti.data.UserSecurity;
import com.kapti.data.UserSecurity.UserSecurityPK;
import com.kapti.data.persistence.GenericDAO;
import com.kapti.data.persistence.StockPlayDAO;

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

    public GenericDAO<IndexSecurity, IndexSecurity> getIndexSecurityDAO() {
        return IndexSecurityDAO.getInstance();
    }

    public GenericDAO<UserSecurity, UserSecurityPK> getUserSecurityDAO() {
        return UserSecurityDAO.getInstance();
    }

}
