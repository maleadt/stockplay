/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.transactionmanager.orderverifiers;

import com.kapti.client.finance.Quote;
import com.kapti.client.user.Order;
import com.kapti.client.user.Order.Type;

/**
 *
 * @author Thijs
 */
public class SellVerifier implements OrderVerifier{

    public Type[] getOrderTypes() {
        return new Type[] { Type.SELL };
    };

    public boolean verifyOrder(Order order, Quote latestQuote) {
        return(order.getPrice() <= latestQuote.getPrice());
    }

}
