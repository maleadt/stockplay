/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.data.persistence;

import com.kapti.data.Quote;
import com.kapti.exceptions.StockPlayException;

/**
 *
 * @author Thijs
 */
public interface QuoteDAO extends GenericDAO<Quote,Quote.QuotePK> {
    Quote findLatest(String symbol) throws StockPlayException;
}
