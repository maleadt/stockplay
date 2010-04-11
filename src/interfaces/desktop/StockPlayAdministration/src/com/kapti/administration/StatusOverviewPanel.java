/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.administration;

import com.kapti.administration.bo.system.Backend;
import com.kapti.administration.bo.system.StatusObject;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import org.jdesktop.swingx.JXTitledSeparator;

/**
 *
 * @author Thijs
 */
public class StatusOverviewPanel extends JPanel implements ActionListener {

    private static StatusOverviewPanel instance = new StatusOverviewPanel();

    public static StatusOverviewPanel getInstance() {
        return instance;
    }
    JLabel uptimeLabel;
    JLabel loggedInUsersLabel;
    JLabel processedRequestsLabel;

    private StatusOverviewPanel() {

        this.setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel titel = new JLabel("Overzicht");
        titel.setFont(titel.getFont().deriveFont(Font.BOLD, 30));

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = 10;
        c.ipady = 25;
        c.gridx = 0;
        c.gridy = 0;
        add(titel, c);

        JXTitledSeparator componentsLabel = new JXTitledSeparator("Componenten");
        componentsLabel.setFont(componentsLabel.getFont().deriveFont(Font.BOLD, 20));
        c.ipadx = 0;

        c.gridy = 2;
        add(componentsLabel, c);

        //We maken hier de componenten-start-stop-dingen aan
//        createStatusItem(new StatusObject("Scraper"), 3);
//        createStatusItem(new StatusObject("Backend"), 4);
//        createStatusItem(new StatusObject("Database"), 5);
//        createStatusItem(new StatusObject("Webserver"), 6);
//        createStatusItem(new StatusObject("AI"), 7);

        StatusView scraperView = new StatusView(this, 3, new StatusObject("Scraper", "System.Scraper.Status"));
        StatusView backendView = new StatusView(this,4, new StatusObject("Backend", "System.Backend.Status"));
        StatusView Database = new StatusView(this, 5, new StatusObject("Database", "System.Database.Status"));
        //StatusView Webserver = new StatusView(this, 6, new StatusObject("Webserver", "System.Scraper.Status"));
        

        JXTitledSeparator statsLabel = new JXTitledSeparator("Statistieken");
        statsLabel.setFont(statsLabel.getFont().deriveFont(Font.BOLD, 20));
        c.gridy = 10;
        add(statsLabel, c);

        GridBagConstraints cStats = new GridBagConstraints();
        cStats.ipadx = 10;
        cStats.fill = GridBagConstraints.HORIZONTAL;
        cStats.gridy = 11;
        cStats.gridx = 2;


        JLabel statLoggedInUsersLabel = new JLabel("Aantal ingelogd gebruikers: ", JLabel.RIGHT);
        add(statLoggedInUsersLabel, cStats);

        cStats.gridx = 3;
        cStats.gridwidth = GridBagConstraints.REMAINDER;

        loggedInUsersLabel = new JLabel("-");
        add(loggedInUsersLabel, cStats);

        cStats.gridy++;
        cStats.gridx = 2;
        cStats.gridwidth = 1;
        JLabel statUptimeLabel = new JLabel("Uptime backend: ", JLabel.RIGHT);
        add(statUptimeLabel, cStats);

        cStats.gridx = 3;
        cStats.gridwidth = GridBagConstraints.REMAINDER;
        uptimeLabel = new JLabel("-");
        add(uptimeLabel, cStats);

        cStats.gridy++;
        cStats.gridx = 2;
        cStats.gridwidth = 1;
        JLabel statProcessedRequestsLabel = new JLabel("Aantal verwerkte requests: ", JLabel.RIGHT);
        add(statProcessedRequestsLabel, cStats);

        cStats.gridx = 3;
        cStats.gridwidth = GridBagConstraints.REMAINDER;
        processedRequestsLabel = new JLabel("-");
        add(processedRequestsLabel, cStats);

        cStats.gridy++;
        add(new Box.Filler(new Dimension(0, 0), new Dimension(25, 0), new Dimension(100, 0)), cStats);


        //we starten nu een timer die elke seconde statistieken ophaalt
        Timer timer = new Timer(1000, this);
        timer.start();

    }

//    private void createStatusItem(StatusObject statObj, int row) {
//        GridBagConstraints cStatus = new GridBagConstraints();
//        cStatus.ipadx = 10;
//        cStatus.gridy = row;
//
//        cStatus.fill = GridBagConstraints.HORIZONTAL;
//        JLabel nameLabel = new JLabel(statObj.getName());
//        cStatus.gridx = 1;
//
//        add(nameLabel, cStatus);
//
//        JLabel statusLabel = new JLabel("", JLabel.LEFT);
//        statusLabel.setPreferredSize(new Dimension(150, 10));
//        if (statObj.getStatus() == StatusObject.Status.STARTED) {
//            statusLabel.setText("Gestart");
//            statusLabel.setForeground(Color.GREEN);
//        } else if (statObj.getStatus() == StatusObject.Status.STOPPED) {
//            statusLabel.setText("Gestopt");
//            statusLabel.setForeground(Color.RED);
//        } else if (statObj.getStatus() == StatusObject.Status.STARTING) {
//            statusLabel.setText("Bezig met starten...");
//            statusLabel.setForeground(Color.ORANGE);
//        } else if (statObj.getStatus() == StatusObject.Status.STOPPING) {
//            statusLabel.setText("Bezig met stoppen...");
//            statusLabel.setForeground(Color.ORANGE);
//        } else if (statObj.getStatus() == StatusObject.Status.ERROR) {
//            statusLabel.setText("FOUT");
//            statusLabel.setForeground(Color.RED);
//        } else { //(statObj.getStatus() == StatusObject.Status.UNKNOWN) {
//            statusLabel.setText("Status onbekend");
//            statusLabel.setForeground(Color.ORANGE);
//        }
//        cStatus.gridx = 2;
//        add(statusLabel, cStatus);
//
//        cStatus.insets = new Insets(5, 10, 5, 10);
//
//        JButton startButton = new JButton("Start");
//        startButton.setEnabled(false);
//        cStatus.gridx = 3;
//        add(startButton, cStatus);
//
//        JButton stopButton = new JButton("Stop");
//        stopButton.setEnabled(false);
//        cStatus.gridx = 4;
//        add(stopButton, cStatus);
//
//        JButton restartButton = new JButton("Herstarten");
//        restartButton.setEnabled(false);
//        cStatus.gridx = 5;
//        add(restartButton, cStatus);
//
//    }

    /**
     * Wordt opgeroepen bij het tikken van de timer voor het updaten van de statistieken
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        Backend backend = new Backend();
        loggedInUsersLabel.setText(Integer.toString(backend.getOnlineUsers()));
        processedRequestsLabel.setText(Integer.toString(backend.getProcessedRequests()));

        long uptime = backend.getUptime();

        int seconds = (int) uptime % 60;
        uptime /= 60;
        int minutes = (int) uptime % 60;
        uptime /= 60;
        int hours = (int) uptime % 24;
        uptime /= 24;
        int days = (int) uptime;


        uptimeLabel.setText(days + " dagen, " + hours + " uren, " + minutes + " minuten en " + seconds + " seconden");
    }

    private class StatusView implements ActionListener {

        private JPanel panel;
        private int row;
        private StatusObject statObj;
        JLabel nameLabel;
        JLabel statusLabel;
        JButton startButton, stopButton, restartButton;

        public StatusView(JPanel panel, int row, StatusObject statObj) {
            this.panel = panel;
            this.row = row;
            this.statObj = statObj;

            //algemene gridbagconstraints-instellingen
            GridBagConstraints cStatus = new GridBagConstraints();
            cStatus.ipadx = 10;
            cStatus.gridy = row;
            cStatus.fill = GridBagConstraints.HORIZONTAL;


            //namelabel
            nameLabel = new JLabel(statObj.getName());
            cStatus.gridx = 1;
            add(nameLabel, cStatus);


            //statuslabel
            statusLabel = new JLabel("", JLabel.LEFT);
            statusLabel.setPreferredSize(new Dimension(150, 10));

            cStatus.gridx = 2;
            add(statusLabel, cStatus);

            //buttons
            cStatus.insets = new Insets(5, 10, 5, 10);

            startButton = new JButton("Start");

            cStatus.gridx = 3;
            add(startButton, cStatus);

            stopButton = new JButton("Stop");

            cStatus.gridx = 4;
            add(stopButton, cStatus);

            restartButton = new JButton("Herstarten");
            cStatus.gridx = 5;
            add(restartButton, cStatus);



            //we zorgen ervoor dat er periodiek geupdated wordt
            statObj.addActionListener(this);
            statObj.fetchUpdates(true);
            updateView();
        }

        public void actionPerformed(ActionEvent e) {
            updateView();
        }

        private void updateView() {

            if (statObj.getStatus() == StatusObject.Status.STARTED) {
                statusLabel.setText("Gestart");
                statusLabel.setForeground(Color.GREEN);
            } else if (statObj.getStatus() == StatusObject.Status.STOPPED) {
                statusLabel.setText("Gestopt");
                statusLabel.setForeground(Color.RED);
            } else if (statObj.getStatus() == StatusObject.Status.STARTING) {
                statusLabel.setText("Bezig met starten...");
                statusLabel.setForeground(Color.ORANGE);
            } else if (statObj.getStatus() == StatusObject.Status.STOPPING) {
                statusLabel.setText("Bezig met stoppen...");
                statusLabel.setForeground(Color.ORANGE);
            } else if (statObj.getStatus() == StatusObject.Status.ERROR) {
                statusLabel.setText("FOUT");
                statusLabel.setForeground(Color.RED);
            } else { //(statObj.getStatus() == StatusObject.Status.UNKNOWN) {
                statusLabel.setText("Status onbekend");
                statusLabel.setForeground(Color.ORANGE);
            }

            startButton.setEnabled(statObj.getStatus() != StatusObject.Status.STARTED
                    && statObj.getStatus() != StatusObject.Status.STARTING);
            stopButton.setEnabled(statObj.getStatus() != StatusObject.Status.STOPPED
                    && statObj.getStatus() != StatusObject.Status.STOPPING);
            restartButton.setEnabled(statObj.getStatus() != StatusObject.Status.STOPPED
                    && statObj.getStatus() != StatusObject.Status.STOPPING);
        }
    }
}
