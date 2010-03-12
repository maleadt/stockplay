/*
 * Graph.java
 * StockPlay - Graph-object voor DOT output.
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
package filterdemo.graph;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tim
 */
public class Graph extends Element {
    //
    // Member data
    //

    private final String mName;
    private List<Element> mElements;


    //
    // Construction
    //

    public Graph(String iName) {
        mName = iName;
        mElements = new ArrayList<Element>();
    }


    //
    // Methods
    //

    public void addElement(Element iElement) {
        mElements.add(iElement);
    }

    public void render(PrintStream iStream) {
        iStream.println("graph " + mName + " {");

        for (Element tElement : mElements) {
            tElement.render(iStream);
        }

        iStream.println("}");
    }
}
