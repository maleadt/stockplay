/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.administration.actions;

import com.kapti.administration.MainFrame;
import com.kapti.administration.StatusOverviewPanel;
import java.awt.event.ActionEvent;
import javax.swing.Icon;

/**
 *
 * @author Thijs
 */
public class ShowStatusAction extends StockPlayAction {

    public ShowStatusAction(String name, Icon icon) {
        super(name, icon);
    }

    public ShowStatusAction(String name, Icon icon, boolean title) {
        super(name, icon, title);
    }



    public void actionPerformed(ActionEvent e) {

        MainFrame.getInstance().setMainPanel(StatusOverviewPanel.getInstance());

    }
}
