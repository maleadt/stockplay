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

import com.kapti.exceptions.FilterException;
import com.kapti.filter.graph.Graph;
import com.kapti.filter.graph.Node;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Abstracte klasse voor een converteerbaar object. Dit is een heel belangrijk
 * object, die voorziet in een interface van methodes die elk conveertbaar
 * object moet implementeren, maar ook een resem gedeelde functionaliteit
 * (zoals parameters, laden van converters, ...) implementeert.
 *
 * @author tim
 */
public abstract class Convertable {
    //
    // Member data
    //
    
    protected List<Convertable> mParameters = new ArrayList<Convertable>();


    //
    // Constructie
    //

    public Convertable(List<Convertable> iParameters) {
        mParameters = iParameters;
    }

    public Convertable(Convertable iObject) {
        mParameters = iObject.mParameters;
    }


    //
    // Methods
    //

    /**
     * Deze methode geeft een Convertable terug die de nodige functionaliteit
     * implementeert om het huidige object te compileren. Daarbij wordt
     * gekeken naar een properties file om te zien welke converter ingeladen moet
     * worden.
     */
    public final Convertable getConverter() throws FilterException {
        // Open the property file
        ResourceBundle rb = ResourceBundle.getBundle("com.kapti.filter.Converters");

        // Get the constructor of a converter
        String tObjectName = this.getClass().getPackage().getName() + "." + rb.getString("converter") + "." + this.getClass().getSimpleName() + "Converter";
        Constructor<?> tConstructor = null;
        try {
            Class<? extends Convertable> tObjectClass = Class.forName(tObjectName).asSubclass(Convertable.class);
            tConstructor = tObjectClass.getConstructor(this.getClass());
        }
        catch (ClassNotFoundException e) {
            throw new FilterException(FilterException.Type.FILTER_FAILURE, "could not find an appropriate converter for object '" + this.getClass().getSimpleName() + "'", e.getCause());
        }
        catch (NoSuchMethodException e) {
            throw new FilterException(FilterException.Type.FILTER_FAILURE, "could not find converter constructor at class '" + tObjectName + "'", e.getCause());
        }
        
        // Create a new object
        Object tObject = null;
        try {
            tObject = tConstructor.newInstance(this);
        }
        catch (InstantiationException e) {
            throw new FilterException(FilterException.Type.FILTER_FAILURE, "requested instantiation of converter '" + tObjectName + "' failed as it is not a concrete class", e.getCause());
        }
        catch (IllegalAccessException e) {
            throw new FilterException(FilterException.Type.FILTER_FAILURE, "illegal access to class '" + tObjectName + "'", e.getCause());
        }
        catch (InvocationTargetException e) {
            throw new FilterException(FilterException.Type.FILTER_FAILURE, "an exception occured when instantiating a converter of class '" + tObjectName + "'", e.getCause());
        }

        // Call the object
        Convertable tConverter = (Convertable) tObject;
        return tConverter;
    }

    /**
     * Compileert het huidige object door een Converter te instantiëren, en dan
     * daarop opnieuw de Compile functie aan te roepen. Het is dus duidelijk
     * dat Converters de compile() functie moeten overschrijven door een
     * effectieve implementatie, of een oneindige loop wordt gecreëerd.
     * 
     * @return
     * @throws FilterException
     */
    public Object compile() throws FilterException {
        return getConverter().compile();
    }

    @Override
    public int hashCode() {
        // Hash the type of data
        int code = this.getClass().hashCode();

        // Hash all children
        for (Convertable c : mParameters) {
            code = (31 * code) + c.hashCode();
        }

        return code;
    }


    //
    // Interface
    //

    /**
     * Elke convertable methode moet ook voorzien in een statische getSignature
     * methode, die de signatuur van de methode vastlegt. Java biedt echter
     * geen faciliteiten om een statische methode af te dwingen, vandaar
     * dit stukje commentaar.
     */

    /**
     * Verplicht te-implementeren methode, die instaat voor het toevoegen van
     * zichzelf aan de globale Graph boom.
     * 
     * @param iGraph
     * @return
     */
    public abstract Node addNode(Graph iGraph);

}
