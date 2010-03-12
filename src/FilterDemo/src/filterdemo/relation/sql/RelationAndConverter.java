/*
 * RelationAndConverter.java
 * StockPlay - Converter voor AND relatie.
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
package filterdemo.relation.sql;

import filterdemo.condition.Condition;
import filterdemo.relation.RelationAnd;

/**
 *
 * @author tim
 */
public class RelationAndConverter extends RelationAnd {
    @Override
    public Object process(Condition a, Condition b) throws Exception {
        return (String)a.convert() + " AND " + (String)b.convert();
    }
}