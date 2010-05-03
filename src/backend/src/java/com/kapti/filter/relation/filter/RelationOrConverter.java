/*
 * RelationAndConverter.java
 * StockPlay - Converter voor OR relatie.
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
package com.kapti.filter.relation.filter;

import com.kapti.exceptions.FilterException;
import com.kapti.filter.condition.Condition;
import com.kapti.filter.relation.Relation;
import com.kapti.filter.relation.RelationOr;

/**
 *
 * @author tim
 */
public class RelationOrConverter extends RelationOr {
    //
    // Construction
    //

    public RelationOrConverter(RelationOr iObject) {
        super(iObject);
    }


    //
    // Methods
    //

    @Override
    public Object process(Condition a, Condition b) throws FilterException {
        StringBuilder tRelation = new StringBuilder();

        // Obey precedence
        boolean tPrecedenceLeft = (a instanceof Relation && !(a instanceof RelationOr));
        boolean tPrecedenceRight = (b instanceof Relation && !(b instanceof RelationOr));

        if (tPrecedenceLeft)
            tRelation.append("(");
        tRelation.append((String)a.compile());
        if (tPrecedenceLeft)
            tRelation.append(")");

        tRelation.append(" || ");

        if (tPrecedenceRight)
            tRelation.append("(");
        tRelation.append((String)b.compile());
        if (tPrecedenceRight)
            tRelation.append(")");

        return tRelation.toString();
    }
}