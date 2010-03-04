/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.data;

/**
 *
 * @author Thijs
 */


public class UserSecurity {

    private UserSecurityPK pk;
    private int amount;

    public UserSecurity(UserSecurityPK pk, int amount) {
        this.pk = pk;
        this.amount = amount;
    }

    public UserSecurity(int user, String symbol, int amount){
        this.pk = new UserSecurityPK(user, symbol);
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public UserSecurityPK getPk() {
        return pk;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    

    public class UserSecurityPK{
        private int user;
        private String symbol;

        public String getSymbol() {
            return symbol;
        }

        public int getUser() {
            return user;
        }

        public UserSecurityPK(int user, String symbol) {
            this.user = user;
            this.symbol = symbol;
        }

        public UserSecurityPK() {
        }

        

        


    }

}
