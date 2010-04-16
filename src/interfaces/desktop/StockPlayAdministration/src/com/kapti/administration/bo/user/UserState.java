/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.administration.bo.user;

import com.kapti.administration.bo.finance.*;
import com.kapti.administration.bo.finance.Security;

/**
 *
 * @author Thijs
 */
public class UserState {

    private User user;
    private int rowNumber;

    public UserState(int rowNumber, User user) {
        this.user = user;
        this.rowNumber = rowNumber;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public User getUser() {
        return user;
    }

    


}
