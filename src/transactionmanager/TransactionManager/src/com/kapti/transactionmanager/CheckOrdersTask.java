/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.transactionmanager;

import com.kapti.administration.bo.finance.FinanceFactory;
import com.kapti.administration.bo.finance.Quote;
import com.kapti.administration.bo.finance.Security;
import com.kapti.administration.bo.user.Order;
import com.kapti.administration.bo.user.OrderFactory;
import com.kapti.administration.bo.user.Transaction;
import com.kapti.administration.bo.user.TransactionFactory;
import com.kapti.exceptions.StockPlayException;
import com.kapti.transactionmanager.orderverifiers.OrderVerifier;
import com.kapti.transactionmanager.orderverifiers.OrderVerifierFactory;
import java.awt.Toolkit;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.TimerTask;
import org.apache.log4j.Logger;

/**
 *
 * @author Laurens
 */
public class CheckOrdersTask implements Runnable {

    private static Logger logger = Logger.getLogger(CheckOrdersTask.class);
    private static OrderFactory orderFactory = OrderFactory.getInstance();
    private static TransactionFactory transactionFactory = TransactionFactory.getInstance();
    private static FinanceFactory financeFactory = FinanceFactory.getInstance();
    private static OrderVerifierFactory orderVerifierFactory = OrderVerifierFactory.getInstance();

    public CheckOrdersTask() {
    }


    public void run() {
    try {
        logger.info("Starting Orderprocessing");

        
        //we halen alle pending orders op
        Collection<Order> currentOrders = null;
        try {
            currentOrders = orderFactory.getAllPendingOrders();

        } catch (StockPlayException ex) {
            logger.error("Failed to fetch pending orders", ex);
        }

        //en we halen alle laatste quotes op
        HashMap<Security, Quote> currentQuotes = new HashMap<Security, Quote>();
        try {
            Collection<Quote> latestQuotes = financeFactory.getAllLatestQuotes();
            for (Quote q : latestQuotes) {
                currentQuotes.put(q.getSecurity(), q);
            }

        } catch (StockPlayException ex) {
            logger.error("Failed to fetch the latest quotes", ex);
        }

        //we testen nu een voor een elk order of het voldoet, en voeren het zoja uit

        for (Order order : currentOrders) {
            logger.info("Verifying order " + order.getId());
            OrderVerifier verifier = orderVerifierFactory.getOrderVerifierByType(order.getType());
            Quote quote = currentQuotes.get(order.getSecurity());

          
              

            if (verifier != null && verifier.verifyOrder(order, quote)) {
                // de voorwaarden om het order te kunnen uitvoeren zijn voldaan, we voeren het uit!
                Transaction transaction = transactionFactory.createTransaction();
                transaction.setUser(order.getUser());
                transaction.setAmount(order.getAmount());
                transaction.setSecurity(order.getSecurity());
                transaction.setTime(new Date());
                transaction.setComment("Execution of order " + order.getId());
                transaction.setPrice(quote.getPrice());
                if (order.getType() == Order.Type.BUY
                        || order.getType() == Order.Type.IMMEDIATE_BUY) {

                    transaction.setType(Transaction.Type.BUY);
                    //transaction.setPrice(quote.getAsk());


                } else if (order.getType() == Order.Type.SELL
                        || order.getType() == Order.Type.IMMEDIATE_SELL) {
                    transaction.setType(Transaction.Type.SELL);
                    //transaction.setPrice(quote.getBid());
                }
                try {
                    if (transactionFactory.execute(transaction)) {
                        logger.info("Order " + order.getId() + " was executed with transaction " + transaction.getId());
                        order.setStatus(Order.OrderStatus.EXECUTED);
                    } else {
                        logger.warn("Failed to execute order " + order.getId());
                        order.setStatus(Order.OrderStatus.FAILED);
                    }
                    orderFactory.makePersistent(order);
                    
                } catch (StockPlayException ex) {
                    logger.error("Exception occured while executing order " + order.getId(), ex);
                }

            }
        }

        logger.info("Orderprocessing ended -- waiting for new call");

    }catch(Exception ex){
        logger.error("Error while processings orders", ex);
    }
    }
}
