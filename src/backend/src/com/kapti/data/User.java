/*
 * User.java
 * StockPlay - Gebruikersklasse
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

package com.kapti.data;

import com.kapti.exceptions.StockPlayException;
import java.util.Date;
import java.util.Hashtable;

public class User {
    //
    // Member data
    //

    public static enum Fields {
        ID, NICKNAME, PASSWORD, LASTNAME, FIRSTNAME, REGDATE, ADMIN, POINTS, STARTAMOUNT, CASH, RRN
    }
    
    private int id = -1;
    private String nickname = "";
    private String password = "";
    private String lastname = "";
    private String firstname = "";
    private Date regdate = null;
    private boolean admin = false;  // TODO: via rechten!
    private int points = 0;
    private double startamount = 0;
    private double cash = 0;
    private int rrn = 0;


    //
    // Construction
    //

    public User() {
    }

    public User(int id) {
        this.id = id;
    }


    //
    // Methods
    //

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public double getCash() {
        return cash;
    }

    public int getId() {
        return id;
    }

    public int getPoints() {
        return points;
    }

    public Date getRegdate() {
        return regdate;
    }

    public double getStartamount() {
        return startamount;
    }

    public void setCash(double cash) {
        this.cash = cash;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setRegdate(Date regdate) {
        this.regdate = regdate;
    }

    public void setStartamount(double startamount) {
        this.startamount = startamount;
    }

    public int getRijksregisternummer() {
        return rrn;
    }

    public void setRijksregisternummer(int rrn) {
        this.rrn = rrn;
    }

    public Hashtable<String, Object> toStruct(Fields... iFields) {
        Hashtable<String, Object> oStruct = new Hashtable<String, Object>();
        for (Fields tField : iFields) {
            switch (tField) {
                case ID:
                    oStruct.put(tField.name(), getId());
                    break;
                case NICKNAME:
                    oStruct.put(tField.name(), getNickname());
                    break;
                case PASSWORD:
                    oStruct.put(tField.name(), getPassword());
                    break;
                case LASTNAME:
                    oStruct.put(tField.name(), getLastname());
                    break;
                case FIRSTNAME:
                    oStruct.put(tField.name(), getFirstname());
                    break;
                case REGDATE:
                    oStruct.put(tField.name(), getRegdate());
                    break;
                case ADMIN:
                    oStruct.put(tField.name(), isAdmin());
                    break;
                case POINTS:
                    oStruct.put(tField.name(), getPoints());
                    break;
                case STARTAMOUNT:
                    oStruct.put(tField.name(), getStartamount());
                    break;
                case CASH:
                    oStruct.put(tField.name(), getCash());
                    break;
                case RRN:
                    oStruct.put(tField.name(), getRijksregisternummer());
                    break;
            }
        }
        return oStruct;
    }

    public void applyStruct(Hashtable<String, Object> iStruct) throws StockPlayException {
        for (String tKey : iStruct.keySet()) {
            Object tValue = iStruct.get(tKey);
            Fields tField = null;
            try {
                tField = Fields.valueOf(tKey.toUpperCase());
            }
            catch (IllegalArgumentException e) {
                throw new StockPlayException("requested key '" + tKey + "' does not exist");
            }

            switch (tField) {
                case NICKNAME:
                    setNickname((String)tValue);
                    break;
                case PASSWORD:
                    setPassword((String)tValue);
                    break;
                case LASTNAME:
                    setLastname((String)tValue);
                    break;
                case FIRSTNAME:
                    setFirstname((String)tValue);
                    break;
                case REGDATE:
                    setRegdate((Date)tValue);
                    break;
                case ADMIN:
                    setAdmin((Boolean)tValue);
                    break;
                case POINTS:
                    setPoints((Integer)tValue);
                    break;
                case STARTAMOUNT:
                    setStartamount((Double)tValue);
                    break;
                case CASH:
                    setCash((Double)tValue);
                    break;
                case RRN:
                    setRijksregisternummer((Integer)tValue);
                    break;

                default:
                    throw new StockPlayException("requested key '" + tKey + "' cannot be modified");
            }
        }
    }

    public static User fromStruct(Hashtable<String, Object> iStruct) throws StockPlayException {
        // Create case mapping
        Hashtable<Fields, String> tStructMap = new Hashtable<Fields, String>();
        for (String tKey : iStruct.keySet()) {
            Fields tField = null;
            try {
                tField = Fields.valueOf(tKey);
            }
            catch (IllegalArgumentException e) {
                throw new StockPlayException("requested key '" + tKey + "' does not exist");
            }
            tStructMap.put(tField, tKey);
        }

        // Check needed keys
        if (tStructMap.containsKey(Fields.ID)) {
            User tUser = new User((Integer)iStruct.get(tStructMap.get(Fields.ID)));
            iStruct.remove(tStructMap.get(Fields.ID));
            return tUser;
        } else {
            throw new StockPlayException("not enough information to instantiate object");
        }
    }
}