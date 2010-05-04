/*
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

package com.kapti.data.persistence;

import com.kapti.exceptions.StockPlayException;
import java.util.ResourceBundle;

public class StockPlayDAOFactory {

    public static StockPlayDAO getDAO() throws StockPlayException {
        try {
            StockPlayDAO spDAO = null;
            ResourceBundle rb = ResourceBundle.getBundle("com.kapti.data.persistence.StockPlayDAOFactory");
            spDAO = (StockPlayDAO) Class.forName(rb.getString("Class")).newInstance();
            return spDAO;
        } catch (InstantiationException ex) {
            throw new RuntimeException("Could not load properties-file with DAO-class", ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException("Could not load properties-file with DAO-class", ex);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("Could not find DAO-class", ex);
        }
    }

}
