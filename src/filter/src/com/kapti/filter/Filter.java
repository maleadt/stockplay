/*
 * Filter.java
 * StockPlay - Filter object.
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

import com.kapti.filter.graph.Graph;
import com.kapti.filter.condition.Condition;
import com.kapti.filter.exception.FilterException;
import com.kapti.filter.relation.Relation;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

/**
 *
 * @author tim
 */
public class Filter {
    //
    // Member data
    //

    private Condition mCondition;
    public Filter() {

    }


    //
    // Methods
    //

    /**
     * Voegt een enkele conditie toe, zonder een relatie te specifiëren. Dit is
     * handig wanneer de relatie reeds geconfigureerd is, of wanneer er geen
     * nodig is (in geval van een enkele conditie dus). Is hier niet aan voldaan,
     * zal de methode een FilterException opwerpen.
     *
     * @param iCondition
     */
    public void addCondition(Condition iCondition) throws FilterException {
        if (mCondition != null)
            throw new FilterException("Overwriting existing condition");
        mCondition = iCondition;
    }

    /**
     * Voegt een conditie toe, waarbij de gespecifieerde relatie toegepast wordt.
     * Is deze relatie nieuw, of dezelfde van diegene die op dit moment
     * gespecifieerd is, zal de condititie gewoon aan de filter toegevoegd worden.
     * Als de relatie verschilt van de huidige, dan zal de huidige set aan
     * condities verwijderd worden en toegevoegd worden aan een subfilter. Die
     * zal dan op zijn beurt toegevoegd worden als conditie, toegepast
     * op de nieuwe doorgegeven conditie en diens relatie.
     *
     * @param iRelation
     * @param iCondition
     */
    public void addCondition(Relation iRelation, Condition iCondition) throws FilterException {
        if (mCondition == null)
            throw new FilterException("When passing a relation, a condition should be defined already");
        iRelation.addParameter(mCondition);
        iRelation.addParameter(iCondition);
        mCondition = iRelation;
    }

    public Object compile() throws FilterException {
        return mCondition.compile();
    }

    public void debug(String iFilename) throws FilterException {
        // Create graph
        Graph graph = new Graph("digraph");
        mCondition.addNode(graph);

        // Render panel to file
        File tFile = null;
        try {
            tFile = new File(iFilename + ".dot");
            if (!tFile.exists())
                tFile.createNewFile();
            graph.render(new PrintStream(tFile));
        }
        catch (IOException e) {
            throw new FilterException("Problem creating output file", e.getCause());
        }

        // Render the DOT file to a PNG
        String[] cmd = { "/usr/bin/dot", "-Tpng", iFilename+".dot", "-o", iFilename+".png" };
        try {
            Process tConverter = Runtime.getRuntime().exec(cmd);

            tConverter.waitFor();
            if (tConverter.exitValue() != 0)
                throw new FilterException("Conversion failed");
        }
        catch (IOException e) {
            throw new FilterException("Problem executing child process to convert DOT code", e.getCause());
        }
        catch (InterruptedException e) {
            throw new FilterException("Was not allowed to wait for DOT output due to interrupt", e.getCause());
        }
    }
}
