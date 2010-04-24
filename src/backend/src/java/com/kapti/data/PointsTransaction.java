/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.data;

import com.kapti.exceptions.InvocationException;
import com.kapti.exceptions.ServiceException;
import com.kapti.exceptions.StockPlayException;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 *
 * @author Thijs
 */
public class PointsTransaction {
    //
    // Member data
    //

    public static enum Fields {
        USER, TIMESTAMP, DELTA, COMMENTS
    }
    public static Map<Fields, Class> Types = new HashMap<Fields, Class>() { {
            put(Fields.USER, Integer.class);        // Deel van de
            put(Fields.TIMESTAMP, Date.class);      // PointsTransactionPK
            put(Fields.DELTA, Integer.class);
            put(Fields.COMMENTS, String.class);
    } };

    private int delta;
    private String comments;
    private PointsTransactionPK pk;


    //
    // Construction
    //

    public PointsTransaction(int user, Date timestamp) {
        this.pk = new PointsTransactionPK(user, timestamp);
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getDelta() {
        return delta;
    }

    public void setDelta(int delta) {
        this.delta = delta;
    }

    public Date getTimestamp() {
        return pk.getTimestamp();
    }

    public int getUser() {
        return pk.getUser();
    }

    public Hashtable<String, Object> toStruct(Fields... iFields) {
        Hashtable<String, Object> oStruct = new Hashtable<String, Object>();
        for (Fields tField : iFields) {
            switch (tField) {
                // Instruction.Fields
                case USER:
                    oStruct.put(tField.name(), getUser());
                    break;
                case TIMESTAMP:
                    oStruct.put(tField.name(), getTimestamp());
                    break;
                case DELTA:
                    oStruct.put(tField.name(), getDelta());
                    break;
                case COMMENTS:
                    oStruct.put(tField.name(), getComments());
                    break;
            }
        }
        return oStruct;
    }

    public void applyStruct(Hashtable<String, Object> iStruct) throws StockPlayException {
        for (String tKey : iStruct.keySet()) {
            Object tValue = iStruct.get(tKey);
            Fields tField = null;
            try {
                tField = Fields.valueOf(tKey.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new InvocationException(InvocationException.Type.KEY_DOES_NOT_EXIST, "requested key '" + tKey + "' does not exist");
            }
            if (!Types.get(tField).isInstance(iStruct.get(tKey)))
                throw new InvocationException(InvocationException.Type.BAD_REQUEST, "provided key '" + tKey + "' requires a " + Types.get(tField) + " instead of an " + iStruct.get(tKey).getClass());

            switch (tField) {
                case DELTA:
                    setDelta((Integer) tValue);
                    break;
                case COMMENTS:
                    setComments((String) tValue);
                    break;
                default:
                    throw new InvocationException(InvocationException.Type.READ_ONLY_KEY, "requested key '" + tKey + "' cannot be modified");
            }
        }
    }

    public static PointsTransaction fromStruct(Hashtable<String, Object> iStruct) throws StockPlayException {
        // Create case mapping
        Hashtable<Fields, String> tStructMap = new Hashtable<Fields, String>();
        for (String tKey : iStruct.keySet()) {
            Fields tField = null;
            try {
                tField = Fields.valueOf(tKey.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new InvocationException(InvocationException.Type.KEY_DOES_NOT_EXIST, "requested key '" + tKey + "' does not exist");
            }
            if (!Types.get(tField).isInstance(iStruct.get(tKey)))
                throw new InvocationException(InvocationException.Type.BAD_REQUEST, "provided key '" + tKey + "' requires a " + Types.get(tField) + " instead of an " + iStruct.get(tKey).getClass());
            tStructMap.put(tField, tKey);
        }

        // Check needed keys
        if (tStructMap.containsKey(Fields.USER) && tStructMap.containsKey(Fields.TIMESTAMP)) {
            PointsTransaction tTransaction = new PointsTransaction((Integer) iStruct.get(tStructMap.get(Fields.USER)), (Date) iStruct.get(tStructMap.get(Fields.TIMESTAMP)));
            iStruct.remove(tStructMap.get(Fields.USER));
            iStruct.remove(tStructMap.get(Fields.TIMESTAMP));
            return tTransaction;
        } else {
            throw new ServiceException(ServiceException.Type.NOT_ENOUGH_INFORMATION);
        }
    }
    
    
    //
    // Classes
    //

    public class PointsTransactionPK {

        private int user;
        private Date timestamp;

        public PointsTransactionPK(int user, Date timestamp) {
            this.user = user;
            this.timestamp = timestamp;
        }

        public Date getTimestamp() {
            return timestamp;
        }

        public int getUser() {
            return user;
        }
    }
}
