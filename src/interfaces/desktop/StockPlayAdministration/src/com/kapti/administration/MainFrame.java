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
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



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
