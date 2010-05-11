/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.administration.actions;

import com.kapti.administration.MainFrame;
import com.kapti.administration.SecuritiesListPanel;
import com.kapti.client.finance.Exchange;
import com.kapti.client.finance.Index;
import java.awt.event.ActionEvent;
import javax.swing.Icon;

/**
 *
 * @author Thijs
 */
public class ShowSecuritiesByIndexAction extends StockPlayAction {

    Index index = null;

    public ShowSecuritiesByIndexAction(String name, Icon icon, Index index) {
        super(name, icon);
        this.index = index;
    }


    public void actionPerformed(ActionEvent e) {
        MainFrame.getInstance().setMainPanel(SecuritiesListPanel.getInstance());
        SecuritiesListPanel.getInstance().filterByIndex(index);
    }

}
