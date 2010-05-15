/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.administration;

import com.kapti.administration.helpers.StockPlayLoginScreen;
import com.kapti.administration.helpers.StockPlayPreferences;
import com.kapti.administration.helpers.StockPlayeIDLoginScreen;
import com.kapti.administration.helpers.URLUtils;
import com.kapti.client.SPClientFactory;
import com.kapti.client.user.User;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author Thijs
 */
public class Main {
    private static final ResourceBundle translations = ResourceBundle.getBundle("com/kapti/administration/translations");

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {

        StockPlayPreferences spp = new StockPlayPreferences();

        //locale instellen
        if (spp.getLocale() != null) {
            Locale.setDefault(spp.getLocale());
        }

        //serverurl instellen

        SPClientFactory.setServerURL(spp.getServerURL());

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                Main m = new Main();

            }
        });

    }
    private StockPlayeIDLoginScreen eIDScreen = null;
    private StockPlayLoginScreen screen = null;

    public Main() {
        StockPlayPreferences prefs = new StockPlayPreferences();
        while (!SPClientFactory.checkConnectivity()) {
            if (JOptionPane.showConfirmDialog(null, translations.getString("CONNECTION_ERROR_MESSAGE"),
                    translations.getString("CONNECTION_ERROR_TITLE"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE) == JOptionPane.YES_OPTION) {
                String output = JOptionPane.showInputDialog(null, translations.getString("STOCKPLAY_SERVER"), prefs.getServerURL());
                if (output != null) {
                    SPClientFactory.setServerURL(output);
                    if (SPClientFactory.checkConnectivity()) {
                        prefs.setServerURL(output);
                        JOptionPane.showMessageDialog(null, translations.getString("STOCKPLAY_SERVER_SETTINGS_CHANGED"), translations.getString("STOCKPLAY_SERVER_SETTINGS_CHANGED_TITLE"), JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, translations.getString("STOCKPLAY_SERVER_SETTINGS_ERROR"), translations.getString("STOCKPLAY_SERVER_SETTINGS_ERROR_TITLE"), JOptionPane.INFORMATION_MESSAGE);
                        System.exit(1);
                    }
                }
            } else {
                System.exit(1);
            }
        }

        if (prefs.getLoginWithEid()) {
            eIDScreen = new StockPlayeIDLoginScreen();
            eIDScreen.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (eIDScreen.isSuccess()) {

                        if (eIDScreen.getUser().getRole() != User.Role.ADMIN) {
                            JOptionPane.showMessageDialog(null, translations.getString("INSUFFICIENT_RIGHTS_ERROR"), translations.getString("ERROR"), JOptionPane.ERROR_MESSAGE);
                            System.exit(1);
                        } else {
                            showMainScreen();
                        }
                    } else //fallback
                    {
                        showLoginScreen();
                    }

                }
            });
            eIDScreen.setVisible(true);
        } else {
            showLoginScreen();

        }
    }

    private void showLoginScreen() {
        screen = new StockPlayLoginScreen();
        screen.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (screen.isSuccess()) {
                    if (screen.getUser().getRole() != User.Role.ADMIN) {
                        JOptionPane.showMessageDialog(null, translations.getString("INSUFFICIENT_RIGHTS_ERROR"), translations.getString("ERROR"), JOptionPane.ERROR_MESSAGE);
                        System.exit(1);
                    } else {
                        showMainScreen();
                    }
                }
            }
        });
    }

    public void showMainScreen() {
        MainFrame mf = MainFrame.getInstance();
        mf.setVisible(true);
    }
}
