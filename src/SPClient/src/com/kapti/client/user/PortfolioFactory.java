/*
 * PortfolioFactory.java
 * StockPlay - Fabriek die de portfolios van de gebruikers kan teruggeven.
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

package com.kapti.client.user;

import com.kapti.client.XmlRpcClientFactory;
import com.kapti.exceptions.RequestError;
import com.kapti.exceptions.StockPlayException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;

/**
 *
 * \brief   Fabriek die de portfolios van de gebruikers kan teruggeven.
 *
 */


public class PortfolioFactory {

    private static PortfolioFactory instance = new PortfolioFactory();

    public static PortfolioFactory getInstance() {
        return instance;
    }

    private PortfolioFactory() {};

    public Collection<UserSecurity> getPortfolioByUser(User user) throws StockPlayException {

        ArrayList<UserSecurity> result = new ArrayList<UserSecurity>();
        try {
            XmlRpcClient client = XmlRpcClientFactory.getXmlRpcClient();
            Object[] userSecurities = (Object[]) client.execute("User.Portfolio.List", new Object[]{"USERID == " + user.id});

            for (Object obj : userSecurities) {
                HashMap geg = (HashMap) obj;
                geg.put("USEROBJECT", user);
                result.add(UserSecurity.fromStruct(geg));
            }
            return result;

        } catch (XmlRpcException ex) {
            throw new RequestError(ex);
        }
    }
}
