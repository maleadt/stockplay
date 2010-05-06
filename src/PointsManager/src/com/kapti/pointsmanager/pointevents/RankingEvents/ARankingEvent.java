package com.kapti.pointsmanager.pointevents.RankingEvents;

import com.kapti.client.user.User;
import java.util.Collection;
import java.util.HashMap;

/**
 * Deze klasse is een puntenevent die berekend wordt over de volledige userbase.
 * De klasse krijgt bij constructie een collectie gebruikers mee en berekent
 * hiervoor een ranking. De score voor iedere gebruiker kan dan achteraf
 * opgevraagd worden.
 *
 * @author Dieter
 */
public abstract class ARankingEvent {

    protected HashMap<User, Integer> winners;

    public ARankingEvent(Collection<User> users) {
        winners = calculateWinners(users);
    }

    //Berekent een HashMap met de winnaars voor dit event, samen
    //met de rang die ze behaald hebben
    protected abstract HashMap<User, Integer> calculateWinners(Collection<User> users);

    //Geeft een gepersonaliseerd bericht met de rang van de speler (NULL indien hij niet in aanmerking komt)
    public abstract String getDescription(User user);

    public abstract int getPoints(User user);

}