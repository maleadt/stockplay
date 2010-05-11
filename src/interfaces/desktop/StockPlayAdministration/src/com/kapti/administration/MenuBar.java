/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.administration;

import com.kapti.administration.actions.ActionsFactory;
import com.kapti.administration.helpers.StockPlayLoginScreen;
import com.kapti.administration.helpers.StockPlayLoginService;
import com.kapti.administration.helpers.StockPlayPasswordStore;
import com.kapti.administration.helpers.StockPlayPreferences;
import com.kapti.administration.helpers.StockPlayUsernameStore;
import com.kapti.client.user.User;
import com.kapti.client.user.UserFactory;
import com.kapti.exceptions.StockPlayException;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import org.jdesktop.swingx.JXLoginPane;
import org.jdesktop.swingx.JXLoginPane.JXLoginFrame;
import org.jdesktop.swingx.JXLoginPane.Status;

/**
 *
 * @author Thijs
 */
public class MenuBar extends JMenuBar {

    JCheckBoxMenuItem saveUsernamesMenuItem = null;
    JCheckBoxMenuItem savePasswordsMenuItem = null;
    JCheckBoxMenuItem loginWithEidMenuItem = null;

    public MenuBar() {

        StockPlayPreferences preferences = new StockPlayPreferences();
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
        for (Action act : actFactory.getSecuritiesActions()) {
            securitiesMenu.add(act);
        }
        add(securitiesMenu);

        JMenu usersMenu = new JMenu("Gebruikersbeheer");
        for (Action act : actFactory.getUsersActions()) {
            usersMenu.add(act);
        }
        add(usersMenu);


        JMenu settingsMenu = new JMenu("Instellingen");

        JMenuItem languageItem = new JMenuItem("Taal:");
        languageItem.setEnabled(false);
        languageItem.setFont(languageItem.getFont().deriveFont(Font.BOLD));
        settingsMenu.add(languageItem);

        ButtonGroup localesGroup = new ButtonGroup();
        for (Locale l : StockPlayPreferences.getSupportedLocales()) {
            JRadioButtonMenuItem langRadioItem = new JRadioButtonMenuItem(l.getDisplayName(l));
            if (l.equals(preferences.getLocale())) {
                langRadioItem.setSelected(true);
            }
            langRadioItem.addActionListener(new ChangeLanguageActionListener(l));
            localesGroup.add(langRadioItem);
            settingsMenu.add(langRadioItem);
        }

        settingsMenu.add(new JSeparator());

        JMenuItem loginItem = new JMenuItem("Login:");
        loginItem.setEnabled(false);
        loginItem.setFont(languageItem.getFont().deriveFont(Font.BOLD));
        settingsMenu.add(loginItem);

        saveUsernamesMenuItem = new JCheckBoxMenuItem("Gebruikersnamen opslaan");
        saveUsernamesMenuItem.setState(preferences.getSaveUsernames());
        saveUsernamesMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                StockPlayPreferences preferences = new StockPlayPreferences();
                preferences.setSaveUsernames(saveUsernamesMenuItem.getState());
            }
        });

        settingsMenu.add(saveUsernamesMenuItem);

        savePasswordsMenuItem = new JCheckBoxMenuItem("Paswoorden opslaan");
        savePasswordsMenuItem.setState(preferences.getSavePasswords());
        savePasswordsMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                StockPlayPreferences preferences = new StockPlayPreferences();
                preferences.setSavePasswords(savePasswordsMenuItem.getState());
            }
        });

        settingsMenu.add(savePasswordsMenuItem);



        loginWithEidMenuItem = new JCheckBoxMenuItem("Login met eID");
        loginWithEidMenuItem.setState(preferences.getLoginWithEid());
        loginWithEidMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                StockPlayPreferences prefs = new StockPlayPreferences();


                if (loginWithEidMenuItem.getState()) {

                    //credentials opvragen
                    JXLoginPane loginPane = new JXLoginPane();
                    JXLoginFrame loginFrame;
                    loginPane.setBannerText("Stockplay");
                    JOptionPane.showMessageDialog(null, "StockPlay heeft een administratoraccount nodig om het gebruik van de eID-login toe te laten\nGeef in het volgende scherm de nodige logingegevens op.", "eID-login instellingen", JOptionPane.INFORMATION_MESSAGE);

                    loginPane.setMessage("Geef een administratoraccount op: ");

                    loginFrame = JXLoginPane.showLoginFrame(loginPane);
                    loginFrame.setTitle("StockPlay eID login account");
                    loginFrame.setVisible(true);

                    loginFrame.addWindowListener(new WindowAdapter() {

                        @Override
                        public void windowClosed(WindowEvent e) {
                            StockPlayPreferences prefs = new StockPlayPreferences();
                            JXLoginFrame loginFrame = (JXLoginFrame) e.getSource();

                            if (loginFrame.getStatus() == Status.SUCCEEDED) {
                                prefs.setEidAdminUsername(loginFrame.getPanel().getUserName());
                                prefs.setEidAdminPassword(new String(loginFrame.getPanel().getPassword()));
                                prefs.setLoginWithEid(true);
                                JOptionPane.showMessageDialog(null, "Het activeren van het eID-loginscherm is gelukt!", "eID-login instelling", JOptionPane.INFORMATION_MESSAGE);


                            } else {

                                JOptionPane.showMessageDialog(null, "Het activeren van het eID-loginscherm is mislukt, probeer opnieuw!", "eID-login instelling", JOptionPane.ERROR_MESSAGE);
                                loginWithEidMenuItem.setState(false);

                            }
                        }
                    });




                }

            }
        });

        settingsMenu.add(loginWithEidMenuItem);

        add(settingsMenu);
    }

    private class ChangeLanguageActionListener implements ActionListener {

        private Locale locale;

        public ChangeLanguageActionListener(Locale locale) {
            this.locale = locale;
        }

        public void actionPerformed(ActionEvent e) {
            StockPlayPreferences preferences = new StockPlayPreferences();
            preferences.setLocale(locale);

            JOptionPane.showMessageDialog(null, "De taal zal worden aangepast de volgende keer als deze applicatie wordt gestart.");
        }
    }
}
