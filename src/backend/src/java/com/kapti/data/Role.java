/*
 * Copyright (c) 2010 StockPlay development team
 * All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.kapti.data;

import com.kapti.exceptions.InvocationException;
import com.kapti.exceptions.ServiceException;
import com.kapti.exceptions.StockPlayException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashMap;
import java.util.Map;

/**
 * \brief   Basisobject voor rol-gerelateerde data
 *
 * Deze klasse wordt gebruikt om alle rol-gerelateerde data te verpakken
 * in een object dat dan verder intern gebruikt wordt binnen de backend. Het
 * biedt ook de nodige functionaliteit om zichzelf terug te converteren naar
 * een object dat over XML-RPC verstuurd kan worden, of om net zichzelf te
 * construeren of aan te passen aan de hand van dergelijke data.
 */
public class Role implements Serializable {
    //
    // Member data
    //

    public static enum Fields {

        ID,
        NAME,
        USER_REMOVE,
        SECURITY_CREATE,
        SECURITY_MODIFY,
        SECURITY_REMOVE,
        SECURITY_UPDATE,
        TRANSACTION_ADMIN,
        POINTS_ADMIN,
        BACKEND_ADMIN,
        DATABASE_ADMIN,
        SCRAPER_ADMIN
    }
    public static Map<Fields, Class> Types = new HashMap<Fields, Class>() {

        {
            put(Fields.ID, Integer.class);
            put(Fields.NAME, String.class);
            put(Fields.USER_REMOVE, Boolean.class);
            put(Fields.SECURITY_CREATE, Boolean.class);
            put(Fields.SECURITY_MODIFY, Boolean.class);
            put(Fields.SECURITY_REMOVE, Boolean.class);
            put(Fields.SECURITY_UPDATE, Boolean.class);
            put(Fields.TRANSACTION_ADMIN, Boolean.class);
            put(Fields.POINTS_ADMIN, Boolean.class);
            put(Fields.BACKEND_ADMIN, Boolean.class);
            put(Fields.DATABASE_ADMIN, Boolean.class);
            put(Fields.SCRAPER_ADMIN, Boolean.class);
        }
    };
    private int id;
    private String name;
    private boolean userRemove;
    private boolean securityCreate;
    private boolean securityModify;
    private boolean securityRemove;
    private boolean securityUpdate;
    private boolean transactionAdmin;
    private boolean pointsAdmin;
    private boolean backendAdmin;
    private boolean databaseAdmin;
    private boolean scraperAdmin;

    //
    // Construction
    //
    public Role(int id) {
        this.id = id;
    }

    public boolean isBackendAdmin() {
        return backendAdmin;
    }

    public void setBackendAdmin(boolean backendAdmin) {
        this.backendAdmin = backendAdmin;
    }

    public boolean isDatabaseAdmin() {
        return databaseAdmin;
    }

    public void setDatabaseAdmin(boolean databaseAdmin) {
        this.databaseAdmin = databaseAdmin;
    }


    public int getId() {
        return id;
    }

   
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isScraperAdmin() {
        return scraperAdmin;
    }

    public void setScraperAdmin(boolean scraperAdmin) {
        this.scraperAdmin = scraperAdmin;
    }

    public boolean isSecurityCreate() {
        return securityCreate;
    }

    public void setSecurityCreate(boolean securityCreate) {
        this.securityCreate = securityCreate;
    }

    public boolean isSecurityModify() {
        return securityModify;
    }

    public void setSecurityModify(boolean securityModify) {
        this.securityModify = securityModify;
    }

    public boolean isSecurityRemove() {
        return securityRemove;
    }

    public void setSecurityRemove(boolean securityRemove) {
        this.securityRemove = securityRemove;
    }

    public boolean isSecurityUpdate() {
        return securityUpdate;
    }

    public void setSecurityUpdate(boolean securityUpdate) {
        this.securityUpdate = securityUpdate;
    }

    public boolean isUserRemove() {
        return userRemove;
    }

    public void setUserRemove(boolean userRemove) {
        this.userRemove = userRemove;
    }

    public boolean isPointsAdmin() {
        return pointsAdmin;
    }

    public void setPointsAdmin(boolean pointsAdmin) {
        this.pointsAdmin = pointsAdmin;
    }

    public boolean isTransactionAdmin() {
        return transactionAdmin;
    }

    public void setTransactionAdmin(boolean transactionAdmin) {
        this.transactionAdmin = transactionAdmin;
    }




    //
    // Methods
    //
    public HashMap<String, Object> toStruct(Fields... iFields) {
        HashMap<String, Object> oStruct = new HashMap<String, Object>();
        for (Fields tField : iFields) {
            switch (tField) {
                case ID:
                    oStruct.put(tField.name(), getId());
                    break;
                case NAME:
                    oStruct.put(tField.name(), getName());
                    break;
                case USER_REMOVE:
                    oStruct.put(tField.name(), isUserRemove());
                    break;
                case SECURITY_CREATE:
                    oStruct.put(tField.name(), isSecurityCreate());
                    break;
                case SECURITY_MODIFY:
                    oStruct.put(tField.name(), isSecurityModify());
                    break;
                case SECURITY_REMOVE:
                    oStruct.put(tField.name(), isSecurityRemove());
                    break;
                case SECURITY_UPDATE:
                    oStruct.put(tField.name(), isSecurityUpdate());
                    break;
                case TRANSACTION_ADMIN:
                    oStruct.put(tField.name(), isTransactionAdmin());
                    break;
                case POINTS_ADMIN:
                    oStruct.put(tField.name(), isPointsAdmin());
                    break;
                case BACKEND_ADMIN:
                    oStruct.put(tField.name(), isBackendAdmin());
                    break;
                case DATABASE_ADMIN:
                    oStruct.put(tField.name(), isDatabaseAdmin());
                    break;
                case SCRAPER_ADMIN:
                    oStruct.put(tField.name(), isScraperAdmin());
                    break;

            }
        }
        return oStruct;
    }

    public void applyStruct(HashMap<String, Object> iStruct) throws StockPlayException {
        for (String tKey : iStruct.keySet()) {
            Object tValue = iStruct.get(tKey);
            Fields tField = null;
            try {
                tField = Fields.valueOf(tKey.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new InvocationException(InvocationException.Type.KEY_DOES_NOT_EXIST, "requested key '" + tKey + "' does not exist");
            }
            if (!Types.get(tField).isInstance(iStruct.get(tKey))) {
                throw new InvocationException(InvocationException.Type.BAD_REQUEST, "provided key '" + tKey + "' requires a " + Types.get(tField) + " instead of an " + iStruct.get(tKey).getClass());
            }

            switch (tField) {
               case NAME:
                   setName((String)tValue);
                    break;
                case USER_REMOVE:
                    setUserRemove((Boolean)tValue);
                    break;
                case SECURITY_CREATE:
                    setSecurityCreate((Boolean)tValue);
                    break;
                case SECURITY_MODIFY:
                    setSecurityModify((Boolean)tValue);
                    break;
                case SECURITY_REMOVE:
                    setSecurityRemove((Boolean)tValue);
                    break;
                case SECURITY_UPDATE:
                    setSecurityUpdate((Boolean)tValue);
                    break;
                case TRANSACTION_ADMIN:
                    setTransactionAdmin((Boolean)tValue);
                    break;
                case POINTS_ADMIN:
                    setPointsAdmin((Boolean)tValue);
                    break;
                case BACKEND_ADMIN:
                    setBackendAdmin((Boolean)tValue);
                    break;
                case DATABASE_ADMIN:
                    setDatabaseAdmin((Boolean)tValue);
                    break;
                case SCRAPER_ADMIN:
                    setScraperAdmin((Boolean)tValue);
                    break;

                default:
                    throw new InvocationException(InvocationException.Type.READ_ONLY_KEY, "requested key '" + tKey + "' cannot be modified");
            }
        }
    }

    public static Role fromStruct(HashMap<String, Object> iStruct) throws StockPlayException {
        // Create case mapping
        HashMap<Fields, String> tStructMap = new HashMap<Fields, String>();
        for (String tKey : iStruct.keySet()) {
            Fields tField = null;
            try {
                tField = Fields.valueOf(tKey.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new InvocationException(InvocationException.Type.KEY_DOES_NOT_EXIST, "requested key '" + tKey + "' does not exist");
            }
            if (!Types.get(tField).isInstance(iStruct.get(tKey))) {
                throw new InvocationException(InvocationException.Type.BAD_REQUEST, "provided key '" + tKey + "' requires a " + Types.get(tField) + " instead of an " + iStruct.get(tKey).getClass());
            }
            tStructMap.put(tField, tKey);
        }

        // Check needed keys
        if (tStructMap.containsKey(Fields.ID)) {
            Role tRole = new Role((Integer) iStruct.get(tStructMap.get(Fields.ID)));
            iStruct.remove(tStructMap.get(Fields.ID));
            return tRole;
        } else {
            throw new ServiceException(ServiceException.Type.NOT_ENOUGH_INFORMATION);
        }
    }
}
