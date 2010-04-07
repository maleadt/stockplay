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
package com.kapti.filter.parsing;

import com.kapti.exceptions.FilterException;
import com.kapti.exceptions.StockPlayException;
import com.kapti.filter.Convertable;
import com.kapti.filter.Filter;
import com.kapti.filter.condition.Condition;
import com.kapti.filter.condition.ConditionEquals;
import com.kapti.filter.condition.ConditionGreaterThan;
import com.kapti.filter.condition.ConditionGreaterThanOrEqual;
import com.kapti.filter.condition.ConditionLessThan;
import com.kapti.filter.condition.ConditionLessThanOrEqual;
import com.kapti.filter.condition.ConditionLike;
import com.kapti.filter.condition.ConditionNot;
import com.kapti.filter.condition.ConditionNotLike;
import com.kapti.filter.data.Data;
import com.kapti.filter.data.DataDate;
import com.kapti.filter.data.DataFloat;
import com.kapti.filter.data.DataInt;
import com.kapti.filter.data.DataKey;
import com.kapti.filter.data.DataString;
import com.kapti.filter.relation.Relation;
import com.kapti.filter.relation.RelationAnd;
import com.kapti.filter.relation.RelationOr;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

/**
 *
 * @author tim
 */
public class Parser {
    //
    // Member data
    //

    static Logger mLogger = Logger.getLogger(Parser.class);

    public static enum TokenType {
        INT, FLOAT,
        WORD, QUOTE,
        LEFT_PARENTHESIS, RIGHT_PARENTHESIS, COMMA,
        WHITESPACE
    }

    private List<Rule<TokenType>> mTokenRules;
    private List<Rule<Class>> mOperatorRules;
    private List<Rule<Class>> mFunctionRules;
    private static Parser instance = new Parser();


    //
    // Construction
    //

    private Parser() {
        mLogger.info("instantiating Parser");

        // Create token ruleset
        mTokenRules = new ArrayList<Rule<TokenType>>();
        mTokenRules.add(new Rule(TokenType.INT, "[0-9]+"));
        mTokenRules.add(new Rule(TokenType.FLOAT, "[0-9.]+"));
        mTokenRules.add(new Rule(TokenType.WORD, "[A-Za-z_]+"));
        mTokenRules.add(new Rule(TokenType.QUOTE, "'([^\']*+)'([sdifk]?)"));
        mTokenRules.add(new Rule(TokenType.LEFT_PARENTHESIS, "\\("));
        mTokenRules.add(new Rule(TokenType.RIGHT_PARENTHESIS, "\\)"));
        mTokenRules.add(new Rule(TokenType.COMMA, ","));
        mTokenRules.add(new Rule(TokenType.WHITESPACE, "\\s+"));

        // Create operator ruleset
        mOperatorRules = new ArrayList<Rule<Class>>();
        mOperatorRules.add(new Rule(ConditionEquals.class, "^EQUALS$"));
        mOperatorRules.add(new Rule(ConditionGreaterThan.class, "^GREATERTHAN$"));
        mOperatorRules.add(new Rule(ConditionGreaterThanOrEqual.class, "^GREATERTHANOREQUAL$"));
        mOperatorRules.add(new Rule(ConditionLessThan.class, "^LESSTHAN$"));
        mOperatorRules.add(new Rule(ConditionLessThanOrEqual.class, "^LESSTHANOREQUAL$"));
        mOperatorRules.add(new Rule(ConditionNot.class, "^NOT"));
        mOperatorRules.add(new Rule(ConditionLike.class, "^LIKE"));
        mOperatorRules.add(new Rule(ConditionNotLike.class, "^NOTLIKE"));
        mOperatorRules.add(new Rule(RelationAnd.class, "^AND$"));
        mOperatorRules.add(new Rule(RelationOr.class, "^OR$"));

        // Create function ruleset
        mFunctionRules = new ArrayList<Rule<Class>>();
    }

    public static Parser getInstance() {
        return instance;
    }

    
    //
    // Methods
    //

    // Main method
    public Filter parse(String iSource) throws StockPlayException {
        mLogger.debug("parsing string '" + iSource + "'");

        // Lexical analysis
        List<Token> tInfix = tokenize(iSource);
        Queue<Token> tPostfix = infix_to_postfix(tInfix);
        
        // Syntactic analysis
        Filter oFilter = interprete(tPostfix);

        return oFilter;
    }

    // Lexical analysis: the tokenizer
    public List<Token> tokenize(String iSource) throws StockPlayException {
        // Setup
        int tPosition = 0;
        final int tEnd = iSource.length();
        List<Token> oTokens = new ArrayList<Token>();

        // Create a new matcher container
        Matcher tMatcher = Pattern.compile("dummy").matcher(iSource);
        tMatcher.useTransparentBounds(true).useAnchoringBounds(false);

        // Walk the string
        while (tPosition < tEnd) {
            tMatcher.region(tPosition, tEnd);

            // Check all rules
            List<Token> tMatches = new ArrayList<Token>();
            for (Rule<TokenType> tRule : mTokenRules) {
                if (tMatcher.usePattern(tRule.getPattern()).lookingAt()) {
                    Token tToken;

                    // Fetch the relevant content
                    int tContentGroup = 0;  // Group 0 equals the entire string
                    int tGroups = tMatcher.groupCount();
                    if (tGroups > 0) {
                        tContentGroup = 1;
                    }
                    String tContent = tContent = iSource.substring(tMatcher.start(tContentGroup), tMatcher.end(tContentGroup));

                    // Construct and save the token (minding eventual extra groups)
                    List<String> tExtra = new ArrayList<String>();
                    for (int i = 2; i <= tGroups; i++) {
                        String tExtraString = iSource.substring(tMatcher.start(i), tMatcher.end(i));
                        if (tExtraString.length() > 0)  // because groups are always present...
                            tExtra.add(tExtraString);
                    }
                    if (tExtra.size() == 0)
                        tExtra = null;
                    tToken = new Token(tRule.getType(), tMatcher.start(), tMatcher.end(), tContent, tExtra);
                    
                    tMatches.add(tToken);
                }
            }

            // Pick the longest match
            Token tTokenLongest = null;
            for (Token tToken : tMatches) {
                if (tTokenLongest == null || tToken.getLength() > tTokenLongest.getLength()) {
                    tTokenLongest = tToken;
                }
            }
            if (tTokenLongest != null) {
                oTokens.add(tTokenLongest);
                tPosition = tTokenLongest.getEnd();
            } else
                throw new FilterException(FilterException.Type.FILTER_FAILURE, "unknown character '" + iSource.substring(tPosition, tPosition+1) + "'");
        }
        return oTokens;
    }

    // Lexical analysis: the infix to postfix convertor (the shunting-yard algorithm)
    // TODO: support functions with variable amount of parameters
    //       http://www.kallisti.net.nz/blog/2008/02/extension-to-the-shunting-yard-algorithm-to-allow-variable-numbers-of-arguments-to-functions/
    public Queue<Token> infix_to_postfix(List<Token> iInfix) throws StockPlayException {
        Queue<Token> tQueue = new LinkedList<Token>();
        Stack<Token> tStack = new Stack<Token>();

        Iterator<Token> tIterator = iInfix.iterator();
        while (tIterator.hasNext()) {
            Token tToken = tIterator.next();

            switch (tToken.getType()) {
                case INT:
                case FLOAT:
                case QUOTE:
                    tQueue.add(tToken);
                    break;

                case COMMA:
                    if (tStack.isEmpty())
                        throw new FilterException(FilterException.Type.FILTER_FAILURE, "misplaced comma or mismatched parenthesis");
                    
                    while (tStack.peek().getType() != TokenType.LEFT_PARENTHESIS) {
                        tQueue.add(tStack.pop());
                        if (tStack.isEmpty())
                            throw new FilterException(FilterException.Type.FILTER_FAILURE, "misplaced comma or mismatched parenthesis");
                    }
                    break;

                case WORD:
                    // Token is a function
                    if (getFunction(tToken) != null) {
                        tStack.push(tToken);
                    }

                    // Token is an operator (condition or relation)
                    else if (getOperator(tToken) != null) {
                        Class tOperator1 = getOperator(tToken);
                        while (!tStack.isEmpty() && tStack.peek().getType() == TokenType.WORD && getOperator(tStack.peek()) != null) {
                            Class tOperator2 = getOperator(tStack.peek());

                            // Precedence of Relation < precedence of Condition
                            if (tOperator1.getSuperclass() == Relation.class && tOperator2.getSuperclass() == Condition.class) {
                                tQueue.add(tStack.pop());
                            }
                            else if (tOperator1.getSuperclass() == Condition.class && tOperator2.getSuperclass() == Relation.class) {
                                break;
                            }
                            else
                                throw new FilterException(FilterException.Type.FILTER_FAILURE, "precedence between equally-typed operators has not been defined (e.g. use brackets!)");
                        }
                        tStack.push(tToken);
                    }

                    // Token is a key
                    else {
                        tQueue.add(tToken);
                    }
                    break;

                case LEFT_PARENTHESIS:
                    tStack.push(tToken);
                    break;

                case RIGHT_PARENTHESIS:
                    if (tStack.isEmpty())
                        throw new FilterException(FilterException.Type.FILTER_FAILURE, "mismatched parenthesis");

                    while (!tStack.isEmpty() && tStack.peek().getType() != TokenType.LEFT_PARENTHESIS) {
                        tQueue.add(tStack.pop());
                        if (tStack.isEmpty())
                            throw new FilterException(FilterException.Type.FILTER_FAILURE, "mismatched parenthesis");
                    }
                    
                    tStack.pop();

                    if (!tStack.isEmpty() && tStack.peek().getType() == TokenType.WORD && getFunction(tStack.peek()) != null) {
                        tQueue.add(tStack.pop());
                    }
                    
                case WHITESPACE:
                    break;
                    
                default: {
                    throw new FilterException(FilterException.Type.FILTER_FAILURE, "unknown token " + tToken);
                }
            }
        }
        
        while (!tStack.isEmpty()) {
            Token tToken = tStack.pop();
            
            switch (tToken.getType()) {
                case LEFT_PARENTHESIS:
                case RIGHT_PARENTHESIS:
                    throw new FilterException(FilterException.Type.FILTER_FAILURE, "mismatched parenthesis");

                case WORD:
                    tQueue.add(tToken);
                    break;
                    
                default:
                    throw new FilterException(FilterException.Type.FILTER_FAILURE, "mismatched token " + tToken);
            }
        }
        
        return tQueue;
    }

    // Syntactic analysis: the interpreter
    public Filter interprete(Queue<Token> iTokens) throws StockPlayException {
        Filter oFilter = new Filter();
        Stack<Convertable> tStack = new Stack<Convertable>();

        if (iTokens.size() == 0)
            return oFilter;

        Iterator<Token> tIterator = iTokens.iterator();
        while (tIterator.hasNext()) {
            Token tToken = tIterator.next();

            switch (tToken.getType()) {
                case INT:
                    tStack.push(new DataInt(Integer.parseInt(tToken.getContent())));
                    break;
                    
                case FLOAT:
                    tStack.push(new DataFloat(Double.parseDouble(tToken.getContent())));
                    break;

                case QUOTE:
                    if (tToken.getExtra() == null) {
                        tStack.push(new DataString(tToken.getContent()));
                    } else if (tToken.getExtra().size() != 1) {
                        throw new FilterException(FilterException.Type.FILTER_FAILURE, "incorrect amount of extra data for quote construction");
                    } else {
                        String tModifier = tToken.getExtra().get(0);
                        if (tModifier.length() != 1) {
                            throw new FilterException(FilterException.Type.FILTER_FAILURE, "quote modifier can only be one character");
                        }

                        // Process all modifiers
                        Data tData = null;
                        if (tModifier.equals("s")) {
                            tData = new DataString(tToken.getContent());
                        } else if (tModifier.equals("i")) {
                            tData = new DataInt(Integer.parseInt(tToken.getContent()));
                        } else if (tModifier.equals("f")) {
                            tData = new DataFloat(Double.parseDouble(tToken.getContent()));
                        } else if (tModifier.equals("d")) {
                            tData = new DataDate(DataDate.parseDate(tToken.getContent()));
                        } else if (tModifier.equals("d")) {
                            tData = new DataKey(tToken.getContent());
                        } else {
                            throw new FilterException(FilterException.Type.FILTER_FAILURE, "unknown quote modifier '" + tModifier + "'");
                        }
                        tStack.push(tData);
                    }
                    
                    break;

                case WORD:
                    Class tOperator = getOperator(tToken);
                    Class tFunction = getFunction(tToken);
                    if (tOperator != null || tFunction != null) {
                        // Pick the class
                        Class tClass = tOperator;
                        if (tClass == null)
                            tClass = tFunction;

                        // Fetch parameter signature
                        Method[] tMethods = tClass.getMethods();
                        Method tSignatureMethod = null;
                        for (Method tMethod : tMethods)
                            if (Modifier.isStatic(tMethod.getModifiers()) && tMethod.getName().compareTo("getSignature") == 0)
                                tSignatureMethod = tMethod;
                        if (tSignatureMethod == null)
                            throw new FilterException(FilterException.Type.FILTER_FAILURE, "could not get parameter signature of class due to missing definition");
                        Class[] tParameterSignature;
                        try {
                            Object tReturn = tSignatureMethod.invoke(null);
                            tParameterSignature = (Class[]) tReturn;
                        }
                        catch (Exception e) {
                            throw new FilterException(FilterException.Type.FILTER_FAILURE, "could not get parameter signature of class", e.getCause());
                        }

                        // Handle parameters
                        int tParameterCount = tParameterSignature.length;
                        if (tStack.size() < tParameterCount)
                            throw new FilterException(FilterException.Type.FILTER_FAILURE, "parameter mismatch, I expected " + tParameterCount + " of them but only got " + tStack.size());
                        Vector<Convertable> tParameters = new Vector<Convertable>();
                        tParameters.setSize(tParameterCount);
                        for (int i = tParameterCount-1; i >= 0; i--) { // mind the reversion of the argument order
                            Convertable tParameter = tStack.pop();
                            Class tExpected = tParameterSignature[i];
                            if (!(tExpected.isInstance(tParameter)))
                                throw new FilterException(FilterException.Type.FILTER_FAILURE, "parameter mismatch, I expected a " + tExpected + " but got a " + tParameter.getClass());
                            tParameters.set(i, tParameter);
                        }

                        // Instantiate the object
                        Condition tCondition = null;
                        Object tObject = null;
                        try {
                            Constructor tConstructor = tClass.getConstructor(List.class);
                            tObject = tConstructor.newInstance(tParameters);
                        }
                        catch (Exception e) {
                            throw new FilterException(FilterException.Type.FILTER_FAILURE, "error instantiating operator", e.getCause());
                        }
                        if (!(tObject instanceof Condition))
                            throw new FilterException(FilterException.Type.FILTER_FAILURE, "attempt to instantiate non condition-typed operator (check the ruleset)");
                        tCondition = (Condition) tObject;

                        // Push the result
                        tStack.push(tCondition);
                    }
                    else {
                        tStack.push(new DataKey(tToken.getContent()));
                    }
                    break;
                
                default:
                    throw new FilterException(FilterException.Type.FILTER_FAILURE, "unknown token " + tToken);
            }
        }

        if (tStack.size() != 1) {
            throw new FilterException(FilterException.Type.FILTER_FAILURE, "filter evaluation failed: result count mismatch");
        }
        Convertable tRoot = tStack.pop();
        if (!(tRoot instanceof Condition))
            throw new FilterException(FilterException.Type.FILTER_FAILURE, "root node should be a condition");

        oFilter.setRoot((Condition)tRoot);

        return oFilter;
    }


    //
    // Auxiliary
    //

    private Class getOperator(Token iToken) {
        // Create a new matcher container
        Matcher tMatcher = Pattern.compile("dummy").matcher(iToken.getContent());
        tMatcher.useTransparentBounds(true).useAnchoringBounds(false);

        // Check all rules
        for (Rule<Class> tRule : mOperatorRules) {
            if (tMatcher.usePattern(tRule.getPattern()).lookingAt()) {
                return tRule.getType();
            }
        }
        return null;
    }

    private Class getFunction(Token iToken) {
        // Create a new matcher container
        Matcher tMatcher = Pattern.compile("dummy").matcher(iToken.getContent());
        tMatcher.useTransparentBounds(true).useAnchoringBounds(false);

        // Check all rules
        for (Rule<Class> tRule : mFunctionRules) {
            if (tMatcher.usePattern(tRule.getPattern()).lookingAt()) {
                return tRule.getType();
            }
        }
        return null;
    }
}