/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.administration;

import com.kapti.administration.bo.user.User;
import com.kapti.administration.bo.user.UserFactory;
import com.kapti.administration.tablemodels.UsersTableModel;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.TableColumn;
import org.apache.log4j.Logger;
import org.apache.xmlrpc.XmlRpcException;
import org.jdesktop.swingx.JXErrorPane;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.error.ErrorInfo;

/**
 *
 * @author Thijs
 */
public class UsersListPanel extends JPanel implements TableModelListener, ListSelectionListener, ActionListener {

    private static final String EDIT_USER_ACTION = "EDIT_USER";
    private static final String CREATE_USER_ACTION = "CREATE_USER";
    private static final String DELETE_USER_ACTION = "DELETE_USER";
    private static Logger logger = Logger.getLogger(UsersListPanel.class);
    private UserFactory usersFactory = new UserFactory();
    private static UsersListPanel instance = new UsersListPanel();
    private JLabel selectedLabel = new JLabel();
    private final ResourceBundle translations = ResourceBundle.getBundle("com/kapti/administration/translations");
    private JXTable usersTable = null;
    private UsersTableModel usersTableModel = null;
    private JButton createUser = new JButton(translations.getString("CREATE_USER"));
    private JButton editUser = new JButton(translations.getString("EDIT_USER"));
    private JButton deleteUser = new JButton(translations.getString("DELETE_USER"));

    public static UsersListPanel getInstance() {
        return instance;
    }

    private UsersListPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titel = new JLabel(translations.getString("OVERVIEW"));
        titel.setFont(titel.getFont().deriveFont(Font.BOLD, 30));
        add(titel, BorderLayout.NORTH);


        //We stellen hier onze securities-JXTable in
        usersTableModel = new UsersTableModel();

        usersTable = new JXTable(usersTableModel);
        usersTable.setColumnControlVisible(true);
        usersTable.setShowGrid(false, false);
        usersTable.setHighlighters(HighlighterFactory.createAlternateStriping());
        usersTable.setAutoCreateRowSorter(true);
        usersTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        usersTable.getSelectionModel().addListSelectionListener(this);

        usersTableModel.addTableModelListener(this);



        //we passen nog enkele weergave-dingen aan

        TableColumn roleColumn = usersTable.getColumn(5);
        JComboBox rolesComboBox = new JComboBox(User.Role.values());
        roleColumn.setCellEditor(new DefaultCellEditor(rolesComboBox));


        JScrollPane usersTableScrollPane = new JScrollPane(usersTable);
        add(usersTableScrollPane, BorderLayout.CENTER);

        //de onderste balk


        add(new UsersListActionPanel(), BorderLayout.SOUTH);

        //we stellen de acties in

        createUser.setActionCommand(CREATE_USER_ACTION);
        createUser.addActionListener(this);

        editUser.setEnabled(false);
        editUser.setActionCommand(EDIT_USER_ACTION);
        editUser.addActionListener(this);

        deleteUser.setEnabled(false);
        deleteUser.setActionCommand(DELETE_USER_ACTION);
        deleteUser.addActionListener(this);

        //we laden hier onze gegevens in
        SwingWorker<Collection<User>, Void> worker = new SwingWorker<Collection<User>, Void>() {

            @Override
            protected Collection<User> doInBackground() throws Exception {
                return new UserFactory().getAllUsers();
            }

            @Override
            protected void done() {
                try {
                    usersTableModel.setUsers(get());

                    usersTableModel.fireTableDataChanged();

                } catch (InterruptedException ex) {
                    JXErrorPane.showDialog(new Exception(translations.getString("ERROR_FETCH_USERS_THREAD"), ex));
                } catch (ExecutionException ex) {
                    JXErrorPane.showDialog(new Exception(translations.getString("ERROR_FETCH_USERS_EXECUTION_EXCEPTION"), ex));
                }
            }
        };

        worker.execute();

    }

    public void actionPerformed(ActionEvent e) {

        if (e.getActionCommand().equals(CREATE_USER_ACTION)) {

            User newUser = usersFactory.createUser();

            EditUserDialog editDialog = new EditUserDialog((JFrame) this.getTopLevelAncestor(), newUser, true);


            editDialog.setVisible(true);
            editDialog.addWindowListener(new WindowAdapter() {

                @Override
                public void windowClosed(WindowEvent e) {
                    EditUserDialog editDialog = (EditUserDialog) e.getSource();
                    System.out.println(editDialog.isSuccess() + " --> " + editDialog.getUser().getNickname());
                    if (e.getNewState() == WindowEvent.WINDOW_CLOSED) {
                        //TODO dit werkt niet
                        if (editDialog.isSuccess()) {
                            usersTableModel.addUser(editDialog.getUser());
                        }
                    }
                }
            });



        } else if (e.getActionCommand().equals(EDIT_USER_ACTION)) {
            for (int rownr : usersTable.getSelectedRows()) {

                EditUserDialog editFrame = new EditUserDialog((JFrame) this.getTopLevelAncestor(), usersTableModel.getUserAt(rownr));
                editFrame.setVisible(true);

                usersTableModel.setUserAt(rownr, editFrame.getUser());

            }
        } else if (e.getActionCommand().equals(DELETE_USER_ACTION)) {


            //we stellen eerst de vraag op
            String question = "";
            if (usersTable.getSelectedRowCount() > 1) {

                String selectedUsers = "";
                for (int rownr : usersTable.getSelectedRows()) {
                    User user = usersTableModel.getUserAt(rownr);

                    selectedUsers += user.getNickname() + " (ID: " + user.getId() + "), ";

                }
                selectedUsers = selectedUsers.substring(0, selectedUsers.length() - 2);

                question = String.format(translations.getString("CONFIRM_USERS_DELETION"), selectedUsers);
            } else {

                User user = usersTableModel.getUserAt(usersTable.getSelectedRow());
                question = String.format(translations.getString("CONFIRM_USER_DELETION"), user.getNickname() + " (ID: " + user.getId() + ")");

            }

            if (JOptionPane.showConfirmDialog(this, question, translations.getString("CONFIRM_USER_DELETION_TITLE"), JOptionPane.YES_NO_CANCEL_OPTION) == JOptionPane.YES_OPTION) {

                for (int rownr : usersTable.getSelectedRows()) {
                    try {
                        usersFactory.removeUser(usersTableModel.getUserAt(rownr));
                        usersTableModel.removeUserAt(rownr);
                    } catch (XmlRpcException ex) {
                        JXErrorPane.showDialog(this, new ErrorInfo(translations.getString("ERROR"), translations.getString("ERROR_DELETING_USER"), null, null, ex, null, null));

                    }

                }



            }


        }

    }

    public void tableChanged(TableModelEvent e) {
        checkButtons();
    }

    public void valueChanged(ListSelectionEvent e) {
        selectedLabel.setText(translations.getString("SELECTED_USERS_COUNT") + usersTable.getSelectedRowCount());
        checkButtons();

    }

    private void checkButtons() {

        editUser.setEnabled(usersTable.getSelectedRowCount() > 0);
        deleteUser.setEnabled(usersTable.getSelectedRowCount() > 0);

    }

    private class UsersListActionPanel extends JPanel {

        public UsersListActionPanel() {
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

            add(Box.createHorizontalGlue());
            add(createUser);
            add(editUser);
            add(deleteUser);
        }
    }
}
