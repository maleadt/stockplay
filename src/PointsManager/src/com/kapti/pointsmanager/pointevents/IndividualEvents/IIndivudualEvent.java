package com.kapti.pointsmanager.pointevents.IndividualEvents;

import com.kapti.client.user.User;

/**
 * Deze interface moet je implementeren om een puntenevent te maken die
 * berekend moet worden voor iedere individuele gebruiker.
 *
 * @author Dieter
 */
public interface IIndivudualEvent {

    //Beschrijving van de gebeurtenis waarvoor punten worden uitgedeeld
    public String getDescription();

    //Punten die de gebruiker krijgt toegekend (eventueel 0 als hij niet in aanmerking komt)
    public int getPoints(User user);

}
