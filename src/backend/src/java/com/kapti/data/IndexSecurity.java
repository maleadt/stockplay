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
 * \brief   Basisobject voor koppeling tussen indexen en aandelen
 *
 * Deze klasse wordt gebruikt om de koppeling tussen index en aandelen
 * te modelleren. Deze object wordt dan intern steeds gebruikt om die koppeling
 * voor te stellen, en linkt via unieke sleutels naar de objecten in kwestie. Het
 * biedt ook de nodige functionaliteit om zichzelf terug te converteren naar
 * een object dat over XML-RPC verstuurd kan worden, of om net zichzelf te
 * construeren of aan te passen aan de hand van dergelijke data.
 */
public class IndexSecurity implements Serializable {
    //
    // Member data
    //

    public static enum Fields {
        INDEX_ISIN, SECURITY_ISIN
    }
    public static Map<Fields, Class> Types = new HashMap<Fields, Class>() { {
            put(Fields.INDEX_ISIN, String.class);     // Deel van de
            put(Fields.SECURITY_ISIN, String.class);   // IndexSecurityPK
    } };
    
    private IndexSecurityPK pk;


    //
    // Construction
    //

    public IndexSecurity(String index_isin, String security_isin) {
        this.pk = new IndexSecurityPK(index_isin, security_isin);
    }


    //
    // Methods
    //

    public IndexSecurityPK getPk() {
        return pk;
    }

    public HashMap<String, Object> toStruct(Fields... iFields) {
        HashMap<String, Object> oStruct = new HashMap<String, Object>();
        for (Fields tField : iFields) {
            switch (tField) {
                case INDEX_ISIN:
                    oStruct.put(tField.name(), getPk().getIndexIsin());
                    break;
                case SECURITY_ISIN:
                    oStruct.put(tField.name(), getPk().getSecurityIsin());
                    break;
            }
        }
        return oStruct;
    }

    public void applyStruct(HashMap<String, Object> iStruct) throws StockPlayException {
        if (iStruct.size() > 0)
            throw new InvocationException(InvocationException.Type.READ_ONLY_KEY, "No keys can be modified");
    }

    public static IndexSecurity fromStruct(HashMap<String, Object> iStruct) throws StockPlayException {
        // Create case mapping
        HashMap<Fields, String> tStructMap = new HashMap<Fields, String>();
        for (String tKey : iStruct.keySet()) {
            Fields tField = null;
            try {
                tField = Fields.valueOf(tKey.toUpperCase());
            }
            catch (IllegalArgumentException e) {
                throw new InvocationException(InvocationException.Type.NON_EXISTING_ENTITY, "requested key '" + tKey + "' does not exist");
            }
            if (!Types.get(tField).isInstance(iStruct.get(tKey)))
                throw new InvocationException(InvocationException.Type.BAD_REQUEST, "provided key '" + tKey + "' requires a " + Types.get(tField) + " instead of an " + iStruct.get(tKey).getClass());
            tStructMap.put(tField, tKey);
        }

        // Check needed keys
        if (tStructMap.containsKey(Fields.INDEX_ISIN) && tStructMap.containsKey(Fields.SECURITY_ISIN)) {
            IndexSecurity tIndexSecurity = new IndexSecurity((String)iStruct.get(tStructMap.get(Fields.INDEX_ISIN)), (String)iStruct.get(tStructMap.get(Fields.SECURITY_ISIN)));
            iStruct.remove(tStructMap.get(Fields.INDEX_ISIN));
            iStruct.remove(tStructMap.get(Fields.SECURITY_ISIN));
            return tIndexSecurity;
        } else
            throw new ServiceException(ServiceException.Type.NOT_ENOUGH_INFORMATION);
    }


    //
    // Subclasses
    //

    public class IndexSecurityPK implements Serializable {
        private String index_isin;
        private String security_isin;

        public IndexSecurityPK(String index_isin, String security_isin) {
            this.index_isin = index_isin;
            this.security_isin = security_isin;
        }

        public String getIndexIsin() {
            return index_isin;
        }

        public String getSecurityIsin() {
            return security_isin;
        }
    }
}