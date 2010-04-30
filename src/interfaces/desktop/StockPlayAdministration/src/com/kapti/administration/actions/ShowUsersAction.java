/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.administration.actions;

import com.kapti.administration.MainFrame;
import com.kapti.administration.UsersListPanel;
import java.awt.event.ActionEvent;
import javax.swing.Icon;

/**
 *
 * @author Thijs
 */
public class ShowUsersAction extends StockPlayAction{

    public ShowUsersAction(String name, Icon icon) {
        super(name, icon);
    }

    public ShowUsersAction(String name, Icon icon, boolean title) {
        super(name, icon, title);
    }

    public void actionPerformed(ActionEvent e) {
        MainFrame.getInstance().setMainPanel(UsersListPanel.getInstance());
    }

}
