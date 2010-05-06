package com.kapti.pointsmanager;

import com.kapti.client.user.User;
import com.kapti.client.user.UserFactory;
import com.kapti.pointsmanager.pointevents.RankingEvents.ARankingEvent;
import com.kapti.pointsmanager.pointevents.RankingEvents.CashRanking;
import com.kapti.pointsmanager.pointevents.RankingEvents.ProfitRanking;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Iterator;
import org.apache.log4j.Logger;

/**
 *
 * @author Dieter
 */
public class Main {

    private static Logger logger = Logger.getLogger(ProfitRanking.class);

    private static String section = null;

    public static void main(String[] args) {
        try {

            //Config bestand inlezen
            BufferedReader input = new BufferedReader(new FileReader(args[0]));

            String line = input.readLine();
            while(line != null) {
                if(line.trim().equals("[RankingEvents]")) {
                    section = "com.kapti.pointsmanager.pointevents.RankingEvents";
                }
                else if(line.trim().equals("[IndividualEvents]")) {
                    section = "com.kapti.pointsmanager.pointevents.IndividualEvents";
                }
                else {
                    loadPointEvent(line);
                }

                line = input.readLine();
            }




            Collection<User> users = UserFactory.getInstance().getAllUsers();

            Iterator<User> userIterator = users.iterator();

            ARankingEvent ranking = new CashRanking(users);

            //Klasse laden adhv naam
            Class loadedClass = Class.forName("com.kapti.pointsmanager.pointevents.RankingEvents.ProfitRanking");
            //Parameters van de gewenste constructor opgeven
            Class[] paramTypes = { Collection.class };

            //Constructor ophalen die aan parameters voldoet
            Constructor cons = loadedClass.getConstructor(paramTypes);

            //Argumenten voor constructor
            Object[] arguments = { users };

            //Object maken
            ARankingEvent theObject = (ARankingEvent) cons.newInstance(arguments);

            while(userIterator.hasNext()) {
                User user = userIterator.next();

    //            ProfitEvent profitEvent = new ProfitEvent();
    //
    //            PointsTransaction p = PointsTransactionFactory.getInstance().createTransaction(user, new Date());
    //
    //            int points = profitEvent.getPoints(user);
    //            if(points>0) {
    //                p.setDelta(profitEvent.getPoints(user));
    //                p.setComment(profitEvent.getDescription());
    //
    //                PointsTransactionFactory.getInstance().makePersistent(p);
    //            }

//                System.out.println(theObject.getPoints(user));
//                Profit profit = new Profit(user);
//                System.out.println(user.getNickname());
//                System.out.println(profit.getProfit());
//                System.out.println(profit.getProfitPercentage());
//                System.out.println("-------");
            }
        }
        catch(FileNotFoundException ex) {
            logger.error("Error when trying to open configuration file", ex);
        }
        catch(Exception ex) {
            logger.error("Error while executing pointsmanager", ex);
        }
    }

    private static void loadPointEvent(String line) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
