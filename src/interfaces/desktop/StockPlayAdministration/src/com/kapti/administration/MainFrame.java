/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.administration;

import java.util.ResourceBundle;
import javax.swing.*;
import java.awt.*;
import java.util.Locale;

/**
 *
 * @author Thijs
 */
public class MainFrame extends JFrame {


    /**
     * Panel dat gebruikt wordt als ouder voor het main panel
     */
    private JPanel mainParentPanel = new JPanel();

    private static MainFrame instance = new MainFrame();
    private final ResourceBundle translations = ResourceBundle.getBundle("com/kapti/administration/translations");


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

        System.out.println("Gevonden taal: " + Locale.getDefault());

        setLayout(new BorderLayout());
        setTitle(translations.getString("MAINTITLE"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setJMenuBar(new MenuBar());


        add(new Menu(this), BorderLayout.WEST);

        mainParentPanel.setLayout(new BorderLayout());
        add(mainParentPanel, BorderLayout.CENTER);

        setMainPanel(StatusOverviewPanel.getInstance());

        setSize(new Dimension(1024,768));
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
