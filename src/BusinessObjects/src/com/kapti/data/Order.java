package com.kapti.data;

import com.kapti.exceptions.StockPlayException;
import java.util.Date;
import java.util.Hashtable;

public class Order extends Instruction {

    //
    // Member data
    //

    public static enum Fields {
        ID, USER, SECURITY, AMOUNT, PRICE, TYPE,    // Instruction.Fields
        STATUS, CREATIONTIME, EXPIRATIONTIME, EXECUTIONTIME, PAR1, PAR2
    }
    
    private OrderStatus status;
    private Date creationTime;
    private Date expirationTime;
    private Date executionTime;
    private int par1;
    private int par2;

    //
    // Construction
    //


    public Order(int id){
        super(id);
    }

    public Order(int id, int user, String security, int amount, double price, InstructionType type, OrderStatus status, Date creationTime, Date expirationTime, Date executionTime) {
        super(id, user, security, amount, price, type);
        this.status =status;
        this.creationTime = creationTime;
        this.expirationTime = expirationTime;
        this.executionTime = executionTime;
    }

    public Order(InstructionType type, int user, String security, int amount, int par1, int par2) {
        super(user, security, amount, type);
        this.par1 = par1;
        this.par2 = par2;
    }

    //
    // Methods
    //

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public Date getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Date executionTime) {
        this.executionTime = executionTime;
    }

    public Date getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Date expirationTime) {
        this.expirationTime = expirationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
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

                case STATUS:
                    oStruct.put(tField.name(), getStatus());
                    break;
                case CREATIONTIME:
                    oStruct.put(tField.name(), getCreationTime());
                    break;
                case EXPIRATIONTIME:
                    oStruct.put(tField.name(), getExpirationTime());
                    break;
                case EXECUTIONTIME:
                    oStruct.put(tField.name(), getExecutionTime());
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

                case STATUS:
                    setStatus(OrderStatus.valueOf((String)tValue));
                    break;
                case CREATIONTIME:
                    setCreationTime((Date)tValue);
                    break;
                case EXPIRATIONTIME:
                    setExpirationTime((Date)tValue);
                    break;
                case EXECUTIONTIME:
                    setExecutionTime((Date)tValue);
                    break;
                default:
                    throw new StockPlayException("requested key '" + tKey + "' cannot be modified");
            }
        }
    }

}