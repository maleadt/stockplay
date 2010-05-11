/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.administration.helpers;

import com.kapti.client.user.User;
import java.awt.event.ActionListener;

/**
 *
 * @author Thijs
 */
public interface LoginScreen {

    void addActionListener(ActionListener listener);

    User getUser();

    boolean isSuccess();

    void removeActionListener(ActionListener listener);

}
