/*
 * ImmediateOrderVerifier.java
 * StockPlay - Gaat een onmiddelijk order verifieren. De voorwaarde zal altijd voldoen.
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
import com.kapti.client.user.Order.Type;

/**
 *
 * \brief   Gaat een onmiddelijk order verifieren. De voorwaarde zal altijd voldoen.
 *
 */

public class ImmediateOrderVerifier implements OrderVerifier {

    public Type[] getOrderTypes() {
        return new Type[] { Order.Type.IMMEDIATE_BUY, Order.Type.IMMEDIATE_SELL };
    }

    public boolean verifyOrder(Order order) {
        return true;
    }

}