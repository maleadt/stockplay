/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.administration;

import com.kapti.administration.bo.finance.Exchange;
import com.kapti.administration.bo.finance.FinanceFactory;
import com.kapti.exceptions.StockPlayException;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import org.jdesktop.swingx.*;
import org.jdesktop.swingx.JXLoginPane.Status;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.auth.LoginService;
import org.jdesktop.swingx.auth.UserNameStore;

/**
 *
 * @author Thijs
 */
public class MainFrame extends JFrame {


    /**
     * Panel dat gebruikt wordt als ouder voor het main panel
     */
    private JPanel mainParentPanel = new JPanel();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                //eerst een loginscherm
                org.jdesktop.swingx.JXLoginPane login = new JXLoginPane();


                login.setBannerText("Stockplay");
                login.setMessage("Geef uw gegevens in om in te loggen:");
                login.setUserNameStore(new UserNameStore() {

                    String[] usernames = new String[]{"Thijs", "Dieter", "Tim", "Laurens"};

                    @Override
                    public String[] getUserNames() {
                        return usernames;
                    }

                    @Override
                    public void setUserNames(String[] names) {
                        usernames = names;
                    }

                    @Override
                    public void loadUserNames() {
                    }

                    @Override
                    public void saveUserNames() {
                    }

                    @Override
                    public boolean containsUserName(String name) {
                        for (String user : usernames) {
                            if (user.equals(name)) {
                                return true;
                            }
                        }
                        return false;
                    }

                    @Override
                    public void addUserName(String userName) {
                        //throw new UnsupportedOperationException("Not supported yet.");
                    }

                    @Override
                    public void removeUserName(String userName) {
                        // throw new UnsupportedOperationException("Not supported yet.");
                    }
                });
                login.setSaveMode(JXLoginPane.SaveMode.USER_NAME);
                login.setLoginService(new LoginService() {

                    @Override
                    public boolean authenticate(String name, char[] password, String server) throws Exception {
                        Thread.sleep(1000);
                        return new String(password).equals(name);
                    }
                });

                if (JXLoginPane.showLoginDialog(null, login) == Status.SUCCEEDED) {
                    //indien oké, het beheervenster laten zien
                    MainFrame frame = MainFrame.getInstance();
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                    frame.setVisible(true);
                }
            }
        });

    }
    private static MainFrame instance = new MainFrame();

    public static MainFrame getInstance() {
        return instance;
    }

    private MainFrame() {
        try {
            // Set System L&F
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException e) {
            // handle exception
        } catch (ClassNotFoundException e) {
            // handle exception
        } catch (InstantiationException e) {
            // handle exception
        } catch (IllegalAccessException e) {
            // handle exception
        }


        setLayout(new BorderLayout());
        setTitle("Stockplay administratie");



        add(new Menu(this), BorderLayout.WEST);

        mainParentPanel.setLayout(new BorderLayout());
        add(mainParentPanel, BorderLayout.CENTER);

        setMainPanel(StatusOverviewPanel.getInstance());

        pack();
        setLocationRelativeTo(null);

    }

    public void setMainPanel(JPanel mainPanel) {
        mainParentPanel.removeAll();
        mainParentPanel.add(mainPanel);
        mainParentPanel.repaint();
    }

    public JPanel getMainPanel() {
        return mainParentPanel;
    }



 
}
