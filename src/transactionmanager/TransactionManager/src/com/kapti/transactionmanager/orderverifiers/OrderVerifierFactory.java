/*
 * OrderVerifierFactory.java
 * StockPlay - Leverd een ordercontroleur.
 *
 * Copyright (c) 2010 StockPlay development team
 * All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.kapti.transactionmanager.orderverifiers;

import com.kapti.client.user.Order;

/**
 *
 * \brief   Leverd een ordercontroleur.
 *
 */

public class OrderVerifierFactory {

    private static OrderVerifierFactory instance = new OrderVerifierFactory();

    public static OrderVerifierFactory getInstance() {
        return instance;
    }

    public OrderVerifier getOrderVerifierByType(Order.Type type){
        switch(type){
            case BUY:
                return new BuyVerifier();
            case SELL:
                return new SellVerifier();
            case IMMEDIATE_BUY:
                return new ImmediateOrderVerifier();
            case IMMEDIATE_SELL:
                return new ImmediateOrderVerifier();
            case STOP_LOSS_BUY:
                return new StopLossBuy();
            default:
                return null;
        }
    }
}