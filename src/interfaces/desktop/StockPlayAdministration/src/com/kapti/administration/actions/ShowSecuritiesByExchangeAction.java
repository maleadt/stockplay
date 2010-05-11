/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.administration.actions;

import com.kapti.administration.MainFrame;
import com.kapti.administration.SecuritiesListPanel;
import com.kapti.client.finance.Exchange;
import java.awt.event.ActionEvent;
import javax.swing.Icon;

/**
 *
 * @author Thijs
 */
public class ShowSecuritiesByExchangeAction extends StockPlayAction {

    Exchange exch = null;

    public ShowSecuritiesByExchangeAction(String name, Icon icon, Exchange exchange) {
        super(name, icon);
        this.exch = exchange;
    }


    public void actionPerformed(ActionEvent e) {
        MainFrame.getInstance().setMainPanel(SecuritiesListPanel.getInstance());
        SecuritiesListPanel.getInstance().filterByExchange(exch);
    }

}
