/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.transactionmanager.orderverifiers;

import com.kapti.administration.bo.user.Order;

/**
 *
 * @author Thijs
 */
public class OrderVerifierFactory {

    private static OrderVerifierFactory instance = new OrderVerifierFactory();

    public static OrderVerifierFactory getInstance() {
        return instance;
    }

    private OrderVerifierFactory() {
    }



    public OrderVerifier getOrderVerifierByType(Order.Type type){
        switch(type){

            case BUY:
                return new BuyVerifier();
            case SELL:
                return new SellVerifier();
            case IMMEDIATE_BUY:
            case IMMEDIATE_SELL:
                return new ImmediateOrderVerifier();

            default:
                return null;

        }


    }

}
