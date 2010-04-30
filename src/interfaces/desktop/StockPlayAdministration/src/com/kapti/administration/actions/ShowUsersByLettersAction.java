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
public class ShowUsersByLettersAction  extends ShowUsersAction{


    private String regex;

    public ShowUsersByLettersAction(String name, Icon icon, char beginLetter, char endLetter) {
        super(name, icon);
        regex = "^[" + Character.toUpperCase(beginLetter) + "-"+ Character.toUpperCase(endLetter)+"" + Character.toLowerCase(beginLetter) + "-" +Character.toLowerCase(endLetter)+"].*";
  
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        UsersListPanel.getInstance().setFilter(regex);

    }




}
