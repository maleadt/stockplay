package com.kapti.pointsmanager;

import com.kapti.client.user.User;
import com.kapti.client.user.UserFactory;
import com.kapti.exceptions.StockPlayException;
import com.kapti.pointsmanager.pointevents.IndividualEvents.IIndividualEvent;
import com.kapti.pointsmanager.pointevents.RankingEvents.ARankingEvent;
import com.kapti.pointsmanager.pointevents.RankingEvents.ProfitRanking;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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

            //Users opvragen
            Collection<User> users = UserFactory.getInstance().getAllUsers();

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
                    if(section.equals("com.kapti.pointsmanager.pointevents.IndividualEvents"))
                        loadIndividualEvent(section + "." + line, users);
                    else
                        loadRankingEvent(section + "." + line, users);
                }

                line = input.readLine();
            }

            System.out.println("Succesfully completed points calculation");
        }
        catch(FileNotFoundException ex) {
            logger.error("Error when trying to open configuration file", ex);
        }
        catch(Exception ex) {
            logger.error("Error while executing pointsmanager", ex);
        }
    }

    private static void loadIndividualEvent(String className, Collection<User> users) throws ClassNotFoundException, InstantiationException, IllegalAccessException, StockPlayException {
        //Klasse laden adhv naam
        Class loadedClass = Class.forName(className);

        //Instantie aanmaken (deze klassen hebben een lege constructor en hierdoor kan je gewoon newInstance aanroepen)
        IIndividualEvent individualEvent = (IIndividualEvent) loadedClass.newInstance();

        Iterator<User> iterator = users.iterator();

        while(iterator.hasNext()) {
            User user = iterator.next();

            if(individualEvent.getPoints(user) != 0) {
                System.out.println(user.getNickname());
                System.out.println(individualEvent.getDescription());
                System.out.println(individualEvent.getPoints(user));
            }
        }


    }

    private static void loadRankingEvent(String className, Collection<User> users) throws ClassNotFoundException, NoSuchMethodException, StockPlayException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Klasse laden adhv naam
        Class loadedClass = Class.forName(className);
        //Parameters van de gewenste constructor opgeven
        Class[] params = { Collection.class };
        
        //Constructor ophalen die aan parameters voldoet
        Constructor cons = loadedClass.getConstructor(params);

        //Argumenten voor constructor
        Object[] args = { users };

        //Object maken
        ARankingEvent rankingEvent = (ARankingEvent) cons.newInstance(args);

        Iterator<User> iterator = users.iterator();

        while(iterator.hasNext()) {
            User user = iterator.next();

            if(rankingEvent.getDescription(user) != null) {
                System.out.println(user.getNickname());
                System.out.println(rankingEvent.getPoints(user));
                System.out.println(rankingEvent.getDescription(user));
            }
        }
    }
}
