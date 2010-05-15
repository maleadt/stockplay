/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.pointsmanager.pointevents.RankingEvents;

import com.kapti.client.user.PointsType;
import com.kapti.client.user.User;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Deze klasse berekent een ranking van de cashpositie van de spelers.
 *
 * @author Dieter
 */
public class CashRanking extends ARankingEvent {

    private static int RANKINGPLAATSEN = 25; //Hoeveel 'podiumplaatsen' er zijn

    public CashRanking(Collection<User> users) {
        super(users);
    }

    @Override
    public PointsType getType() {
        return PointsType.CASH;
    }

    @Override
    protected HashMap<User, Integer> calculateWinners(Collection<User> users) {

        //Gebruikers sorteren op cashpositie
        List<User> usersList = new ArrayList<User>(users);
        Collections.sort(usersList, new CashComparer());

        //Hoogst gerankte spelers eruithalen
        HashMap<User, Integer> winningUsers = new HashMap<User, Integer>();
        for(int i=0 ; i<RANKINGPLAATSEN ; i++)
            winningUsers.put(usersList.get(i), RANKINGPLAATSEN-i);

        return winningUsers;
    }

    @Override
    public String getDescription(User user) {
        if(winners.containsKey(user)) {
            DecimalFormat df = new DecimalFormat("#.##");
            return "Nr. " + (RANKINGPLAATSEN-winners.get(user)+1) + " (" + df.format(user.getCash()) + "€)";
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

    //Rangschikt van hoog naar laag
    private class CashComparer implements Comparator<User> {
        public int compare(User o1, User o2) {
            if(o1.getCash() < o2.getCash())
                return 1;
            else if(o1.getCash() == o2.getCash())
                return 0;
            else
                return -1;
        }
    }
}
