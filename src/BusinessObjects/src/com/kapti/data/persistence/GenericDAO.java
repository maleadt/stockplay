package com.kapti.data.persistence;

import com.kapti.exceptions.StockPlayException;
import java.util.Collection;

public interface GenericDAO<T, ID> {

    T findById(ID id) throws StockPlayException;
    Collection<T> findAll() throws StockPlayException;
    Collection<T> findByExample(T exampleInstance) throws StockPlayException;

    boolean update(T entity) throws StockPlayException;
    boolean create(T entity) throws StockPlayException;
    boolean delete(T entity) throws StockPlayException;

}