/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.bo;

import java.util.Date;

/**
 *
 * @author Thijs
 */
public class User {

    private int id = -1;
    private String nickname ="";
    private String lastname = "" ;
    private String firstname = "";
    private Date regdate = null;
    private boolean admin = false;
    private int points = 0;
    private double startamount = 0;
    private double cash = 0;

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


}
