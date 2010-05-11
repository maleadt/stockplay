/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.administration;

import com.kapti.administration.helpers.StockPlayLoginScreen;
import com.kapti.administration.helpers.StockPlayPreferences;
import com.kapti.administration.helpers.StockPlayeIDLoginScreen;
import com.kapti.client.SPClientFactory;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import javax.swing.SwingUtilities;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

/**
 *
 * @author Thijs
 */
public class Main  {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {

        StockPlayPreferences spp = new StockPlayPreferences();

        //locale instellen
        if (spp.getLocale() != null) {
            Locale.setDefault(spp.getLocale());
        }

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                Main m = new Main();

            }
        });

    }

    private StockPlayeIDLoginScreen eIDScreen = null;
    private StockPlayLoginScreen screen = null;

    public Main() {

        if(!SPClientFactory.checkConnectivity()){

            JXErrorPane.showDialog(null, new ErrorInfo("Verbindingsfout", "Er kon geen verbinding worden gemaakt met de StockPlay-servers. Controleer uw internetconnectiviteit en probeer opnieuw!", null, "Connectivity", null, null, null));

            System.exit(1);
        }

        StockPlayPreferences prefs = new StockPlayPreferences();




        if (prefs.getLoginWithEid()) {
            eIDScreen = new  StockPlayeIDLoginScreen();
            eIDScreen.addActionListener(new ActionListener(){

                public void actionPerformed(ActionEvent e) {
                    if(eIDScreen.isSuccess())
                        showMainScreen();
                    else
                        //fallback
                        showLoginScreen();

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
                    showMainScreen();
                }
            }
        });
    }

    public void showMainScreen() {
        MainFrame mf = MainFrame.getInstance();
        mf.setVisible(true);
    }

}
