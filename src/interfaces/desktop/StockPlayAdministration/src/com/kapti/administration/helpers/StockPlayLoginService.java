/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.administration.helpers;

import com.kapti.client.user.User;
import com.kapti.client.user.UserFactory;
import com.kapti.exceptions.StockPlayException;
import java.util.Collection;
import java.util.Iterator;
import org.apache.log4j.Logger;
import org.jdesktop.swingx.auth.LoginService;

/**
 *
 * @author Thijs
 */
public class StockPlayLoginService extends LoginService {

    private static Logger logger = Logger.getLogger(StockPlayLoginService.class);

    @Override
    public boolean authenticate(String name, char[] password, String server) throws Exception {

        try {
            if (UserFactory.getInstance().verifyLogin(name, new String(password))) {

                //we bekijken of de gebruiker genoeg rechten heeft
                Collection<User> users = UserFactory.getInstance().getUsersByFilter("nickname EQUALS '" + name + "'");
                Iterator<User> it = users.iterator();

                if (it.hasNext()) {
                    User u = it.next();
                    if (u.getRole() == User.Role.ADMIN) {
                        return true;
                    } else {
                        throw new StockPlayException("User exists, but doesn't have sufficient rights");
                    }
                }
            }
            return false;
        } catch (Exception ex) {
            logger.error("An exception occured while verifiying the login details", ex);
            throw new StockPlayException("An exception occured while verifiying the login details", ex);
        }
    }
}
