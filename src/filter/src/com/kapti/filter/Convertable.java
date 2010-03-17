/*
 * Convertable.java
 * StockPlay - Interface voor converteerbare eenheid.
 *
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
package com.kapti.filter;

import com.kapti.filter.exception.FilterException;
import com.kapti.filter.graph.Graph;
import com.kapti.filter.graph.Node;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 *
 * @author tim
 */
public abstract class Convertable {
    //
    // Member data
    //
    
    protected List<Convertable> mParameters = new ArrayList<Convertable>();


    //
    // Methods
    //

    public void copyData(Convertable iObject) {
        mParameters = iObject.mParameters;
    }

    /**
     * Algemene conversiemethode. Wordt gebruik om de conditie om te zetten naar
     * een alternatief formaat, zoals een SQL WHERE filter.
     */
    public final Convertable getConverter() throws FilterException {
        // Open the property file
        ResourceBundle rb = ResourceBundle.getBundle("com.kapti.filter.Converters");

        // Get the constructor of a converter
        String tObjectName = this.getClass().getPackage().getName() + "." + rb.getString("converter") + "." + this.getClass().getSimpleName() + "Converter";
        Constructor tConstructor = null;
        try {
            Class tObjectClass = Class.forName(tObjectName);
            tConstructor = tObjectClass.getConstructor();
        }
        catch (ClassNotFoundException e) {
            throw new FilterException("could not find an appropriate converter for object '" + this.getClass().getSimpleName() + "'", e.getCause());
        }
        catch (NoSuchMethodException e) {
            throw new FilterException("could not find converter constructor at class '" + tObjectName + "'", e.getCause());
        }
        
        // Create a new object
        Object tObject = null;
        try {
            tObject = tConstructor.newInstance();
        }
        catch (InstantiationException e) {
            throw new FilterException("requested instantiation of converter '" + tObjectName + "' failed as it is not a concrete class", e.getCause());
        }
        catch (IllegalAccessException e) {
            throw new FilterException("illegal access to class '" + tObjectName + "'", e.getCause());
        }
        catch (InvocationTargetException e) {
            throw new FilterException("an exception occured when instantiating a converter of class '" + tObjectName + "'", e.getCause());
        }

        // Call the object
        Convertable tConverter = (Convertable) tObject;
        tConverter.copyData(this);
        return tConverter;
    }

    public Object compile() throws FilterException {
        return getConverter().compile();
    }
    
    public void addParameter(Convertable iParameter) {
        mParameters.add(iParameter);
    }
    public void setParameters(List<Convertable> iParameters) {
        mParameters = iParameters;
    }



    //
    // Interface
    //

    public abstract Class[] getParameterSignature();

    public abstract Node addNode(Graph iGraph);

}
