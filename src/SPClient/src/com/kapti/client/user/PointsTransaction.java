/*
 * PointsTransaction.java
 * StockPlay - Punten verschillen noteren.
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

package com.kapti.client.user;

import com.kapti.exceptions.StockPlayException;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Date;
import java.util.HashMap;
import org.apache.log4j.Logger;

/**
 *
 * \brief   Punten verschillen noteren.
 *
 */

public class PointsTransaction {

    private static Logger logger = Logger.getLogger(PointsTransaction.class);

    public static enum Fields {

        USER, TYPE, TIMESTAMP, DELTA, COMMENTS
    }

    PointsTransaction(User user, PointsType type, Date time) {
        this.user = user;
        this.type = type;
        this.time = time;
    }

    PointsTransaction(User user, Date time, int delta, String comment) {
        this.user = user;
        this.time = time;
        this.delta = delta;
        this.comment = comment;
    }

    protected PointsType type = PointsType.MANUAL;
    public static final String PROP_TYPE = "type";

    /**
     * Get the value of user
     *
     * @return the value of user
     */
    public PointsType getType() {
        return type;
    }

    /**
     * Set the value of user
     *
     * @param user new value of user
     */
    public void setType(PointsType type) {
        PointsType oldType = this.type;
        this.type = type;
        propertyChangeSupport.firePropertyChange(PROP_TYPE, oldType, type);
    }

    protected User user;
    public static final String PROP_USER = "user";

    /**
     * Get the value of user
     *
     * @return the value of user
     */
    public User getUser() {
        return user;
    }

    /**
     * Set the value of user
     *
     * @param user new value of user
     */
    public void setUser(User user) {
        User oldUser = this.user;
        this.user = user;
        propertyChangeSupport.firePropertyChange(PROP_USER, oldUser, user);
    }



    protected Date time;
    public static final String PROP_TIME = "time";

    /**
     * Get the value of time
     *
     * @return the value of time
     */
    public Date getTime() {
        return time;
    }

    /**
     * Set the value of time
     *
     * @param time new value of time
     */
    void setTime(Date time) {
        Date oldTime = this.time;
        this.time = time;
        propertyChangeSupport.firePropertyChange(PROP_TIME, oldTime, time);
    }

    protected int delta;
    public static final String PROP_DELTA = "delta";

    /**
     * Get the value of delta
     *
     * @return the value of delta
     */
    public int getDelta() {
        return delta;
    }

    /**
     * Set the value of delta
     *
     * @param delta new value of delta
     */
    public void setDelta(int delta) {
        int oldDelta = this.delta;
        this.delta = delta;
        propertyChangeSupport.firePropertyChange(PROP_DELTA, oldDelta, delta);
    }

    protected String comment;
    public static final String PROP_COMMENT = "comment";

    /**
     * Get the value of comment
     *
     * @return the value of comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * Set the value of comment
     *
     * @param comment new value of comment
     */
    public void setComment(String comment) {
        String oldComment = this.comment;
        this.comment = comment;
        propertyChangeSupport.firePropertyChange(PROP_COMMENT, oldComment, comment);
    }



    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    /**
     * Add PropertyChangeListener.
     *
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Remove PropertyChangeListener.
     *
     * @param listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }


        public static PointsTransaction fromStruct(HashMap h) {
            User user = null;
                  try {
            user = UserFactory.getInstance().getUserById((Integer) h.get(Fields.USER.toString()));
        } catch (StockPlayException ex) {

            logger.error("Error while retrieving user for pointstransaction", ex);
        }

        PointsTransaction t = new PointsTransaction(user,(PointsType) h.get(Fields.TYPE.toString()) , (Date) h.get(Fields.TIMESTAMP.toString()));

        t.setDelta((Integer) h.get(Fields.DELTA.toString()));
        t.setComment((String) h.get(Fields.COMMENTS.toString()));

        return t;
    }

    public HashMap toStruct(){

        HashMap h = new HashMap();

        h.put(Fields.USER.toString(), getUser().getId());
        h.put(Fields.TYPE.toString(), getType().toString());
        h.put(Fields.TIMESTAMP.toString(), getTime());
        h.put(Fields.DELTA.toString(), getDelta());

        h.put(Fields.COMMENTS.toString(), getComment());

        return h;


    }
}