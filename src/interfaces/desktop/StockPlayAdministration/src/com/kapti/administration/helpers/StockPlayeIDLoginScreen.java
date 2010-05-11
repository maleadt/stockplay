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
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
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
public class StockPlayeIDLoginScreen extends JFrame implements PropertyChangeListener, LoginScreen {

    JPanel panel = null;
    JLabel step1Label = new JLabel("1. Detecteren van kaartlezers");
    JLabel step2Label = new JLabel("2. Gelieve uw eID in de lezer te steken");
    JLabel step3Label = new JLabel("3. Controle van eID op StockPlay server");
    JLabel step4Label = new JLabel("4. Verificatie pincode");
    JLabel step5Label = new JLabel("5. Inloggen op StockPlay server");
    JLabel titleLabel = new JLabel("Login met eID");
    private final static Color activeColor = Color.BLACK;
    private final static Color inactiveColor = Color.LIGHT_GRAY;
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
                JOptionPane.showMessageDialog(rootPane, "Er is een probleem opgetreden tijdens het inloggen op de StockPlay server met de opgegeven gegevens! Controleer uw instellingen!", "Fout tijdens inloggen!", JOptionPane.ERROR_MESSAGE);
                return null;
            }

            Collection<User> users = uf.getUsersByFilter("rrn == '" + eid.getRijksRegisterNummer() + "'");
            Iterator<User> it = users.iterator();

            if (!it.hasNext()) {
                JOptionPane.showMessageDialog(rootPane, "Er is geen account gekoppeld met de de ingelezen eID! Gelieve te controleren of u deze juist heeft geregistreerd!", "Fout tijdens inloggen!", JOptionPane.ERROR_MESSAGE);
                return null;

            }

            User user = it.next();

            this.setProgress(4);

            while (!eid.isAuthenticated()) {
                eid.verifyPIN();

                if (!eid.isAuthenticated()) {

                    if (eid.isUserCancelled()) {
                        JOptionPane.showMessageDialog(rootPane, "De verificatie van uw pin is mislukt omdat u het verificatieproces hebt geannuleerd", "Fout bij verifiëren pin", JOptionPane.ERROR_MESSAGE);
                        return null;
                    } else {
                        if (JOptionPane.showConfirmDialog(rootPane, String.format("De ingegeven pin was ongeldig! U heeft nog %d poging(en)! Wilt u opnieuw proberen aanmelden?", eid.getTriesLeft()), "Ongeldige PIN", JOptionPane.YES_NO_CANCEL_OPTION)
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

                JXErrorPane.showDialog(new Exception("Er is een fout opgetreden tijdens het verwerken van de eID-login", ex));
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
}
