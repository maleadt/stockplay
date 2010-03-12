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
package filterdemo;

import filterdemo.graph.Graph;
import filterdemo.graph.Node;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 *
 * @author tim
 */
public abstract class Convertable {
    public void copyData(Convertable iObject) {
        mParameters = iObject.mParameters;
    }

    /**
     * Algemene conversiemethode. Wordt gebruik om de conditie om te zetten naar
     * een alternatief formaat, zoals een SQL WHERE filter.
     */
    public final Convertable getConverter() throws Exception {
        // Open the property file
        ResourceBundle rb = ResourceBundle.getBundle("filterdemo.Converters");

        // Get the constructor of a converter
        String tObjectName = this.getClass().getPackage().getName() + "." + rb.getString("converter") + "." + this.getClass().getSimpleName() + "Converter";
        Class tObjectClass = Class.forName(tObjectName);
        Constructor tConstructor = tObjectClass.getConstructor();
        
        // Create a new object
        Object tObject = tConstructor.newInstance();

        // Call the object
        Convertable tConverter = (Convertable) tObject;
        tConverter.copyData(this);
        tConverter.check();
        return tConverter;
    }

    public Object convert() throws Exception {
        return getConverter().convert();
    }
    
    abstract public void check();
    
    protected List<Convertable> mParameters = new ArrayList<Convertable>();
    public void addParameter(Convertable iParameter) {
        mParameters.add(iParameter);
    }
    public void setParameters(List<Convertable> iParameters) {
        mParameters = iParameters;
    }

    public abstract Node addNode(Graph iGraph);

}
