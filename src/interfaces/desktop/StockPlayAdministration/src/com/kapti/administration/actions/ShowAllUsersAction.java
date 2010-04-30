/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.administration.actions;

import com.kapti.administration.UsersListPanel;
import java.awt.event.ActionEvent;
import javax.swing.Icon;

/**
 *
 * @author Thijs
 */
public class ShowAllUsersAction extends ShowUsersAction {

    public ShowAllUsersAction(String name, Icon icon) {
        super(name, icon);
    }

    public ShowAllUsersAction(String name, Icon icon, boolean title) {
        super(name, icon, title);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        UsersListPanel.getInstance().removeFilter();
    }
}
