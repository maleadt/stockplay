/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.transactionmanager.orderverifiers;

import com.kapti.administration.bo.finance.Quote;
import com.kapti.administration.bo.user.Order;

/**
 *
 * @author Thijs
 */
public interface OrderVerifier {

    public Order.Type[] getOrderTypes();

    public boolean verifyOrder(Order order, Quote latestQuote);
}
