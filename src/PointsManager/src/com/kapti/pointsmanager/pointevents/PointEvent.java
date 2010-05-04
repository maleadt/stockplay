package com.kapti.pointsmanager.pointevents;

import com.kapti.client.user.User;

/**
 *
 * @author Dieter
 */
public interface PointEvent {

    public String getDescription();

    public int getPoints(User user);

}
