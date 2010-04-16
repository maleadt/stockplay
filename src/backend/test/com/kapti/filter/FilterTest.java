/*
 * FilterTest.java
 * StockPlay - Testcase voor de Filter component van de backend
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

import com.kapti.filter.parsing.Parser;
import com.kapti.filter.parsing.Parser.TokenType;
import com.kapti.filter.parsing.Token;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class FilterTest {

    private static Parser mParser = null;

    @BeforeClass
    public static void setUpClass() throws Exception {
        mParser = Parser.getInstance();

    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void tokenize() throws Exception {
        TokenType[] tTokenTypes = {TokenType.INT, TokenType.WHITESPACE, TokenType.FLOAT, TokenType.WORD, TokenType.QUOTE, TokenType.LEFT_PARENTHESIS, TokenType.RIGHT_PARENTHESIS, TokenType.COMMA};
        String[] tTokenValues = {"5", " ", "1.6", "FOO", "BAR", "(", ")", ","};

        List<Token> tTokens = mParser.tokenize("5 1.6FOO'BAR'(),");

        Assert.assertEquals("aantal tokens niet corrent", tTokenTypes.length, tTokens.size());
        for (int i = 0; i < tTokens.size(); i++) {
            Assert.assertEquals("tokentype komt niet overeen", tTokenTypes[i], tTokens.get(i).getType());
            Assert.assertEquals("token inhoud komt niet overeen", tTokenValues[i], tTokens.get(i).getContent());
        }
    }

    @Test
    public void infix_to_postfix() throws Exception {
        TokenType[] tTokenTypes = {TokenType.WORD, TokenType.FLOAT, TokenType.OPERATOR_STRICTGREATER, TokenType.WORD, TokenType.INT, TokenType.OPERATOR_EQUALS, TokenType.OPERATOR_AND};
        String[] tTokenValues = {"foo", "1.2", "GREATERTHAN", "answer", "42", "EQUALS", "AND"};

        List<Token> tTokens = mParser.tokenize("foo GREATERTHAN 1.2 AND answer EQUALS 42");
        Queue<Token> tTokensInfix = mParser.infix_to_postfix(tTokens);

        int i = 0;
        Iterator<Token> tIterator = tTokensInfix.iterator();
        while (tIterator.hasNext()) {
            Token tToken = tIterator.next();
            Assert.assertEquals("tokentype komt niet overeen", tTokenTypes[i], tToken.getType());
            Assert.assertEquals("token inhoud komt niet overeen", tTokenValues[i], tToken.getContent());
            i++;
        }
    }
}