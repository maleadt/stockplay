/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.transactionmanager;

import com.kapti.administration.bo.user.Transaction;
import com.kapti.administration.bo.user.TransactionFactory;
import com.kapti.transactionmanager.orders.ImmediateOrder;
import com.kapti.transactionmanager.orders.OrderManager;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author Laurens
 */
public class OrderDeamon {

    private TransactionFactory transactionFactory;

    public OrderDeamon() {

    }

    /**
     * @param transactionFactory the transactionFactory to set
     */
    public void setTransactionFactory(TransactionFactory transactionFactory) {
        this.transactionFactory = transactionFactory;
    }

    public void processOrder(Collection<com.kapti.administration.bo.user.Order> orders) {
        OrderManager orderManager;
        for (com.kapti.administration.bo.user.Order order : orders) {
            orderManager = new ImmediateOrder(order);
            if (orderManager.conditionResult()) {
                Transaction transaction = transactionFactory.createTransaction();
                transaction.setAmount(order.getAmount());
                transaction.setSecurity(order.getSecurity());
                transaction.setTime(new Date());
                transaction.getType(order.getType();
            }
        }
    }

}
