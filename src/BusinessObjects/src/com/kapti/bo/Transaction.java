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
public class Transaction extends Instruction {

    private Date time;

    public Transaction(int id){
        super(id);
    }

    public Transaction(int id, int user, String security, int amount, double price, InstructionType type, Date time) {
        super(id, user, security, amount, price, type);
        this.time = time;
    }

    

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }


    
}
