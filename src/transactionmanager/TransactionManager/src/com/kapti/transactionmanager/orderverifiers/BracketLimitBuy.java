/*
 * BracketLimitBuy.java
 * StockPlay - Bracket Limit Order: Als de koers buiten een van de twee limietwaarden valt dan geeft de module een positief antwoord terug.
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

import com.kapti.client.finance.Quote;
import com.kapti.client.user.Order;
import com.kapti.client.user.Order.Type;
import java.util.Date;

/**
 *
 * \brief   Bracket Limit Order: Als de koers buiten een van de twee limietwaarden valt dan geeft de module een positief antwoord terug.
 *
 */

public class BracketLimitBuy implements OrderVerifier {

    public Type[] getOrderTypes() {
        return new Type[] {
            Type.BRACKET_LIMIT_BUY
        };
    }

    public boolean verifyOrder(Order order) {
        Quote latestQuote = Data.getReference().getCurrentQuotes().get(order.getSecurity());

//        System.out.println("Limit " + order.getPrice());
//        System.out.println("Low " + Data.getReference().getLowest(order.getCreationTime(), new Date(), order.getSecurity().getISIN()));
//        System.out.println("Track " + (order.getPrice() + Data.getReference().getLowest(order.getCreationTime(), new Date(), order.getSecurity().getISIN())));
//        System.out.println("Current " + latestQuote.getPrice());

        return (latestQuote.getPrice() >= (order.getPrice() + Data.getReference().getLowest(order.getCreationTime(), new Date(), order.getSecurity().getISIN())));
    }

}