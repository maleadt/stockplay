/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.administration;

import com.kapti.administration.bo.finance.Security;

/**
 *
 * @author Thijs
 */
public class SecurityState {

    private Security security;
    private int rowNumber;

    public SecurityState(int rowNumber, Security security) {
        this.security = security;
        this.rowNumber = rowNumber;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public Security getSecurity() {
        return security;
    }

    


}
