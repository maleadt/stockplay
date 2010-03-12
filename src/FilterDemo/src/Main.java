/*
 * Main.java
 * StockPlay - Main klasse voor filter demo.
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

import filterdemo.Filter;
import filterdemo.condition.*;
import filterdemo.data.*;
import filterdemo.exception.FilterException;
import filterdemo.relation.*;


/**
 *
 * @author tim
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Test filter
        Filter tFilter = new Filter();
        try {
            Condition tCheckIf42 = new ConditionEquals();
            tCheckIf42.addParameter(new DataKey("id"));
            tCheckIf42.addParameter(new DataInt(42));
            tFilter.addCondition(tCheckIf42);
            Condition tCheckIfTim = new ConditionEquals();
            tCheckIfTim.addParameter(new DataKey("name"));
            tCheckIfTim.addParameter(new DataString("Tim Besard"));
            tFilter.addCondition(new RelationOr(), tCheckIfTim);
        }
        catch (FilterException e) {
            System.err.println("Filter construction failed");
            e.printStackTrace();
        }

        // Convert filter
        try {
            String tSQL = (String) tFilter.convert();
            System.out.println("Conversion report: " + tSQL);
        }
        catch (Exception e) {
            System.out.println("Filtering failed");
            e.printStackTrace();
        }

        // Test debug
        try {
            tFilter.debug("/tmp/AST");
        }
        catch (FilterException e) {
            System.out.println("Debug dump failed");
            e.printStackTrace();
        }

        // Test parser
        //Parser tParser = new Parser();
        //tParser.parse("EQUALS(id 42) AND EQUALS(name \"Tim Besard\"");
    }

}
