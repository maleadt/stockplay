/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.administration;

import com.kapti.administration.bo.finance.Exchange;
import com.kapti.administration.bo.finance.FinanceFactory;
import com.kapti.administration.tablemodels.SecuritiesTableModel;
import com.kapti.exceptions.StockPlayException;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    private MainFrame mainframe;
    private JXTaskPane statusMenuPane, securitiesMenuPane, usersMenuPane;
    private FinanceFactory financeFactory = new FinanceFactory();

    public Menu(MainFrame mainframe) {

        this.mainframe = mainframe;

        /////////////////
        // Status-menu //
        /////////////////

        statusMenuPane = new JXTaskPane();
        statusMenuPane.setTitle("Status");
        statusMenuPane.setIcon(createImageIcon("images/cog.png"));
        statusMenuPane.addPropertyChangeListener("collapsed", this);

        statusMenuPane.add(createMenuitem("Componenten", new ShowStatusOverviewActionListener(), "server", Font.BOLD));

        statusMenuPane.add(createMenuitem("Scraper", new ShowStatusOverviewActionListener(), "world"));
        statusMenuPane.add(createMenuitem("Database", new ShowStatusOverviewActionListener(), "database"));
        statusMenuPane.add(createMenuitem("Webserver", new ShowStatusOverviewActionListener(), "server"));
        statusMenuPane.add(createMenuitem("AI", new ShowStatusOverviewActionListener(), "ai"));

        statusMenuPane.add(new Box.Filler(new Dimension(0, 20), new Dimension(0, 20), new Dimension(0, 30)));

        statusMenuPane.add(createMenuitem("Status", new ShowStatusOverviewActionListener(), "chart_bar", Font.BOLD));

        add(statusMenuPane);

        /////////////////////////
        // Effectenbeheer-menu //
        /////////////////////////

        securitiesMenuPane = new JXTaskPane();
        securitiesMenuPane.setTitle("Effectenbeheer");
        securitiesMenuPane.setIcon(createImageIcon("images/money.png"));
        securitiesMenuPane.setCollapsed(true);
        securitiesMenuPane.addPropertyChangeListener("collapsed", this);

        securitiesMenuPane.add(createMenuitem("Beurzen", new ShowSecuritiesListActionListener(), "money", Font.BOLD));


        Collection<Exchange> exchanges = null;
        try {
            exchanges = financeFactory.getAllExchanges();
        } catch (StockPlayException ex) {
            JXErrorPane.showDialog(mainframe, new ErrorInfo("Fatal communication error", "A fatal error occured while fetching the exchanges", null, null, ex, null, null));
        }


        for (Exchange exch : exchanges) {
            securitiesMenuPane.add(createMenuitem(exch.getName(), new ShowSecuritiesListActionListener(exch), "money_euro"));
        }

        securitiesMenuPane.add(new Box.Filler(new Dimension(0, 20), new Dimension(0, 20), new Dimension(0, 30)));
        securitiesMenuPane.add(createMenuitem("Indexen", new ShowSecuritiesListActionListener(), "money", Font.BOLD));

        String[] indexes = new String[]{"Bel20", "AMS30", "CAC40", "Dow Jones"};
        for (String index : indexes) {
            securitiesMenuPane.add(createMenuitem(index, new ShowSecuritiesListActionListener(), "money_dollar"));
        }

        add(securitiesMenuPane);

        ///////////////////////////
        // Gebruikersbeheer-menu //
        ///////////////////////////

        usersMenuPane = new JXTaskPane();
        usersMenuPane.setTitle("Gebruikersbeheer");
        usersMenuPane.setIcon(createImageIcon("images/user.png"));
        usersMenuPane.setCollapsed(true);
        usersMenuPane.addPropertyChangeListener("collapsed", this);

        usersMenuPane.add(createMenuitem("Overzicht", new ShowUsersListActionListener(), "folder_user", Font.BOLD));

        usersMenuPane.add(new Box.Filler(new Dimension(0, 20), new Dimension(0, 20), new Dimension(0, 30)));
        usersMenuPane.add(createMenuitem("Alfabetisch zoeken", new ShowUsersListActionListener(), "folder_user", Font.BOLD));
        usersMenuPane.add(createMenuitem("A-E", new ShowUsersListActionListener(), "group"));
        usersMenuPane.add(createMenuitem("F-J", new ShowUsersListActionListener(), "group"));
        usersMenuPane.add(createMenuitem("K-O", new ShowUsersListActionListener(), "group"));
        usersMenuPane.add(createMenuitem("P-S", new ShowUsersListActionListener(), "group"));
        usersMenuPane.add(createMenuitem("T-Z", new ShowUsersListActionListener(), "group"));

        usersMenuPane.add(new Box.Filler(new Dimension(0, 20), new Dimension(0, 20), new Dimension(0, 30)));
        usersMenuPane.add(createMenuitem("Zoeken op registratiedatum", new ShowUsersListActionListener(), "folder_user", Font.BOLD));
        usersMenuPane.add(createMenuitem("Vandaag", new ShowUsersListActionListener(), "group"));
        usersMenuPane.add(createMenuitem("Afgelopen week", new ShowUsersListActionListener(), "group"));
        usersMenuPane.add(createMenuitem("Afgelopen maand", new ShowUsersListActionListener(), "group"));
        usersMenuPane.add(createMenuitem("Afgelopen jaar", new ShowUsersListActionListener(), "group"));
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
            if (!evt.getSource().equals(usersMenuPane)) {
                usersMenuPane.setCollapsed(true);
            }
        }
    }

    private JComponent createMenuitem(String name, ActionListener actionlistener, String iconname) {
        return createMenuitem(name, actionlistener, iconname, Font.PLAIN);
    }

    private JComponent createMenuitem(String name, ActionListener actionlistener, String iconname, int fontstyle) {

        JXHyperlink link = new JXHyperlink();
        link.setText(name);
        link.setClickedColor(link.getUnclickedColor()); //voorkomen dat link van kleur verandert
        if (fontstyle != Font.PLAIN) {
            link.setFont(link.getFont().deriveFont(fontstyle));
        }

        if (iconname != null) {
            link.setIcon(createImageIcon("images/" + iconname + ".png"));
        }
        link.addActionListener(actionlistener);

        return link;
    }

    /* public void actionPerformed(ActionEvent e) {

    MenuitemAction action = MenuitemAction.valueOf(e.getActionCommand());
    if (action.equals(MenuitemAction.StatusOverview)) {
    setMainPanel(StatusOverviewPanel.getInstance());
    } else if (action.equals(MenuitemAction.SecuritiesList)) {
    setMainPanel(SecuritiesListPanel.getInstance());
    } else if (action.equals(MenuitemAction.UsersList)) {
    setMainPanel(null);
    }
    }*/
    /**
     * Geeft een ImageIcon terug als de link valide was, anders null
     * @param path
     * @return
     */
    protected ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    private class ShowStatusOverviewActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            mainframe.setMainPanel(StatusOverviewPanel.getInstance());
        }
    }

    private class ShowSecuritiesListActionListener implements ActionListener {

        Exchange exchange = null;

        public ShowSecuritiesListActionListener() {
        }

        public ShowSecuritiesListActionListener(Exchange filter) {
            this.exchange = filter;
        }

        public void actionPerformed(ActionEvent e) {
            if (!(mainframe.getMainPanel() instanceof SecuritiesListPanel)) {
                mainframe.setMainPanel(SecuritiesListPanel.getInstance());
            }

            SecuritiesListPanel.getInstance().setFilter(exchange);

        }
    }

    private class ShowUsersListActionListener implements ActionListener {

        private String regex = "";

        public ShowUsersListActionListener() {
        }

        
        public ShowUsersListActionListener(String regex) {
            this.regex = regex;
        }



        public void actionPerformed(ActionEvent e) {
            mainframe.setMainPanel(null);
        }
    }
}
