package com.kapti.data;

import java.util.Date;

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