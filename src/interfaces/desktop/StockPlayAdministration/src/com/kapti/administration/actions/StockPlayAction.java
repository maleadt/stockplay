/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.administration.actions;

import javax.swing.AbstractAction;
import javax.swing.Icon;

/**
 *
 * @author Thijs
 */
public abstract class StockPlayAction extends AbstractAction {

    public StockPlayAction(String name, Icon icon, boolean title) {
        if (title) {
            name = "<html><b>" + name + "</b></html>";
            this.putValue(Layout.LEAVE_SPACE, true);
        } else {
            this.putValue(Layout.LEAVE_SPACE, false);
        }

        this.putValue(AbstractAction.NAME, name);
        this.putValue(AbstractAction.SMALL_ICON, icon);
    }

    public StockPlayAction(String name, Icon icon) {
        this(name, icon, false);
    }
}
