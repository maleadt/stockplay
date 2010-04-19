/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.transactionmanager.orders;

import com.kapti.administration.bo.user.Order;

/**
 *
 * @author Laurens
 */
public abstract class OrderManager {

    protected Order order;

    public OrderManager(Order iOrder) {
        order = iOrder;
    }

//    public void checkCondition() {
//        if (conditionResult())
//            process();
//    }

    public abstract boolean conditionResult();

//    protected void process() {
//    }

}