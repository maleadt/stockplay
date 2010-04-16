/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.administration.helpers;

import com.kapti.administration.bo.user.User;
import com.kapti.administration.bo.user.UserFactory;
import java.util.Collection;
import java.util.Iterator;
import org.jdesktop.swingx.auth.LoginService;

/**
 *
 * @author Thijs
 */
public class StockPlayLoginService extends LoginService {



    @Override
    public boolean authenticate(String name, char[] password, String server) throws Exception {
        UserFactory uf = new UserFactory();
        if(uf.verifyLogin(name, new String(password))){

            //we bekijken of de gebruiker genoeg rechten heeft
            Collection<User> users = uf.getUsersByFilter("nickname EQUALS '" + name + "'");
            Iterator<User> it = users.iterator();

            if(it.hasNext()){
                User u = it.next();
                return u.getRole() == User.Role.ADMIN;
            }

        }
        return false;
    }
}
