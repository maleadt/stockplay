/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.administration;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import org.jdesktop.swingx.*;
import org.jdesktop.swingx.JXLoginPane.Status;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.auth.LoginService;
import org.jdesktop.swingx.auth.UserNameStore;

/**
 *
 * @author Thijs
 */
public class MainFrame extends JFrame implements ActionListener, PropertyChangeListener {

    private JXTaskPane statusMenuPane, securitiesMenuPane, usersMenuPane;
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



        add(getMenu(), BorderLayout.WEST);

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

    private JXTaskPaneContainer getMenu() {
        JXTaskPaneContainer menuContainer = new JXTaskPaneContainer();

        /////////////////
        // Status-menu //
        /////////////////
        statusMenuPane = new JXTaskPane();
        statusMenuPane.setTitle("Status");
        statusMenuPane.setIcon(createImageIcon("images/cog.png"));
        statusMenuPane.addPropertyChangeListener("collapsed", this);

        statusMenuPane.add(createMenuitem("Componenten", MenuitemAction.StatusOverview, "server", Font.BOLD));

        statusMenuPane.add(createMenuitem("Scraper", MenuitemAction.StatusOverview, "world"));
        statusMenuPane.add(createMenuitem("Database", MenuitemAction.StatusOverview, "database"));
        statusMenuPane.add(createMenuitem("Webserver", MenuitemAction.StatusOverview, "server"));
        statusMenuPane.add(createMenuitem("AI", MenuitemAction.StatusOverview, "ai"));

        statusMenuPane.add(new Box.Filler(new Dimension(0, 20), new Dimension(0, 20), new Dimension(0, 30)));

        statusMenuPane.add(createMenuitem("Status", MenuitemAction.StatusOverview, "chart_bar", Font.BOLD));
        menuContainer.add(statusMenuPane);

        /////////////////////////
        // Effectenbeheer-menu //
        /////////////////////////

        securitiesMenuPane = new JXTaskPane();
        securitiesMenuPane.setTitle("Effectenbeheer");
        securitiesMenuPane.setIcon(createImageIcon("images/money.png"));
        securitiesMenuPane.setCollapsed(true);
        securitiesMenuPane.addPropertyChangeListener("collapsed", this);

        securitiesMenuPane.add(createMenuitem("Beurzen", MenuitemAction.SecuritiesList, "money", Font.BOLD));

        String[] exchanges = new String[]{"Euronext Brussel", "Euronext Amsterdam", "Euronext Paris", "NYSE", "Nasdaq"};
        for (String exch : exchanges) {
            securitiesMenuPane.add(createMenuitem(exch, MenuitemAction.SecuritiesList, "money_euro"));
        }

        securitiesMenuPane.add(new Box.Filler(new Dimension(0, 20), new Dimension(0, 20), new Dimension(0, 30)));
        securitiesMenuPane.add(createMenuitem("Indexen", MenuitemAction.SecuritiesList, "money", Font.BOLD));

        String[] indexes = new String[]{"Bel20", "AMS30", "CAC40", "Dow Jones"};
        for (String index : indexes) {
            securitiesMenuPane.add(createMenuitem(index, MenuitemAction.SecuritiesList, "money_dollar"));
        }

        menuContainer.add(securitiesMenuPane);

        ///////////////////////////
        // Gebruikersbeheer-menu //
        ///////////////////////////

        usersMenuPane = new JXTaskPane();
        usersMenuPane.setTitle("Gebruikersbeheer");
        usersMenuPane.setIcon(createImageIcon("images/user.png"));
        usersMenuPane.setCollapsed(true);
        usersMenuPane.addPropertyChangeListener("collapsed", this);

        usersMenuPane.add(createMenuitem("Overzicht", MenuitemAction.UsersList, "folder_user", Font.BOLD));

        usersMenuPane.add(new Box.Filler(new Dimension(0, 20), new Dimension(0, 20), new Dimension(0, 30)));
        usersMenuPane.add(createMenuitem("Alfabetisch zoeken", MenuitemAction.UsersList, "folder_user", Font.BOLD));
        usersMenuPane.add(createMenuitem("A-E", MenuitemAction.UsersList, "group"));
        usersMenuPane.add(createMenuitem("F-J", MenuitemAction.UsersList, "group"));
        usersMenuPane.add(createMenuitem("K-O", MenuitemAction.UsersList, "group"));
        usersMenuPane.add(createMenuitem("P-S", MenuitemAction.UsersList, "group"));
        usersMenuPane.add(createMenuitem("T-Z", MenuitemAction.UsersList, "group"));

        usersMenuPane.add(new Box.Filler(new Dimension(0, 20), new Dimension(0, 20), new Dimension(0, 30)));
        usersMenuPane.add(createMenuitem("Zoeken op registratiedatum", MenuitemAction.UsersList, "folder_user", Font.BOLD));
        usersMenuPane.add(createMenuitem("Vandaag", MenuitemAction.UsersList, "group"));
        usersMenuPane.add(createMenuitem("Afgelopen week", MenuitemAction.UsersList, "group"));
        usersMenuPane.add(createMenuitem("Afgelopen maand", MenuitemAction.UsersList, "group"));
        usersMenuPane.add(createMenuitem("Afgelopen jaar", MenuitemAction.UsersList, "group"));
        menuContainer.add(usersMenuPane);




        return menuContainer;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getNewValue().equals(false)) {
            if (evt.getSource().equals(statusMenuPane)) {
                setMainPanel(StatusOverviewPanel.getInstance());
            } else {

                statusMenuPane.setCollapsed(true);
            }
            if (evt.getSource().equals(securitiesMenuPane)) {
                setMainPanel(SecuritiesListPanel.getInstance());
            } else {
                securitiesMenuPane.setCollapsed(true);
            }
            if (!evt.getSource().equals(usersMenuPane)) {
                usersMenuPane.setCollapsed(true);
            }
        }
    }

    private enum MenuitemAction {

        StatusOverview,
        SecuritiesList,
        UsersList
    };

    private JComponent createMenuitem(String name, MenuitemAction action, String iconname) {
        return createMenuitem(name, action, iconname, Font.PLAIN);
    }

    private JComponent createMenuitem(String name, MenuitemAction action, String iconname, int fontstyle) {

        JXHyperlink link = new JXHyperlink();
        link.setText(name);
        link.setClickedColor(link.getUnclickedColor()); //voorkomen dat link van kleur verandert
        if (fontstyle != Font.PLAIN) {
            link.setFont(link.getFont().deriveFont(fontstyle));
        }
        link.setActionCommand(action.toString());
        if (iconname != null) {
            link.setIcon(createImageIcon("images/" + iconname + ".png"));
        }
        link.addActionListener(this);

        return link;
    }

    public void actionPerformed(ActionEvent e) {

        MenuitemAction action = MenuitemAction.valueOf(e.getActionCommand());
        if (action.equals(MenuitemAction.StatusOverview)) {
            setMainPanel(StatusOverviewPanel.getInstance());
        } else if (action.equals(MenuitemAction.SecuritiesList)) {
            setMainPanel(SecuritiesListPanel.getInstance());
        } else if (action.equals(MenuitemAction.UsersList)) {
            setMainPanel(null);
        }
    }

    /**
     * Geeft een ImageIcon terug als de link valide was, anders null
     * @param path
     * @return
     */
    protected ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}
