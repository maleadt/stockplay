/*
 * CheckOrdersTask.java
 * StockPlay - Controleer of aan de voorwaarde van een order voldaan is en voert het dan uit.
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

package com.kapti.transactionmanager;

import com.kapti.client.finance.Quote;
import com.kapti.client.finance.Security;
import com.kapti.client.user.Order;
import com.kapti.client.user.OrderFactory;
import com.kapti.client.user.Transaction;
import com.kapti.client.user.TransactionFactory;
import com.kapti.exceptions.StockPlayException;
import com.kapti.transactionmanager.orderverifiers.Data;
import com.kapti.transactionmanager.orderverifiers.OrderVerifier;
import com.kapti.transactionmanager.orderverifiers.OrderVerifierFactory;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import org.apache.log4j.Logger;

/**
 *
 * \brief   Controleer of aan de voorwaarde van een order voldaan is en voert het dan uit.
 *
 */

public class CheckOrdersTask implements Runnable {

    private static Logger logger = Logger.getLogger(CheckOrdersTask.class);
    private static TransactionFactory transactionFactory = TransactionFactory.getInstance();
    private static OrderVerifierFactory orderVerifierFactory = OrderVerifierFactory.getInstance();
    private OrderFactory orderFactory = OrderFactory.getInstance();
    private static Data data = Data.getReference();

    public CheckOrdersTask() {
    }

    public void run() {
    try {
        logger.info("Starting Orderprocessing");

        // Data verkrijgen
        Collection<Order> currentOrders = data.getCurrentOrders();
        HashMap<Security, Quote> currentQuotes = data.getCurrentQuotes();

        // We testen nu een voor een elk order of het voldoet, en voeren het uit indien dat het geval is
        for (Order order : currentOrders) {
            logger.info("Verifying order " + order.getId());
            OrderVerifier verifier = orderVerifierFactory.getOrderVerifierByType(order.getType());
            Quote quote = currentQuotes.get(order.getSecurity());

//            if (verifier != null && verifier.verifyOrder(order, quote) &&
//                (((order.getType() != Order.Type.BUY || order.getType() != Order.Type.IMMEDIATE_BUY)) &&
//                order.getUser().getCash() > quote.getPrice() * order.getAmount())) {

                if ((verifier != null) && (verifier.verifyOrder(order))) {


                // De voorwaarden om het order te kunnen uitvoeren zijn voldaan, we voeren het uit!
                Transaction transaction = transactionFactory.createTransaction();
                transaction.setUser(order.getUser());
                transaction.setAmount(order.getAmount());
                transaction.setSecurity(order.getSecurity());
                transaction.setTime(new Date());
                transaction.setComment("Execution of order " + order.getId());
                transaction.setPrice(quote.getPrice());
                if (order.getType() == Order.Type.BUY || order.getType() == Order.Type.BUY_IMMEDIATE)
                    transaction.setType(Transaction.Type.BUY);
                else
                    transaction.setType(Transaction.Type.SELL);
                try {
                    if (transactionFactory.execute(transaction)) {
                        logger.info("Order " + order.getId() + " was executed with transaction " + transaction.getId());
                        order.setStatus(Order.OrderStatus.EXECUTED);
                    } else {
                        logger.warn("Failed to execute order " + order.getId());
                        order.setStatus(Order.OrderStatus.FAILED);
                    }
                    orderFactory.makePersistent(order);

                    // We passen de user aan in onze cache
                    order.getUser().setCash(order.getUser().getCash() - quote.getPrice() * order.getAmount());
                } catch (StockPlayException ex) {
                    logger.error("Exception occured while executing order " + order.getId(), ex);
                }
            }
        }
        logger.info("Orderprocessing ended -- waiting for new call");
    } catch(Exception ex) {
        logger.error("Error while processings orders", ex);
    }
    }
}