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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;
import java.util.ResourceBundle;
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
    private static final ResourceBundle translations = ResourceBundle.getBundle("com/kapti/administration/translations");

    JCheckBoxMenuItem saveUsernamesMenuItem = null;
    JCheckBoxMenuItem savePasswordsMenuItem = null;
    JCheckBoxMenuItem loginWithEidMenuItem = null;

    public MenuBar() {

        StockPlayPreferences preferences = new StockPlayPreferences();
        ActionsFactory actFactory = ActionsFactory.getInstance();

        JMenu fileMenu = new JMenu(translations.getString("FILE"));

        JMenuItem exitItem = new JMenuItem(translations.getString("EXIT"));
        exitItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                MainFrame.getInstance().dispose();
            }
        });
        fileMenu.add(exitItem);
        add(fileMenu);

        JMenu statusMenu = new JMenu(translations.getString("STATUS"));

        for (Action act : actFactory.getStatusActions()) {
            statusMenu.add(act);
        }
        add(statusMenu);

        JMenu securitiesMenu = new JMenu(translations.getString("SECURITIESMANAGEMENTMENUITEM"));
        for (Action act : actFactory.getSecuritiesActions()) {
            securitiesMenu.add(act);
        }
        add(securitiesMenu);

        JMenu usersMenu = new JMenu(translations.getString("USERMANAGEMENTMENUITEM"));
        for (Action act : actFactory.getUsersActions()) {
            usersMenu.add(act);
        }
        add(usersMenu);


        JMenu settingsMenu = new JMenu(translations.getString("SETTINGS"));

        JMenuItem languageItem = new JMenuItem(translations.getString("LANGUAGE"));
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

        JMenuItem loginItem = new JMenuItem(translations.getString("LOGIN"));
        loginItem.setEnabled(false);
        loginItem.setFont(languageItem.getFont().deriveFont(Font.BOLD));
        settingsMenu.add(loginItem);

        saveUsernamesMenuItem = new JCheckBoxMenuItem(translations.getString("SAVE_USERNAMES"));
        saveUsernamesMenuItem.setState(preferences.getSaveUsernames());
        saveUsernamesMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                StockPlayPreferences preferences = new StockPlayPreferences();
                preferences.setSaveUsernames(saveUsernamesMenuItem.getState());
            }
        });

        settingsMenu.add(saveUsernamesMenuItem);

        savePasswordsMenuItem = new JCheckBoxMenuItem(translations.getString("SAVE_PASSWORDS"));
        savePasswordsMenuItem.setState(preferences.getSavePasswords());
        savePasswordsMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                StockPlayPreferences preferences = new StockPlayPreferences();
                preferences.setSavePasswords(savePasswordsMenuItem.getState());
            }
        });

        settingsMenu.add(savePasswordsMenuItem);



        loginWithEidMenuItem = new JCheckBoxMenuItem(translations.getString("LOGIN_WITH_EID"));
        loginWithEidMenuItem.setState(preferences.getLoginWithEid());
        loginWithEidMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                StockPlayPreferences prefs = new StockPlayPreferences();


                if (loginWithEidMenuItem.getState()) {

                    //credentials opvragen
                    JXLoginPane loginPane = new JXLoginPane();
                    JXLoginFrame loginFrame;
                    loginPane.setBannerText(translations.getString("LOGIN_EID_BANNER"));
                    JOptionPane.showMessageDialog(null, translations.getString("LOGIN_EID_MESSAGE"), translations.getString("LOGIN_EID_TITLE"), JOptionPane.INFORMATION_MESSAGE);

                    loginPane.setMessage(translations.getString("LOGIN_EID_MESSAGE2"));

                    loginFrame = JXLoginPane.showLoginFrame(loginPane);
                    loginFrame.setTitle(translations.getString("LOGIN_EID_TITLE2"));
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
                                JOptionPane.showMessageDialog(null, translations.getString("LOGIN_EID_SUCCESS"), translations.getString("LOGIN_EID_SUCCESS_TITLE"), JOptionPane.INFORMATION_MESSAGE);


                            } else {

                                JOptionPane.showMessageDialog(null, translations.getString("LOGIN_EID_FAIL"), translations.getString("LOGIN_EID_FAIL_TITLE"), JOptionPane.ERROR_MESSAGE);
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

            JOptionPane.showMessageDialog(null, translations.getString("LANGUAGE_CHANGED_MESSAGE"));
        }
    }
}
