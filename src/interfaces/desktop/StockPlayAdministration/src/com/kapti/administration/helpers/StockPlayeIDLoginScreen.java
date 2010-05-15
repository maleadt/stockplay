/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.administration.helpers;

import com.kapti.client.user.User;
import com.kapti.client.user.UserFactory;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import org.jdesktop.swingx.JXErrorPane;

/**
 *
 * @author Thijs
 */
public class StockPlayeIDLoginScreen extends JFrame implements PropertyChangeListener, LoginScreen, ActionListener {

    JPanel panel = null;
    JLabel step1Label = new JLabel(translations.getString("EID_1"));
    JLabel step2Label = new JLabel(translations.getString("EID_2"));
    JLabel step3Label = new JLabel(translations.getString("EID_3"));
    JLabel step4Label = new JLabel(translations.getString("EID_4"));
    JLabel step5Label = new JLabel(translations.getString("EID_5"));
    JLabel titleLabel = new JLabel(translations.getString("EID_LOGIN_TITLE"));
    JButton cancelButton = new JButton(translations.getString("EID_LOGIN_CANCEL"));
    private final static Color activeColor = Color.BLACK;
    private final static Color inactiveColor = Color.LIGHT_GRAY;
    private static final ResourceBundle translations = ResourceBundle.getBundle("com/kapti/administration/translations");
    private long RRN = -1;
    private List<ActionListener> listeners = new ArrayList<ActionListener>();

    public void addActionListener(ActionListener listener) {
        listeners.add(listener);
    }

    public void removeActionListener(ActionListener listener) {
        listeners.remove(listener);
    }

    private void fireActionEvent(ActionEvent e) {
        for (ActionListener listener : listeners) {
            listener.actionPerformed(e);
        }
    }

    public StockPlayeIDLoginScreen() throws HeadlessException {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        //instellen van titel

        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(titleLabel, BorderLayout.NORTH);


        panel = new JPanel();

        panel.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(step1Label);
        panel.add(step2Label);
        panel.add(step3Label);
        panel.add(step4Label);
        panel.add(step5Label);

        add(panel, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosed(WindowEvent e) {
                if (eIDWorker.isDone() && RRN > 0) {
                    fireActionEvent(new ActionEvent(this, 1, ""));

                } else {
                    fireActionEvent(new ActionEvent(this, 0, ""));
                }
            }
        });


        add(cancelButton, BorderLayout.SOUTH);

        cancelButton.addActionListener(this);


        pack();
        setLocationRelativeTo(null);


        eIDWorker.addPropertyChangeListener(this);
        eIDWorker.execute();



    }

    private void showStep(int i) {
        step1Label.setForeground(i == 1 ? activeColor : inactiveColor);
        step2Label.setForeground(i == 2 ? activeColor : inactiveColor);
        step3Label.setForeground(i == 3 ? activeColor : inactiveColor);
        step4Label.setForeground(i == 4 ? activeColor : inactiveColor);
        step5Label.setForeground(i == 5 ? activeColor : inactiveColor);
    }
    SwingWorker<User, Integer> eIDWorker = new SwingWorker<User, Integer>() {

        @Override
        protected User doInBackground() throws Exception {

            this.setProgress(1);
            eIDService eid = new eIDService();

            this.setProgress(2);
            eid.findCard();
            this.setProgress(3);
            eid.readCard();
            //we zoeken nu deze gebruiker op op de server
            StockPlayPreferences prefs = new StockPlayPreferences();

            UserFactory uf = UserFactory.getInstance();

            if (!uf.verifyLogin(prefs.getEidAdminUsername(), prefs.getEidAdminPassword())) {
                JOptionPane.showMessageDialog(rootPane, translations.getString("EID_SERVER_ERROR_MESSAGE"), translations.getString("EID_SERVER_ERROR_TITLE"), JOptionPane.ERROR_MESSAGE);
                return null;
            }

            Collection<User> users = uf.getUsersByFilter("rrn == '" + eid.getRijksRegisterNummer() + "'");
            Iterator<User> it = users.iterator();

            if (!it.hasNext()) {
                JOptionPane.showMessageDialog(rootPane, translations.getString("EID_NOACCOUNT_ERROR_MESSAGE"), translations.getString("EID_NOACCOUNT_ERROR_TITLE"), JOptionPane.ERROR_MESSAGE);
                return null;

            }

            User user = it.next();

            this.setProgress(4);

            while (!eid.isAuthenticated()) {
                eid.verifyPIN();

                if (!eid.isAuthenticated()) {

                    if (eid.isUserCancelled()) {
                        JOptionPane.showMessageDialog(rootPane, translations.getString("EID_PINCANCEL_ERROR_MESSAGE"), translations.getString("EID_PINCANCEL_ERROR_TITLE"), JOptionPane.ERROR_MESSAGE);
                        return null;
                    } else {
                        if (JOptionPane.showConfirmDialog(rootPane, String.format(translations.getString("EID_PIN_ERROR_MESSAGE"), eid.getTriesLeft()), translations.getString("EID_PIN_ERROR_TITLE"), JOptionPane.YES_NO_CANCEL_OPTION)
                                != JOptionPane.YES_OPTION) {
                            return null;
                        }
                    }
                }

            }

            this.setProgress(5);

            uf.setLoggedInUser(user);

            return user;
        }

        @Override
        protected void done() {
            try {
                //TODO sessieid ophalen bij de backend
                user = get();
                success = user != null;
                fireActionEvent(new ActionEvent(this, success ? 1 : 0, ""));
            } catch (InterruptedException ex) {
                fireActionEvent(new ActionEvent(this, 0, ""));
                Logger.getLogger(StockPlayeIDLoginScreen.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {

                JXErrorPane.showDialog(new Exception(translations.getString("EID_PROCESS_ERROR"), ex));
                fireActionEvent(new ActionEvent(this, 0, ""));
                Logger.getLogger(StockPlayeIDLoginScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
            setVisible(false);
            super.done();
        }
    };

    public void propertyChange(PropertyChangeEvent evt) {
        showStep(eIDWorker.getProgress());
    }
    protected boolean loggedIn;

    /**
     * Get the value of loggedIn
     *
     * @return the value of loggedIn
     */
    public boolean isLoggedIn() {
        return loggedIn;
    }
    private User user = null;
    private boolean success = false;

    public User getUser() {
        return user;
    }

    public boolean isSuccess() {
        return success;
    }

    public void actionPerformed(ActionEvent e) {
        fireActionEvent(new ActionEvent(this, 0, ""));
        setVisible(false);
    }
}
