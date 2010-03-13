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
import filterdemo.exception.FilterException;
import filterdemo.exception.ParserException;
import filterdemo.parsing.Parser;


/**
 *
 * @author tim
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Test parser
        Parser tParser = new Parser();

        // Parse input
        Filter tFilter = null;
        try {
            tFilter = tParser.parse("EQUALS('id' 42) AND EQUALS('name', 'Tim Besard') OR EQUALS('gender' 'M')");
        }
        catch (ParserException e) {
            System.err.println("Parsing failed");
            e.printStackTrace();
        }

        // Print a filter tree
        try {
            tFilter.debug("/tmp/AST");
        }
        catch (FilterException e) {
            System.out.println("Debug dump failed");
            e.printStackTrace();
        }

        // Compile output
        try {
            System.out.println("Compiled result: " + (String)tFilter.compile());
        }
        catch (FilterException e) {
            System.err.println("Compilation failed");
            e.printStackTrace();
        }
    }

}
