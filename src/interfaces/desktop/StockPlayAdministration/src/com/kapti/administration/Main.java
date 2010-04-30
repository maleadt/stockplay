/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.administration;

import com.kapti.administration.helpers.StockPlayLoginService;
import com.kapti.administration.helpers.StockPlayPreferences;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;
import javax.swing.SwingUtilities;
import org.jdesktop.swingx.JXLoginPane;
import org.jdesktop.swingx.JXLoginPane.Status;

/**
 *
 * @author Thijs
 */
public class Main {




    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {


        StockPlayPreferences spp  = new StockPlayPreferences();

        if(spp.getLocale() != null){
            System.out.println(spp.getLocale().getLanguage() + " is de nieuwe taal");
            Locale.setDefault(spp.getLocale());
        }

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {

                //eerst een loginscherm
                JXLoginPane loginPane = new JXLoginPane();
                loginPane.setBannerText("Stockplay");
                loginPane.setMessage("Geef uw gegevens in om in te loggen:");

                loginPane.setSaveMode(JXLoginPane.SaveMode.USER_NAME);
                loginPane.setLoginService(new StockPlayLoginService());
                //loginPane.setLoginService(new StockPlayeIDLoginService());
                //loginPane.setUserNameEnabled(false);
            

                JXLoginPane.JXLoginFrame loginFrame = JXLoginPane.showLoginFrame(loginPane);
                loginFrame.setTitle("StockPlay login");
                loginFrame.setVisible(true);

                loginFrame.addWindowListener(new WindowAdapter() {

                    @Override
                    public void windowClosed(WindowEvent e) {
                        if (((JXLoginPane.JXLoginFrame)e.getSource()).getStatus() == Status.SUCCEEDED) {
                            MainFrame frame = MainFrame.getInstance();
                            frame.setVisible(true);
                        }
                    }
                });


            }
        });

    }
}
