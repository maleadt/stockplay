package com.kapti.data;

import com.kapti.exceptions.StockPlayException;
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

    public Hashtable<String, Object> toStruct(Fields... iFields) {
        Hashtable<String, Object> oStruct = new Hashtable<String, Object>();
        for (Fields tField : iFields) {
            switch (tField) {
                // Instruction.Fields
                case ID:
                    oStruct.put(tField.name(), getId());
                    break;
                case USER:
                    oStruct.put(tField.name(), getUser());
                    break;
                case SECURITY:
                    oStruct.put(tField.name(), getSecurity());
                    break;
                case AMOUNT:
                    oStruct.put(tField.name(), getAmount());
                    break;
                case PRICE:
                    oStruct.put(tField.name(), getPrice());
                    break;
                case TYPE:
                    oStruct.put(tField.name(), getType());
                    break;

                case TIME:
                    oStruct.put(tField.name(), getTime());
                    break;
            }
        }
        return oStruct;
    }

    @Override
    public void fromStruct(Hashtable<String, Object> iStruct) throws StockPlayException {
        for (String tKey : iStruct.keySet()) {
            Object tValue = iStruct.get(tKey);
            Fields tField = null;
            try {
                tField = Fields.valueOf(tKey);
            }
            catch (IllegalArgumentException e) {
                throw new StockPlayException("requested key '" + tKey + "' does not exist");
            }

            switch (tField) {
                case USER:
                    setUser((Integer)tValue);
                    break;
                case SECURITY:
                    setSecurity((String)tValue);
                    break;
                case AMOUNT:
                    setAmount((Integer)tValue);
                    break;
                case PRICE:
                    setPrice((Double)tValue);
                    break;
                case TYPE:
                    setType(InstructionType.valueOf((String)tValue));
                    break;

                case TIME:
                    setTime((Date)tValue);
                    break;

                default:
                    throw new StockPlayException("requested key '" + tKey + "' cannot be modified");
            }
        }
    }
   
}