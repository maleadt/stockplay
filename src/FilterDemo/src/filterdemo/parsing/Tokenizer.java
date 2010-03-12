/*
 * Tokenizer.java
 * StockPlay - Tokenizer voor interpretatie van filterstrings.
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

import filterdemo.exception.TokenizerException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author tim
 */
public class Tokenizer {
    //
    // Member data
    //

    List<Rule> mRules;

    public Tokenizer(List<Rule> iRules) {
        mRules = iRules;
    }


    //
    // Methods
    //

    List<Token> tokenize(String iSource) throws TokenizerException {
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
            for (Rule tRule : mRules) {
                if (tMatcher.usePattern(tRule.getPattern()).lookingAt()) {
                    // Fetch the relevant content
                    String tContent = null;
                    int tGroup = tMatcher.groupCount();
                    if (tGroup > 1) {
                        throw new TokenizerException("found multiple matching groups within rule");
                    }
                    tContent = iSource.substring(tMatcher.start(tGroup), tMatcher.end(tGroup));
                    
                    tMatches.add(new Token(tRule.getType(), tMatcher.start(), tMatcher.end(), tContent));
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
                tPosition++;    // TODO: warn, as we couldn't match anything?
        }
        return oTokens;
    }
}
