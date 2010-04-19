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
public class ImmediateOrder extends OrderManager {

    public ImmediateOrder(Order iOrder) {
        super(iOrder);
    }


    @Override
    public boolean conditionResult() {
        return true;
    }

}
