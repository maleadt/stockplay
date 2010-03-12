/*
 * Parser.java
 * StockPlay - Parser voor string-filter conversie.
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
package filterdemo.parsing;

import filterdemo.Filter;
import filterdemo.exception.ParserException;
import filterdemo.exception.TokenizerException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author tim
 */
public class Parser {
    //
    // Member data
    //

    private Tokenizer mTokenizer;

    public static enum Type {
        INT, FLOAT,
        WORD, QUOTE,
        PARAM_OPEN, PARAM_CLOSE,
        WHITESPACE
    }


    //
    // Construction
    //

    public Parser() {
        List<Rule> tRules = new ArrayList<Rule>();
        tRules.add(new Rule(Type.INT, "[0-9]+"));
        tRules.add(new Rule(Type.FLOAT, "[0-9.]+"));
        tRules.add(new Rule(Type.WORD, "[A-Za-z_]+"));
        tRules.add(new Rule(Type.QUOTE, "\"([^\"]*+)\""));
        tRules.add(new Rule(Type.PARAM_OPEN, "\\("));
        tRules.add(new Rule(Type.PARAM_CLOSE, "\\)"));
        tRules.add(new Rule(Type.WHITESPACE, "\\s+"));
        mTokenizer = new Tokenizer(tRules);
    }


    //
    // Methods
    //

    public Filter parse(String iSource) throws ParserException, TokenizerException {
        // Tokenize the string
        List<Token> result = mTokenizer.tokenize(iSource);
        System.out.println("Parsed tokens: ");
        for (Token tToken : result) {
            System.out.println(tToken + ": " + tToken.getContent());
        }

        // Interprete the string
        Filter oFilter = interprete(result.iterator());

        return null;
    }

    // The FSM
    public Filter interprete(Iterator<Token> iIterator) throws ParserException {
        Filter oFilter = new Filter();

        // State
        Token tToken;
        String tCondition, tRelation;
        List tParameters = null;

        // Process the tokens
        while (iIterator.hasNext()) {
            tToken = iIterator.next();

            switch (tToken.getType()) {
                case INT: {
                    if (tParameters == null)
                        throw new ParserException("found raw integer out of parameter scope");
                    tParameters.add(Integer.parseInt(tToken.getContent()));

                    break;
                }
                case FLOAT: {
                    if (tParameters == null)
                        throw new ParserException("found raw float out of parameter scope");
                    tParameters.add(Double.parseDouble(tToken.getContent()));

                    break;
                }
                case WORD: {
                    // Check if word is a routine
                    if (true) {

                    }

                    // A regular string
                    // TODO: dit is onmogelijk, strings zijn altijd quoted
                    else {
                        if (tParameters == null)
                            throw new ParserException("found unknown string out of parameter scope");
                        tParameters.add(tToken.getContent());
                    }

                    break;
                }
                case QUOTE: {
                    if (tParameters == null)
                        throw new ParserException("found unknown quoted string out of parameter scope");
                    tParameters.add(tToken.getContent());

                    break;
                }
                case PARAM_OPEN: {
                    tParameters = new ArrayList();

                    break;
                }
                case PARAM_CLOSE: {
                    tParameters = null;

                    break;
                }
                case WHITESPACE: {


                    break;
                }
                default: {
                    throw new ParserException("unknown token");
                }
            }
        }


        return oFilter;
    }
}
