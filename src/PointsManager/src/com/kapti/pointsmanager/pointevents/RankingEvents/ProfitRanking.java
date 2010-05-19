package com.kapti.pointsmanager.pointevents.RankingEvents;

import com.kapti.client.user.PointsType;
import com.kapti.client.user.User;
import com.kapti.exceptions.StockPlayException;
import com.kapti.pointsmanager.util.Profit;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * Deze ranking beloont de spelers die de grootste hoeveelheid winst heeft 
 * gemaakt in de voorbije dag.
 *
 * @author Dieter
 */
public class ProfitRanking extends ARankingEvent {

    private static int RANKINGPLAATSEN = 25;

    private static Logger logger = Logger.getLogger(ProfitRanking.class);

    private HashMap<User, Double> profitMap;

    public ProfitRanking(Collection<User> users) {
        super(users);
    }

    @Override
    public PointsType getType() {
        return PointsType.PROFIT;
    }

    @Override
    protected HashMap<User, Integer> calculateWinners(Collection<User> users) {
        profitMap = new HashMap<User, Double>();
        List<Profit> profitList = new ArrayList<Profit>();

        //Winst van alle gebruikers berekenen
        Iterator<User> iterator = users.iterator();

        while(iterator.hasNext()) {
            User user = iterator.next();

            try {
                Profit profit = new Profit(user);
                profitList.add(profit);
                profitMap.put(user, profit.getProfit());
            } catch (StockPlayException ex) {
                logger.error("Error when trying to calculate profit for user " + user.getId(), ex);
            }
        }

        //Rangschikken volgens gemaakte winst
        Collections.sort(profitList, new ProfitComparer());

        //Hashmap maken met scores voor iedere speler
        HashMap<User, Integer> winnersMap = new HashMap<User, Integer>();

        for(int i=0 ; i<RANKINGPLAATSEN ; i++) {
            if (i < profitList.size())
                winnersMap.put(profitList.get(i).getUser(), RANKINGPLAATSEN-i);
        }

        return winnersMap;
    }

    @Override
    public String getDescription(User user) {
        if(winners.containsKey(user)) {
            DecimalFormat df = new DecimalFormat("#.##");
            return "Nr. " + (RANKINGPLAATSEN-winners.get(user)+1) + " (" + df.format(profitMap.get(user)) + "€)";
        }
        else
            return null;
    }

    @Override
    public int getPoints(User user) {
        if(winners.containsKey(user))
            return winners.get(user);
        else
            return 0;
    }

    //Rangschikt van hoog naar laag volgens winst
    private class ProfitComparer implements Comparator<Profit> {
        public int compare(Profit o1, Profit o2) {
            if(o1.getProfit() < o2.getProfit())
                return 1;
            else if(o1.getProfit() == o2.getProfit())
                return 0;
            else
                return -1;
        }
    }
}
