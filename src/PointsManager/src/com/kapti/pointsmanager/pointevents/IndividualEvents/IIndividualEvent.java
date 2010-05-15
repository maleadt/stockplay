package com.kapti.pointsmanager.pointevents.IndividualEvents;

import com.kapti.client.user.PointsType;
import com.kapti.client.user.User;

/**
 * Deze interface moet je implementeren om een puntenevent te maken die
 * berekend moet worden voor iedere individuele gebruiker.
 *
 * @author Dieter
 */
public interface IIndividualEvent {

    //Beschrijving van het type event
    public PointsType getType();

    //Reden waarom de punten uitgedeeld werden
    public String getDescription();

    //Punten die de gebruiker krijgt toegekend (eventueel 0 als hij niet in aanmerking komt)
    public int getPoints(User user);

}
