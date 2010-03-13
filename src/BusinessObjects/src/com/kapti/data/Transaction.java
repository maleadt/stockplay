package com.kapti.data;

import java.util.Date;
import java.util.Hashtable;

public class Transaction extends Instruction {
    //
    // Member data
    //

    public static enum Fields {
        ID, USER, SECURITY, AMOUNT, PRICE, TYPE,    // Instruction.Fields
        TIME
    }
    
    private Date time;


    //
    // Construction
    //

    public Transaction(int id){
        super(id);
    }

    public Transaction(int id, int user, String security, int amount, double price, InstructionType type, Date time) {
        super(id, user, security, amount, price, type);
        this.time = time;
    }


    //
    // Methods
    //

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Hashtable toStruct(Fields... iFields) {
        Hashtable<String, Object> oStruct = new Hashtable<String, Object>();
        for (Fields tField : iFields) {
            switch (tField) {
                // Instruction.Fields
                case ID:
                    oStruct.put("id", getId());
                    break;
                case USER:
                    oStruct.put("user", getUser());
                    break;
                case SECURITY:
                    oStruct.put("security", getSecurity());
                    break;
                case AMOUNT:
                    oStruct.put("amount", getAmount());
                    break;
                case PRICE:
                    oStruct.put("price", getPrice());
                    break;
                case TYPE:
                    oStruct.put("type", getType());
                    break;

                case TIME:
                    oStruct.put("time", getTime());
                    break;
            }
        }
        return oStruct;
    }
   
}