/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.mobileclient.user;

import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 *
 * @author Thijs
 */
public class User {

    private final static String IDFIELD = "ID";
    private final static String NICKNAMEFIELD = "NICKNAME";
    private final static String PASSWORDFIELD = "PASSWORD";
    private final static String EMAILFIELD = "EMAIL";
    private final static String LASTNAMEFIELD = "LASTNAME";
    private final static String FIRSTNAMEFIELD = "FIRSTNAME";
    private final static String ROLEFIELD = "ROLE";
    private final static String REGDATEFIELD = "REGDATE";
    private final static String POINTSFIELD = "POINTS";
    private final static String CASHFIELD = "CASH";
    private final static String RRNFIELD = "RRN";
    private final static String STARTAMOUNTFIELD = "STARTAMOUNT";

    public static class Role {

        public final static String USERROLE = "USER";
        public final static String ADMINROLE = "ADMIN";
        public final static String SCRAPERROLE = "SCRAPER";
        public final static String AIROLE = "AI";

        public static String fromId(int id) {
            switch (id) {
                case 0:
                    return USERROLE;
                case 1:
                    return ADMINROLE;
                case 2:
                    return SCRAPERROLE;
                case 3:
                    return AIROLE;
                default:
                    return null;
            }
        }

        public static int getId(String role) {
            if (role == USERROLE) {
                return 0;
            } else if (role == ADMINROLE) {
                return 1;
            } else if (role == SCRAPERROLE) {
                return 2;
            } else if (role == AIROLE) {
                return 3;
            } else {
                return -1;
            }
        }
    }

    User() {
    }
    protected int id = -1;

    /**
     * Get the value of id
     *
     * @return the value of id
     */
    public int getId() {
        return id;
    }
    protected String nickname;

    /**
     * Get the value of nickname
     *
     * @return the value of nickname
     */
    public String getNickname() {
        return nickname;
    }
    protected String email;

    /**
     * Get the value of email
     *
     * @return the value of email
     */
    public String getEmail() {
        return email;
    }
    protected String lastname;

    /**
     * Get the value of lastname
     *
     * @return the value of lastname
     */
    public String getLastname() {
        return lastname;
    }
    protected String firstname;

    /**
     * Get the value of firstname
     *
     * @return the value of firstname
     */
    public String getFirstname() {
        return firstname;
    }
    protected String role = Role.USERROLE;

    /**
     * Get the value of admin
     *
     * @return the value of admin
     */
    public String getRole() {
        return role;
    }
    protected Integer points = new Integer(0);

    /**
     * Get the value of points
     *
     * @return the value of points
     */
    public Integer getPoints() {
        return points;
    }
    protected Date regdate = null;

    /**
     * Get the value of regdate
     *
     * @return the value of regdate
     */
    public Date getRegdate() {
        return regdate;
    }
    protected Double cash;

    /**
     * Get the value of cash
     *
     * @return the value of cash
     */
    public Double getCash() {
        return cash;
    }
    protected Double startamount;

    /**
     * Get the value of startamount
     *
     * @return the value of startamount
     */
    public Double getStartamount() {
        return startamount;
    }
    protected Long RRN;

    /**
     * Get the value of RRN
     *
     * @return the value of RRN
     */
    public Long getRijksregisternummer() {
        return RRN;
    }
    protected String password;

    /**
     * Get the value of password
     *
     * @return the value of password
     */
    public String getPassword() {
        return password;
    }

    public void setRegdate(Date regdate) {
        this.regdate = regdate;
    }

    public static User fromStruct(Hashtable h) {

        User u = new User();


        for (Enumeration k = h.keys(); k.hasMoreElements();) {
            String key = (String) k.nextElement();

            if (key.equals(IDFIELD)) {
                u.id = ((Integer) h.get(IDFIELD)).intValue();
            } else if (key.equals(EMAILFIELD)) {
                u.email = (String) h.get(EMAILFIELD);
            } else if (key.equals(NICKNAMEFIELD)) {
                u.nickname = (String) h.get(NICKNAMEFIELD);
            } else if (key.equals(LASTNAMEFIELD)) {
                u.lastname = (String) h.get(LASTNAMEFIELD);
            } else if (key.equals(FIRSTNAMEFIELD)) {
                u.firstname = (String) h.get(FIRSTNAMEFIELD);
            } else if (key.equals(PASSWORDFIELD)) {
                u.password = (String) h.get(PASSWORDFIELD);
            } else if (key.equals(ROLEFIELD)) {
                u.role = Role.fromId(((Integer) h.get(ROLEFIELD)).intValue());
            } else if (key.equals(RRNFIELD)) {
                u.RRN = new Long(Long.parseLong((String) h.get(RRNFIELD)));
            } else if (key.equals(REGDATEFIELD)) {
                u.regdate = (Date) h.get(REGDATEFIELD);
            } else if (key.equals(POINTSFIELD)) {
                u.points = (Integer) h.get(POINTSFIELD);

            } else if (key.equals(STARTAMOUNTFIELD)) {
                u.startamount = Double.valueOf((String) h.get(STARTAMOUNTFIELD));

            } else if (key.equals(CASHFIELD)) {
                u.cash = Double.valueOf((String) h.get(CASHFIELD));
            }

        }
        return u;
    }

    public Hashtable toStruct() {
        Hashtable h = new Hashtable();
        h.put(IDFIELD, new Integer(getId()));
        h.put(NICKNAMEFIELD, getNickname());
        h.put(LASTNAMEFIELD, getLastname());
        h.put(FIRSTNAMEFIELD, getFirstname());
        h.put(EMAILFIELD, getEmail());
        h.put(ROLEFIELD, new Integer(Role.getId(role)));
        if (getRijksregisternummer() != null) {
            h.put(RRNFIELD, getRijksregisternummer().toString());
        }
        if (getPassword() != null) {
            h.put(PASSWORDFIELD, getPassword());
        }
        return h;
    }
}
