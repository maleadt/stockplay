/*
 * Main.java
 * StockPlay - Start van de transaction manager.
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

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;

/**
 * \brief   Start van de transaction manager.
 *
 * Deze klasse wordt aangeroepen als de transaction manager gestart wordt.
 * Ze gaat alle benodigde objecten aanmaken en de periode check opstarten.
 *
 */

public class Main {

    private static Logger logger = Logger.getLogger(Main.class);
    private static ScheduledExecutorService daemonService;

    public static void main(String[] as) {
        logger.info("Starting Transaction Manager..");

//        CheckOrdersTask t = new CheckOrdersTask();
//        t.run();

        daemonService = Executors.newSingleThreadScheduledExecutor();
        //we voeren elke x seconden een controle van de orders uit
        daemonService.scheduleAtFixedRate(new CheckOrdersTask(), 0, 20, TimeUnit.SECONDS);
    }

}