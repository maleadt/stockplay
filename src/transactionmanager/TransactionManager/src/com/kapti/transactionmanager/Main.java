package com.kapti.transactionmanager;

import com.kapti.administration.bo.user.Order;
import com.kapti.administration.bo.user.OrderFactory;
import com.kapti.administration.bo.user.Transaction;
import com.kapti.administration.bo.user.TransactionFactory;
import com.kapti.exceptions.StockPlayException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] as) {
        System.out.println("Starting Transaction Manager..");

        TransactionFactory transactionFactory = TransactionFactory.getInstance();
        OrderFactory orderFactory = OrderFactory.getInstance();

        // TESTING
        try {
            for (Transaction transaction : transactionFactory.getAllTransactions()) {
                System.out.println(transaction.getId());
            }

 
            for (Order order : orderFactory.getAllOrders()) {
                System.out.println(order.getId());
            }
        } catch (StockPlayException ex) {
        }
        // TESTING


        OrderDeamon deamon = new OrderDeamon();
        deamon.setTransactionFactory(transactionFactory);

        for (;;) {
            System.out.println("Controlling orders..");
            try {
                deamon.processOrder(orderFactory.getAllOrders());
            } catch (StockPlayException ex) {
            }
        }

    }
}