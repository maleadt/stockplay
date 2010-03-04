/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.data.persistence;

import com.kapti.exceptions.StockPlayException;
import java.util.Collection;

/**
 *
 * @author Thijs
 */
public interface GenericDAO<T, ID> {

    T findById(ID id) throws StockPlayException;
    Collection<T> findAll() throws StockPlayException;
    Collection<T> findByExample(T exampleInstance) throws StockPlayException;

    boolean update(T entity) throws StockPlayException;
    boolean create(T entity) throws StockPlayException;
    boolean delete(T entity) throws StockPlayException;


}
