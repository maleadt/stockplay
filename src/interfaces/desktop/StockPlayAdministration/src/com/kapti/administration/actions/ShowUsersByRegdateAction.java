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
public class ShowUsersByRegdateAction extends ShowUsersAction {


    long period;
    public ShowUsersByRegdateAction(String name, Icon icon, long period) {
        super(name, icon);
        this.period = period;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        UsersListPanel.getInstance().setFilter(period);
        
    }





}
