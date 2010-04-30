/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.administration;

import com.kapti.administration.actions.ActionsFactory;
import com.kapti.administration.actions.Layout;
import com.kapti.client.finance.Exchange;
import com.kapti.client.finance.FinanceFactory;
import com.kapti.exceptions.StockPlayException;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.util.Collection;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.error.ErrorInfo;

/**
 *
 * @author Thijs
 */
public class Menu extends JXTaskPaneContainer implements PropertyChangeListener {

    private static final ResourceBundle translations = ResourceBundle.getBundle("com/kapti/administration/translations");
    private MainFrame mainframe;
    private JXTaskPane statusMenuPane, securitiesMenuPane, usersMenuPane;
    private FinanceFactory financeFactory = FinanceFactory.getInstance();

    public Menu(MainFrame mainframe) {

        this.mainframe = mainframe;


        ActionsFactory actFactory = ActionsFactory.getInstance();

        /////////////////
        // Status-menu //
        /////////////////

        statusMenuPane = new JXTaskPane();
        statusMenuPane.setTitle(translations.getString("STATUSMENU"));
        statusMenuPane.setIcon(createImageIcon("cog.png"));
        statusMenuPane.addPropertyChangeListener("collapsed", this);

        for (Action act : actFactory.getStatusActions()) {
            if (((Boolean) act.getValue(Layout.LEAVE_SPACE))) {
                statusMenuPane.add(new Box.Filler(new Dimension(0, 20), new Dimension(0, 20), new Dimension(0, 30)));
            }
            statusMenuPane.add(act);
        }

        add(statusMenuPane);

        /////////////////////////
        // Effectenbeheer-menu //
        /////////////////////////

        securitiesMenuPane = new JXTaskPane();
        securitiesMenuPane.setTitle(translations.getString("SECURITIESMANAGEMENTMENUITEM"));
        securitiesMenuPane.setIcon(createImageIcon("money.png"));
        securitiesMenuPane.setCollapsed(true);
        securitiesMenuPane.addPropertyChangeListener("collapsed", this);

        for (Action act : actFactory.getSecuritiesActions()) {
            if (((Boolean) act.getValue(Layout.LEAVE_SPACE))) {
                securitiesMenuPane.add(new Box.Filler(new Dimension(0, 20), new Dimension(0, 20), new Dimension(0, 30)));
            }
            securitiesMenuPane.add(act);
        }

        add(securitiesMenuPane);

        ///////////////////////////
        // Gebruikersbeheer-menu //
        ///////////////////////////

        usersMenuPane = new JXTaskPane();
        usersMenuPane.setTitle(translations.getString("USERMANAGEMENTMENUITEM"));
        usersMenuPane.setIcon(createImageIcon("user.png"));
        usersMenuPane.setCollapsed(true);
        usersMenuPane.addPropertyChangeListener("collapsed", this);


        for (Action act : actFactory.getUsersActions()) {
            if (((Boolean) act.getValue(Layout.LEAVE_SPACE))) {
                usersMenuPane.add(new Box.Filler(new Dimension(0, 20), new Dimension(0, 20), new Dimension(0, 30)));
            }
            usersMenuPane.add(act);
        }
        add(usersMenuPane);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getNewValue().equals(false)) {
            if (evt.getSource().equals(statusMenuPane)) {
                mainframe.setMainPanel(StatusOverviewPanel.getInstance());
            } else {

                statusMenuPane.setCollapsed(true);
            }
            if (evt.getSource().equals(securitiesMenuPane)) {
                mainframe.setMainPanel(SecuritiesListPanel.getInstance());
            } else {
                securitiesMenuPane.setCollapsed(true);
            }
            if (evt.getSource().equals(usersMenuPane)) {
                mainframe.setMainPanel(UsersListPanel.getInstance());
            } else {
                usersMenuPane.setCollapsed(true);
            }
        }
    }

 
    protected ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = getClass().getResource("/com/kapti/administration/images/" + path);

        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println(translations.getString("ERROR_FILE_UNFINDABLE") + path);
            return null;
        }
    }
}
