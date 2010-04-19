package com.kapti.transactionmanager;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;

public class Main {

    private static Logger logger = Logger.getLogger(Main.class);
    private static ScheduledExecutorService daemonService;

    public Main() {
    }

    public static void main(String[] as) {
        logger.info("Starting Transaction Manager..");


        daemonService = Executors.newSingleThreadScheduledExecutor();
        //we voeren elke minuut een controle van de orders uit
       daemonService.scheduleAtFixedRate(new CheckOrdersTask(), 0, 30, TimeUnit.SECONDS);
    }
}
