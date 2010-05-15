/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.administration;

import com.kapti.client.user.User;
import com.kapti.client.user.UserFactory;
import com.kapti.administration.tablemodels.UsersTableModel;
import com.kapti.exceptions.StockPlayException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXErrorPane;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.error.ErrorInfo;

/**
 *
 * @author Thijs
 */
public class UsersListPanel extends JPanel implements TableModelListener, ListSelectionListener, ActionListener {

    private static final String REFRESH_ACTION = "REFRESH";
    private static final String EDIT_USER_ACTION = "EDIT_USER";
    private static final String CREATE_USER_ACTION = "CREATE_USER";
    private static final String DELETE_USER_ACTION = "DELETE_USER";
    private static Logger logger = Logger.getLogger(UsersListPanel.class);
    private UserFactory usersFactory = UserFactory.getInstance();
    private static UsersListPanel instance = new UsersListPanel();
    private final ResourceBundle translations = ResourceBundle.getBundle("com/kapti/administration/translations");
    private JLabel selectedLabel = new JLabel(translations.getString("SELECTED_USERS_COUNT") + " 0");
    private JLabel titleLabel = new JLabel(translations.getString("OVERVIEW"));
    private JXTable usersTable = null;
    private UsersTableModel usersTableModel = null;
    private JButton refreshButton = new JButton(translations.getString("REFRESH"));
    private JButton createUser = new JButton(translations.getString("CREATE_USER"));
    private JButton editUser = new JButton(translations.getString("EDIT_USER"));
    private JButton deleteUser = new JButton(translations.getString("DELETE_USER"));
    //filtering
    private NicknameRowFilter nicknameRowFilter = null;
    private RegTimeRowFilter regTimeRowFilter = null;
    private StringRowFilter stringRowFilter = null;
    private JLabel searchLabel = new JLabel(translations.getString("SEARCH_USER"));
    private JTextField searchField = new JTextField();

    public static UsersListPanel getInstance() {
        return instance;
    }

    private UsersListPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 30));
        add(titleLabel, BorderLayout.NORTH);


        //We stellen hier onze securities-JXTable in
        usersTableModel = new UsersTableModel();

        usersTable = new JXTable(usersTableModel);
        usersTable.setColumnControlVisible(true);
        usersTable.setShowGrid(false, false);
        usersTable.setHighlighters(HighlighterFactory.createAlternateStriping());
        usersTable.setAutoCreateRowSorter(true);
        usersTable.setAutoResizeMode(JXTable.AUTO_RESIZE_OFF);
        usersTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        usersTable.getSelectionModel().addListSelectionListener(this);

        usersTableModel.addTableModelListener(this);

        //we passen nog enkele weergave-dingen aan
        TableColumn roleColumn = usersTable.getColumn(5);
        JComboBox rolesComboBox = new JComboBox(User.Role.values());
        roleColumn.setCellEditor(new DefaultCellEditor(rolesComboBox));


        JScrollPane usersTableScrollPane = new JScrollPane(usersTable);
        usersTableScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(usersTableScrollPane, BorderLayout.CENTER);

        //de onderste balk


        add(new UsersListActionPanel(), BorderLayout.SOUTH);

        //we stellen de acties in
        refreshButton.setActionCommand(REFRESH_ACTION);
        refreshButton.addActionListener(this);

        createUser.setActionCommand(CREATE_USER_ACTION);
        createUser.addActionListener(this);

        editUser.setEnabled(false);
        editUser.setActionCommand(EDIT_USER_ACTION);
        editUser.addActionListener(this);

        deleteUser.setEnabled(false);
        deleteUser.setActionCommand(DELETE_USER_ACTION);
        deleteUser.addActionListener(this);

        //we laden hier onze gegevens in
        fetchUsersTable();

    }

    private void fetchUsersTable() {
        SwingWorker<Collection<User>, Void> worker = new SwingWorker<Collection<User>, Void>() {

            @Override
            protected Collection<User> doInBackground() throws Exception {
                return UserFactory.getInstance().getAllUsers();
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

    public void filterByNickname(String nicknameFilter, String friendlyName) {
        if (nicknameFilter != null) {
            regTimeRowFilter = null;
            nicknameRowFilter = new NicknameRowFilter(nicknameFilter, friendlyName);
        } else {
            nicknameRowFilter = null;
        }

        updateFilters();
    }

    public void resetFilters() {
        nicknameRowFilter = null;
        regTimeRowFilter = null;

        searchField.setText("");
        updateFilters();
    }

    private class NicknameRowFilter extends javax.swing.RowFilter<TableModel, Integer> {

        private Pattern nicknameFilterPattern;
        private String friendlyName = "";

        public NicknameRowFilter(String nicknameFilter, String friendlyName) {
            nicknameFilterPattern = Pattern.compile(nicknameFilter);

            this.friendlyName = friendlyName;

        }

        public String getFriendlyName() {
            return friendlyName;
        }

        @Override
        public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {
            UsersTableModel model = (UsersTableModel) entry.getModel();

            User u = model.getUserAt(entry.getIdentifier());

            return nicknameFilterPattern.matcher(u.getNickname()).matches();
        }
    }

    public void filterByRegtime(long period, String friendlyName) {
        if (period > 0) {
            nicknameRowFilter = null;
            regTimeRowFilter = new RegTimeRowFilter(period, friendlyName);
        } else {
            regTimeRowFilter = null;
        }
        updateFilters();
    }

    private class RegTimeRowFilter extends javax.swing.RowFilter<TableModel, Integer> {

        private long period;
        private String friendlyName = "";

        public RegTimeRowFilter(long period, String friendlyName) {
            this.period = period;
            this.friendlyName = friendlyName;

        }

        public String getFriendlyName() {
            return friendlyName;
        }

        @Override
        public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {
            UsersTableModel model = (UsersTableModel) entry.getModel();

            User u = model.getUserAt(entry.getIdentifier());

            return Calendar.getInstance().getTime().getTime() - u.getRegdate().getTime() <= period;
        }
    }

    private class StringRowFilter extends javax.swing.RowFilter<TableModel, Integer> {

        private String str;

        public StringRowFilter(String str) {
            this.str = str;
        }

        @Override
        public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {
            UsersTableModel model = (UsersTableModel) entry.getModel();
            User u = model.getUserAt(entry.getIdentifier());

            Pattern p = Pattern.compile(str + ".*", Pattern.CASE_INSENSITIVE);

            if (p.matcher(u.getFirstname()).matches()) {
                return true;
            }

            if (p.matcher(u.getLastname()).matches()) {
                return true;
            }

            if (p.matcher(u.getNickname()).matches()) {
                return true;
            }

            if (p.matcher(u.getEmail()).matches()) {
                return true;
            }

            if (p.matcher(u.getRijksregisternummer().toString()).matches()) {
                return true;
            }

            return false;
        }
    }

    private void updateFilters() {
        List<RowFilter<TableModel, Integer>> filters = new ArrayList<RowFilter<TableModel, Integer>>();

        String title = translations.getString("OVERVIEW");

        if (nicknameRowFilter != null) {
            filters.add(nicknameRowFilter);
            title = translations.getString("USER_NICKNAME_FILTER") + " '" + nicknameRowFilter.getFriendlyName() + "'";
        }

        if (regTimeRowFilter != null) {
            filters.add(regTimeRowFilter);
            title = translations.getString("USER_REGTIME_FILTER") + " '" + regTimeRowFilter.getFriendlyName() + "'";

        }
        if (stringRowFilter != null) {
            filters.add(stringRowFilter);
            title = title.concat(" " + translations.getString("FILTERED"));
        }

        if (!filters.isEmpty()) {
            RowFilter<TableModel, Integer> comboFilter = RowFilter.andFilter(filters);
            usersTable.setRowFilter(comboFilter);
        } else {
            usersTable.setRowFilter(null);
        }

        titleLabel.setText(title);



    }

    public void actionPerformed(ActionEvent e) {

        if (e.getActionCommand().equals(REFRESH_ACTION)) {
            fetchUsersTable();

        } else if (e.getActionCommand().equals(CREATE_USER_ACTION)) {

            User newUser = usersFactory.createUser();

            EditUserDialog editDialog = new EditUserDialog((JFrame) this.getTopLevelAncestor(), newUser, translations.getString("NEW_USER_TITLE"), true);


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

                EditUserDialog editFrame = new EditUserDialog((JFrame) this.getTopLevelAncestor(), usersTableModel.getUserAt(rownr), String.format(translations.getString("EDIT_USER_TITLE"), usersTableModel.getUserAt(rownr).getId()));
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
                    } catch (StockPlayException ex) {
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
        selectedLabel.setText(translations.getString("SELECTED_USERS_COUNT") + " " + usersTable.getSelectedRowCount());
        checkButtons();
    }

    private void checkButtons() {
        editUser.setEnabled(usersTable.getSelectedRowCount() > 0);
        deleteUser.setEnabled(usersTable.getSelectedRowCount() > 0);
    }

    private class UsersListActionPanel extends JPanel {

        public UsersListActionPanel() {

            setLayout(new BorderLayout());
            add(new JPanel() {

                {
                    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
                    setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
                    add(selectedLabel);

                    add(Box.createHorizontalGlue());
                    add(createUser);
                    add(editUser);
                    add(deleteUser);
                }
            }, BorderLayout.NORTH);

            add(new JPanel() {

                {
                    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
                    setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
                    add(searchLabel);
                    add(searchField);

                    add(Box.createHorizontalStrut(300));
                    add(Box.createHorizontalGlue());
                    add(refreshButton);
                }
            }, BorderLayout.SOUTH);
        }
    }
}
