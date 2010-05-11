/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.administration.helpers;

import com.kapti.client.user.User;
import com.kapti.client.user.UserFactory;
import com.kapti.exceptions.StockPlayException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdesktop.swingx.JXLoginPane;
import org.jdesktop.swingx.JXLoginPane.JXLoginFrame;
import org.jdesktop.swingx.JXLoginPane.Status;

/**
 *
 * @author Thijs
 */
public class StockPlayLoginScreen implements LoginScreen {


    private List<ActionListener> listeners = new ArrayList<ActionListener>();
    public void addActionListener(ActionListener listener){
        listeners.add(listener);
    }

    public void removeActionListener(ActionListener listener){
        listeners.remove(listener);
    }


    private void fireActionEvent(ActionEvent e){

        for(ActionListener listener : listeners){
            listener.actionPerformed(e);
        }
    }

    private boolean success = false;

    public boolean isSuccess() {
        return success;
    }

    private User user = null;

    public User getUser() {
        return user;
    }



    private final JXLoginPane loginPane = new JXLoginPane();
    private JXLoginFrame loginFrame;


    public StockPlayLoginScreen() {
        StockPlayPreferences prefs = new StockPlayPreferences();

        loginPane.setBannerText("Stockplay");
        loginPane.setMessage("Geef uw gegevens in om in te loggen:");

        if (prefs.getSavePasswords() && prefs.getSaveUsernames()) {
            loginPane.setSaveMode(JXLoginPane.SaveMode.BOTH);
        } else if (prefs.getSaveUsernames()) {
            loginPane.setSaveMode(JXLoginPane.SaveMode.USER_NAME);
        } else if (prefs.getSavePasswords()) {
            loginPane.setSaveMode(JXLoginPane.SaveMode.PASSWORD);
        } else {
            loginPane.setSaveMode(JXLoginPane.SaveMode.NONE);
        }
        loginPane.setLoginService(new StockPlayLoginService());
        loginPane.setUserNameStore(new StockPlayUsernameStore());
        loginPane.setPasswordStore(new StockPlayPasswordStore());

        loginFrame = JXLoginPane.showLoginFrame(loginPane);
        loginFrame.setTitle("StockPlay login");
        loginFrame.setVisible(true);

        loginFrame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosed(WindowEvent e) {
                if (loginPane.getStatus() == Status.SUCCEEDED) {
                    success= true;
                    fireActionEvent(new ActionEvent(this, 1,""));
                    try {
                        Collection<User> users = UserFactory.getInstance().getUsersByFilter("nickname == '" + loginPane.getUserName() + "'");
                        Iterator<User> it = users.iterator();
                        if(it.hasNext())
                            user = it.next();
                    } catch (StockPlayException ex) {
                        Logger.getLogger(StockPlayLoginScreen.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else
                    fireActionEvent(new ActionEvent(this, 0,""));
            }
        });
    }
}
