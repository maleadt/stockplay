/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.administration;

import com.kapti.administration.actions.ActionsFactory;
import com.kapti.administration.helpers.StockPlayPreferences;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;

/**
 *
 * @author Thijs
 */
public class MenuBar extends JMenuBar {


    public MenuBar() {

        ActionsFactory actFactory = ActionsFactory.getInstance();

        JMenu fileMenu = new JMenu("Bestand");

        JMenuItem exitItem = new JMenuItem("Afsluiten");
        exitItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                MainFrame.getInstance().dispose();
            }
        });
        fileMenu.add(exitItem);
        add(fileMenu);

        JMenu statusMenu = new JMenu("Status");

        for (Action act : actFactory.getStatusActions()) {
            statusMenu.add(act);
        }
        add(statusMenu);

        JMenu securitiesMenu = new JMenu("Effectenbeheer");
        for(Action act: actFactory.getSecuritiesActions())
            securitiesMenu.add(act);
        add(securitiesMenu);

        JMenu usersMenu = new JMenu("Gebruikersbeheer");
        for(Action act: actFactory.getUsersActions())
            usersMenu.add(act);
        add(usersMenu);


        JMenu settingsMenu = new JMenu("Instellingen");

        JMenuItem languageItem = new JMenuItem("Taal:");
        languageItem.setFont(languageItem.getFont().deriveFont(Font.BOLD));
        settingsMenu.add(languageItem);

        ButtonGroup localesGroup = new ButtonGroup();
        for (Locale l : StockPlayPreferences.getSupportedLocales()) {
            JRadioButtonMenuItem langRadioItem = new JRadioButtonMenuItem(l.getDisplayName(l));
            langRadioItem.addActionListener(new ChangeLanguageActionListener(l));
            localesGroup.add(langRadioItem);
            settingsMenu.add(langRadioItem);
        }


        add(settingsMenu);
    }


    private class ChangeLanguageActionListener implements ActionListener{

        private Locale locale;

        public ChangeLanguageActionListener(Locale locale){
            this.locale = locale;
        }

        public void actionPerformed(ActionEvent e) {
            StockPlayPreferences preferences = new StockPlayPreferences();
            preferences.setLocale(locale);

            JOptionPane.showMessageDialog(null, "De taal zal worden aangepast de volgende keer als deze applicatie wordt gestart.");
        }

    }
}
