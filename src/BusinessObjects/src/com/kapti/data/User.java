package com.kapti.data;

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

    public User(int id, String nickname, String password, String lastname, String firstname, boolean admin, Date regdate, int rrn, int points, double startamount, double cash) {
        this.id = id;
        this.nickname = nickname;
        this.password = password;
        this.lastname = lastname;
        this.firstname = firstname;
        this.regdate = regdate;
        this.rrn = rrn;
        this.admin = admin;
        this.points = points;
        this.startamount = startamount;
        this.cash = cash;
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

    public Hashtable toStruct(Fields... iFields) {
        Hashtable<String, Object> oStruct = new Hashtable<String, Object>();
        for (Fields tField : iFields) {
            switch (tField) {
                case ID:
                    oStruct.put("id", getId());
                    break;
                case NICKNAME:
                    oStruct.put("name", getNickname());
                    break;
                case PASSWORD:
                    oStruct.put("location", getPassword());
                    break;
                case LASTNAME:
                    oStruct.put("location", getLastname());
                    break;
                case FIRSTNAME:
                    oStruct.put("location", getFirstname());
                    break;
                case REGDATE:
                    oStruct.put("location", getRegdate());
                    break;
                case ADMIN:
                    oStruct.put("location", isAdmin());
                    break;
                case POINTS:
                    oStruct.put("location", getPoints());
                    break;
                case STARTAMOUNT:
                    oStruct.put("location", getStartamount());
                    break;
                case CASH:
                    oStruct.put("location", getCash());
                    break;
                case RRN:
                    oStruct.put("location", getRijksregisternummer());
                    break;
            }
        }
        return oStruct;
    }
}