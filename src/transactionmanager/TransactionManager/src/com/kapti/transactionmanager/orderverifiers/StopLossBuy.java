/*
 * StopLossBuy.java
 * StockPlay - Stop loss order: Een order wordt omgezet in een effectieve handeling na een trigger op de limietwaarde. Het order wordt hierdoor sneller uitgevoerd, maar er is geen minimumprijs (maximumprijs) gegarandeerd.
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
import java.util.Date;

/**
 *
 * \brief   Stop loss order: Een order wordt omgezet in een effectieve handeling na een trigger op de limietwaarde. Het order wordt hierdoor sneller uitgevoerd, maar er is geen minimumprijs (maximumprijs) gegarandeerd.
 *
 */

public class StopLossBuy implements OrderVerifier {

    public Type[] getOrderTypes() {
        return new Type[] {
            Type.STOP_LOSS_BUY
        };
    }

    public boolean verifyOrder(Order order) {
        return (Data.getReference().getHighest(order.getCreationTime(), new Date(), order.getSecurity().getISIN()) >= order.getPrice());
    }

}