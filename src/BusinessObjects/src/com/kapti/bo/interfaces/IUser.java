/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.bo.interfaces;

import java.util.Date;

/**
 *
 * @author Thijs
 */
public interface IUser {

    double getCash();

    String getFirstname();

    int getId();

    String getLastname();

    String getNickname();

    int getPoints();

    Date getRegdate();

    double getStartamount();

    boolean isAdmin();

    void setAdmin(boolean admin);

    void setFirstname(String firstname);

    void setLastname(String lastname);

    void setNickname(String nickname);

}
