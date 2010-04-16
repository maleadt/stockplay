/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.administration.bo.user;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

/**
 *
 * @author Thijs
 */
public class User implements Cloneable {

    private static final ResourceBundle translations = ResourceBundle.getBundle("com/kapti/administration/translations");

    public static enum Fields {

        ID, NICKNAME, PASSWORD, EMAIL, LASTNAME, FIRSTNAME, REGDATE, ROLE, POINTS, STARTAMOUNT, CASH, RRN
    }

    public enum Role {

        USER(0),
        ADMIN(1),
        SCRAPER(2),
        AI(3);
        int id;

        private Role(int id) {
            this.id = id;
        }

        public static Role fromId(int id) {
            for (Role r : Role.values()) {
                if (r.id == id) {
                    return r;
                }
            }
            return null;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            if (this == Role.USER) {
                return translations.getString("USER");
            } else if (this == Role.ADMIN) {
                return translations.getString("ADMINISTRATOR");
            } else if (this == Role.SCRAPER) {
                return translations.getString("SCRAPER");
            } else if (this == Role.AI) {
                return translations.getString("AI");
            } else {
                return translations.getString("UNKNOWN");
            }
        }
    }
    /**
     * Deze variabele geeft aan of de waarden van het object werden gewijzigd
     */
    private boolean dirty = false;

    boolean isDirty() {
        return dirty;
    }

    void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    User(int id) {
        this.id = id;
    }

    User(int id, String nickname, String email, String lastname, String firstname,
            Date regdate, Role role, int points, double startamount, double cash, long rrn) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.lastname = lastname;
        this.firstname = firstname;
        this.regdate = regdate;
        this.role = role;
        this.points = points;
        this.cash = cash;
        this.startamount = startamount;
        this.RRN = rrn;
    }
    protected int id;

    /**
     * Get the value of id
     *
     * @return the value of id
     */
    public int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }
    protected String nickname;
    public static final String PROP_NICKNAME = "nickname";

    /**
     * Get the value of nickname
     *
     * @return the value of nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Set the value of nickname
     *
     * @param nickname new value of nickname
     */
    public void setNickname(String nickname) {
        String oldNickname = this.nickname;
        this.nickname = nickname;
        propertyChangeSupport.firePropertyChange(PROP_NICKNAME, oldNickname, nickname);
        dirty = true;
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
    protected String email;
    public static final String PROP_EMAIL = "email";

    /**
     * Get the value of email
     *
     * @return the value of email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the value of email
     *
     * @param email new value of email
     */
    public void setEmail(String email) {
        String oldEmail = this.email;
        this.email = email;
        propertyChangeSupport.firePropertyChange(PROP_EMAIL, oldEmail, email);
        dirty = true;

    }
    protected String lastname;
    public static final String PROP_LASTNAME = "lastname";

    /**
     * Get the value of lastname
     *
     * @return the value of lastname
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * Set the value of lastname
     *
     * @param lastname new value of lastname
     */
    public void setLastname(String lastname) {
        String oldLastname = this.lastname;
        this.lastname = lastname;
        propertyChangeSupport.firePropertyChange(PROP_LASTNAME, oldLastname, lastname);
        dirty = true;

    }
    protected String firstname;
    public static final String PROP_FIRSTNAME = "firstname";

    /**
     * Get the value of firstname
     *
     * @return the value of firstname
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * Set the value of firstname
     *
     * @param firstname new value of firstname
     */
    public void setFirstname(String firstname) {
        String oldFirstname = this.firstname;
        this.firstname = firstname;
        propertyChangeSupport.firePropertyChange(PROP_FIRSTNAME, oldFirstname, firstname);
        dirty = true;

    }
    protected Role role = Role.USER;
    public static final String PROP_ROLE = "role";

    /**
     * Get the value of admin
     *
     * @return the value of admin
     */
    public Role getRole() {
        return role;
    }

    /**
     * Set the value of admin
     *
     * @param admin new value of admin
     */
    public void setRole(Role role) {
        Role oldRole = this.role;
        this.role = role;
        propertyChangeSupport.firePropertyChange(PROP_ROLE, oldRole, role);
        dirty = true;

    }
    protected Integer points = 0;
    public static final String PROP_POINTS = "points";

    /**
     * Get the value of points
     *
     * @return the value of points
     */
    public Integer getPoints() {
        return points;
    }

    /**
     * Set the value of points
     *
     * @param points new value of points
     */
    public void setPoints(Integer points) {
        Integer oldPoints = this.points;
        this.points = points;
        propertyChangeSupport.firePropertyChange(PROP_POINTS, oldPoints, points);
        dirty = true;

    }
    protected Date regdate = null;//Calendar.getInstance().getTime();
    public static final String PROP_REGDATE = "regdate";

    /**
     * Get the value of regdate
     *
     * @return the value of regdate
     */
    public Date getRegdate() {
        return regdate;
    }
    protected Double cash;
    public static final String PROP_CASH = "cash";

    /**
     * Get the value of cash
     *
     * @return the value of cash
     */
    public Double getCash() {
        return cash;
    }

    /**
     * Set the value of cash
     *
     * @param cash new value of cash
     */
    public void setCash(Double cash) {
        Double oldCash = this.cash;
        this.cash = cash;
        propertyChangeSupport.firePropertyChange(PROP_CASH, oldCash, cash);
        dirty = true;

    }
    protected Double startamount;
    public static final String PROP_STARTAMOUNT = "startamount";

    /**
     * Get the value of startamount
     *
     * @return the value of startamount
     */
    public Double getStartamount() {
        return startamount;
    }
    protected Long RRN;
    public static final String PROP_RRN = "RRN";

    /**
     * Get the value of RRN
     *
     * @return the value of RRN
     */
    public Long getRijksregisternummer() {
        return RRN;
    }

    /**
     * Set the value of RRN
     *
     * @param RRN new value of RRN
     */
    public void setRijksregisternummer(Long RRN) {
        Long oldRRN = this.RRN;
        this.RRN = RRN;
        propertyChangeSupport.firePropertyChange(PROP_RRN, oldRRN, RRN);
        dirty = true;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    protected String password;
    public static final String PROP_PASSWORD = "password";

    /**
     * Get the value of password
     *
     * @return the value of password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the value of password
     *
     * @param password new value of password
     */
    public void setPassword(String password) {
        String oldPassword = this.password;
        this.password = password;
        propertyChangeSupport.firePropertyChange(PROP_PASSWORD, oldPassword, password);
    }

    public void setRegdate(Date regdate) {
        this.regdate = regdate;
    }

    public void setStartamount(Double startamount) {
        this.startamount = startamount;
    }
}
