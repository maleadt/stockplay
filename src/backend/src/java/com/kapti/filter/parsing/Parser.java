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
import com.kapti.filter.condition.*;
import com.kapti.filter.condition.ConditionGreater;
import com.kapti.filter.data.Data;
import com.kapti.filter.data.DataDate;
import com.kapti.filter.data.DataFloat;
import com.kapti.filter.data.DataInt;
import com.kapti.filter.data.DataKey;
import com.kapti.filter.data.DataRegex;
import com.kapti.filter.data.DataString;
import com.kapti.filter.relation.RelationAnd;
import com.kapti.filter.relation.RelationOr;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
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
        OPERATOR_EQUALS, OPERATOR_NOTEQUALS, OPERATOR_LESS, OPERATOR_GREATER, OPERATOR_STRICTLESS, OPERATOR_STRICTGREATER, OPERATOR_LIKE, OPERATOR_NOTLIKE, OPERATOR_AND, OPERATOR_OR,
        WHITESPACE
    }

    private List<Rule<TokenType>> mTokenRules;
    private Map<TokenType, Class<? extends Condition>> mOperatorMap;
    //private Map<TokenType, Class> mFunctionMap;
    private static Parser instance = new Parser();


    //
    // Construction
    //

    private Parser() {
        mLogger.info("instantiating Parser");

        // Create token ruleset (lower items have higher priority)
        mTokenRules = new ArrayList<Rule<TokenType>>();
        mTokenRules.add(new Rule<TokenType>(TokenType.WHITESPACE, "\\s+"));
        mTokenRules.add(new Rule<TokenType>(TokenType.LEFT_PARENTHESIS, "\\("));
        mTokenRules.add(new Rule<TokenType>(TokenType.RIGHT_PARENTHESIS, "\\)"));
        mTokenRules.add(new Rule<TokenType>(TokenType.COMMA, ","));
        mTokenRules.add(new Rule<TokenType>(TokenType.WORD, "[A-Za-z_]+"));
        mTokenRules.add(new Rule<TokenType>(TokenType.QUOTE, "'([^\']*+)'([rksdifk]*)"));
        mTokenRules.add(new Rule<TokenType>(TokenType.FLOAT, "-?[0-9.]+"));
        mTokenRules.add(new Rule<TokenType>(TokenType.INT, "-?[0-9]+"));
        mTokenRules.add(new Rule<TokenType>(TokenType.OPERATOR_EQUALS, "(==|EQUALS)"));
        mTokenRules.add(new Rule<TokenType>(TokenType.OPERATOR_NOTEQUALS, "(!=|NOT)"));
        mTokenRules.add(new Rule<TokenType>(TokenType.OPERATOR_LESS, "(<=|LESSTHANOREQUAL)"));
        mTokenRules.add(new Rule<TokenType>(TokenType.OPERATOR_GREATER, "(>=|GREATERTHANOREQUAL)"));
        mTokenRules.add(new Rule<TokenType>(TokenType.OPERATOR_STRICTLESS, "(<|LESSTHAN)"));
        mTokenRules.add(new Rule<TokenType>(TokenType.OPERATOR_STRICTGREATER, "(>|GREATERTHAN)"));
        mTokenRules.add(new Rule<TokenType>(TokenType.OPERATOR_LIKE, "(=~|LIKE)"));
        mTokenRules.add(new Rule<TokenType>(TokenType.OPERATOR_NOTLIKE, "(!~|NOTLIKE)"));
        mTokenRules.add(new Rule<TokenType>(TokenType.OPERATOR_AND, "(&&|AND)"));
        mTokenRules.add(new Rule<TokenType>(TokenType.OPERATOR_OR, "(\\|\\||OR)"));
        
        // Create operator translation map
        mOperatorMap = new HashMap<TokenType, Class<? extends Condition>>();
        mOperatorMap.put(TokenType.OPERATOR_EQUALS, ConditionEquals.class);
        mOperatorMap.put(TokenType.OPERATOR_NOTEQUALS, ConditionNotEquals.class);
        mOperatorMap.put(TokenType.OPERATOR_STRICTLESS, ConditionStrictLess.class);
        mOperatorMap.put(TokenType.OPERATOR_LESS, ConditionLess.class);
        mOperatorMap.put(TokenType.OPERATOR_STRICTGREATER, ConditionStrictGreater.class);
        mOperatorMap.put(TokenType.OPERATOR_GREATER, ConditionGreater.class);
        mOperatorMap.put(TokenType.OPERATOR_LIKE, ConditionLike.class);
        mOperatorMap.put(TokenType.OPERATOR_NOTLIKE, ConditionNotLike.class);
        mOperatorMap.put(TokenType.OPERATOR_AND, RelationAnd.class);    // TODO: relation in operator ruleset?
        mOperatorMap.put(TokenType.OPERATOR_OR, RelationOr.class);
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
                    String tContent = iSource.substring(tMatcher.start(tContentGroup), tMatcher.end(tContentGroup));

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
                if (tTokenLongest == null || tToken.getLength() >= tTokenLongest.getLength()) {
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

                case OPERATOR_EQUALS:
                case OPERATOR_NOTEQUALS:
                case OPERATOR_LESS:
                case OPERATOR_GREATER:
                case OPERATOR_STRICTLESS:
                case OPERATOR_STRICTGREATER:
                case OPERATOR_LIKE:
                case OPERATOR_NOTLIKE:
                case OPERATOR_AND:
                case OPERATOR_OR:
                    while (!tStack.isEmpty() && isOperator(tStack.peek())) {
                        Token tToken2 = tStack.peek();

                        // Precedence of Relation < precedence of Condition
                        if (isRelation(tToken) && isCondition(tToken2)) {
                            tQueue.add(tStack.pop());
                        }
                        else if (isCondition(tToken) && isRelation(tToken2)) {
                            break;
                        }

                        // Assume left-precedence for equal conditions
                        else if (isRelation(tToken) && isRelation(tToken2) && tToken.getType() == tToken2.getType()) {
                            break;
                        }
                        else
                            throw new FilterException(FilterException.Type.FILTER_FAILURE, "I cannot make up the operator-precedence here, please use brackets");
                    }
                    tStack.push(tToken);
                    break;

                case WORD:
                    // Token is a key
                    tQueue.add(tToken);
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

                    //if (!tStack.isEmpty() && isFunction(tStack.peek()) {
                    //    tQueue.add(tStack.pop());
                    //}

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
                case OPERATOR_EQUALS:
                case OPERATOR_NOTEQUALS:
                case OPERATOR_LESS:
                case OPERATOR_GREATER:
                case OPERATOR_STRICTLESS:
                case OPERATOR_STRICTGREATER:
                case OPERATOR_LIKE:
                case OPERATOR_NOTLIKE:
                case OPERATOR_AND:
                case OPERATOR_OR:
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
                        // TODO: getExtra no array, only return 1.
                        throw new FilterException(FilterException.Type.FILTER_FAILURE, "incorrect amount of extra data for quote construction");
                    } else {
                        String tModifiers = tToken.getExtra().get(0);

                        // Process the type modifier
                        Data tData = null;
                        String tType = tModifiers.substring(0, 1);
                        if (tType.equals("s")) {
                            tData = new DataString(tToken.getContent());
                        } else if (tType.equals("i")) {
                            tData = new DataInt(Integer.parseInt(tToken.getContent()));
                        } else if (tType.equals("f")) {
                            tData = new DataFloat(Double.parseDouble(tToken.getContent()));
                        } else if (tType.equals("d")) {
                            tData = new DataDate(DataDate.parseDate(tToken.getContent()));
                        } else if (tType.equals("d")) {
                            tData = new DataKey(tToken.getContent());
                        } else if (tType.equals("r")) {
                            tData = new DataRegex(tToken.getContent());
                        } else {
                            throw new FilterException(FilterException.Type.FILTER_FAILURE, "unknown type modifier '" + tType + "'");
                        }

                        // Process extra modifiers
                        if (tModifiers.length() > 1) {
                            String tExtraModifiers = tModifiers.substring(1);

                            if (tData instanceof DataRegex) {
                                ((DataRegex)tData).setModifiers(tExtraModifiers.toCharArray());
                            } else {
                                throw new FilterException(FilterException.Type.FILTER_FAILURE, "datatype doesn't accept extra modifiers");
                            }
                        }

                        tStack.push(tData);
                    }

                    break;
                
                case OPERATOR_EQUALS:
                case OPERATOR_NOTEQUALS:
                case OPERATOR_LESS:
                case OPERATOR_GREATER:
                case OPERATOR_STRICTLESS:
                case OPERATOR_STRICTGREATER:
                case OPERATOR_LIKE:
                case OPERATOR_NOTLIKE:
                case OPERATOR_AND:
                case OPERATOR_OR:
                    // Pick the class
                    Class<? extends Condition> tClass = mOperatorMap.get(tToken.getType());

                    // Fetch parameter signature
                    Method[] tMethods = tClass.getMethods();
                    Method tSignatureMethod = null;
                    for (Method tMethod : tMethods)
                        if (Modifier.isStatic(tMethod.getModifiers()) && tMethod.getName().compareTo("getSignature") == 0)
                            tSignatureMethod = tMethod;
                    if (tSignatureMethod == null)
                        throw new FilterException(FilterException.Type.FILTER_FAILURE, "could not get parameter signature of class due to missing definition");
                    Class[] tParameterSignature;    // TODO: ? extends Data
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
                    break;

                case WORD:
                    tStack.push(new DataKey(tToken.getContent()));
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

    private boolean isOperator(Token iToken) {
        switch (iToken.getType()) {
            case OPERATOR_EQUALS:
            case OPERATOR_NOTEQUALS:
            case OPERATOR_LESS:
            case OPERATOR_GREATER:
            case OPERATOR_STRICTLESS:
            case OPERATOR_STRICTGREATER:
            case OPERATOR_LIKE:
            case OPERATOR_NOTLIKE:
            case OPERATOR_AND:
            case OPERATOR_OR:
                return true;

            default:
                return false;
        }
    }

    private boolean isCondition(Token iToken) {
        switch (iToken.getType()) {
            case OPERATOR_EQUALS:
            case OPERATOR_NOTEQUALS:
            case OPERATOR_LESS:
            case OPERATOR_GREATER:
            case OPERATOR_STRICTLESS:
            case OPERATOR_STRICTGREATER:
            case OPERATOR_LIKE:
            case OPERATOR_NOTLIKE:
                return true;

            default:
                return false;
        }
    }

    private boolean isRelation(Token iToken) {
        switch (iToken.getType()) {
            case OPERATOR_AND:
            case OPERATOR_OR:
                return true;

            default:
                return false;
        }
    }
}