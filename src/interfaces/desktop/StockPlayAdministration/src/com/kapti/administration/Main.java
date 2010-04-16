/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.administration;

import com.kapti.administration.helpers.StockPlayLoginService;
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
    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {

                MainFrame.getInstance().setVisible(true);

                //eerst een loginscherm
                JXLoginPane loginPane = new JXLoginPane();
                loginPane.setBannerText("Stockplay");
                loginPane.setMessage("Geef uw gegevens in om in te loggen:");

                //loginPane.setSaveMode(JXLoginPane.SaveMode.USER_NAME);
                loginPane.setLoginService(new StockPlayLoginService());

                JXLoginPane.JXLoginFrame loginFrame = JXLoginPane.showLoginFrame(loginPane);
                loginFrame.setTitle("StockPlay login");
                loginFrame.setVisible(true);

                if (loginFrame.getStatus() == Status.SUCCEEDED) {
                    MainFrame frame = MainFrame.getInstance();
                    frame.setVisible(true);
                }
            }
        });

    }
}
