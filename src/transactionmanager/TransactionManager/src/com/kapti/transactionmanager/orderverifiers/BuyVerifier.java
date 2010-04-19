/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.transactionmanager.orderverifiers;

import com.kapti.administration.bo.finance.Quote;
import com.kapti.administration.bo.user.Order;
import com.kapti.administration.bo.user.Order.Type;

/**
 *
 * @author Thijs
 */
public class BuyVerifier implements OrderVerifier{

    public Type[] getOrderTypes() {
        return new Type[] { Type.BUY };
    }
    public boolean verifyOrder(Order order, Quote latestQuote) {
        return order.getPrice() >= latestQuote.getPrice();
    
    }
}
