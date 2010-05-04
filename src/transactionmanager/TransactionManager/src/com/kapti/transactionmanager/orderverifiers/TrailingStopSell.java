/*
 * TrailingStopSell.java
 * StockPlay - Trailing Stop Order: Bij een verkoop order wordt de trigger waarde ingesteld op een vast aantal beurspunten onder de hoogste koers. Als de hoogste koers dus verhoogd in waarde, dan verhoogd ook de trigger waarde evenveel punten. Hetzelfde kan ook ingesteld worden bij een aankooporder. Deze trigger waarde krijgt de module via de parameters mee en het maximum sinds de periode van het plaatsen van de order en het huidige tijdstip vraagt deze aan de backend.
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

/**
 *
 * \brief   Trailing Stop Order: Bij een verkoop order wordt de trigger waarde ingesteld op een vast aantal beurspunten onder de hoogste koers. Als de hoogste koers dus verhoogd in waarde, dan verhoogd ook de trigger waarde evenveel punten. Hetzelfde kan ook ingesteld worden bij een aankooporder. Deze trigger waarde krijgt de module via de parameters mee en het maximum sinds de periode van het plaatsen van de order en het huidige tijdstip vraagt deze aan de backend.
 *
 */

public class TrailingStopSell implements OrderVerifier {

    public Type[] getOrderTypes() {
        return new Type[] {
            Type.BRACKET_LIMIT_SELL
        };
    }

    public boolean verifyOrder(Order order) {
        Quote latestQuote = Data.getReference().getCurrentQuotes().get(order.getSecurity());
        return (latestQuote.getPrice() >= order.getPrice() && (latestQuote.getPrice() <= order.getSecondairyLimit()));
    }

}