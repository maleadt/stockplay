package com.kapti.data.persistence;

import com.kapti.data.*;

public interface StockPlayDAO {

    public GenericDAO<Exchange, String> getExchangeDAO();
    public GenericDAO<Index, Integer> getIndexDAO();
    public GenericDAO<Order, Integer> getOrderDAO();
    public QuoteDAO getQuoteDAO();
    public SecurityDAO getSecurityDAO();
    public GenericDAO<Transaction, Integer> getTransactionDAO();
    public GenericDAO<User, Integer> getUserDAO();
    public GenericDAO<IndexSecurity, IndexSecurity> getIndexSecurityDAO();
    public GenericDAO<UserSecurity, UserSecurity.UserSecurityPK> getUserSecurityDAO();
}