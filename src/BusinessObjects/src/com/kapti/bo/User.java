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
    private String nickname = "";
    private String password = "";
    private String lastname = "";
    private String firstname = "";
    private Date regdate = null;
    private boolean admin = false;
    private int points = 0;
    private double startamount = 0;
    private double cash = 0;
    private int rrn = 0;

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


}
