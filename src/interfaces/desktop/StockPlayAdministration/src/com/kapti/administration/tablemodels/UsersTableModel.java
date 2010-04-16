package com.kapti.administration.tablemodels;

import com.kapti.administration.bo.user.User;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Date;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Dieter
 */
public class UsersTableModel extends AbstractTableModel {

    private final static String[] columnTitles = {"Id", "Nickname", "E-mailadres", "Achternaam", "Voornaam", "Rol", "Registratiedatum", "Punten", "Cash", "Startbedrag", "Rijksregisternummer"};
    private final static Class[] columnTypes = {Integer.class, String.class, String.class, String.class, String.class, String.class, Date.class, Integer.class, Double.class, Double.class, Long.class};
    private final static boolean[] editableColumns = new boolean[]{false, true, true, true, true, true, false, false, false, false, true};
    //Andere opmaak van datums kan bekomen worden met een "SimpleDateFormat" toe te passen
    //maar geen enkel formaat dat ik ingeef wordt ooit geaccepteerd...
    Vector<User> users = new Vector<User>();

    public UsersTableModel() {
    }

    public void setUsers(Collection<User> users) {
        this.users.addAll(users);
    }

    public User[] getUsers() {
        return (User[])users.toArray();
    }

    public int getRowCount() {
        return users.size();
    }

    public int getColumnCount() {
        return columnTitles.length;
    }

    public User getUserAt(int rowIndex) {
        return users.get(rowIndex);
    }

    public void setUserAt(int rowIndex, User user) {
        users.set(rowIndex, user);
        this.fireTableRowsUpdated(rowIndex, rowIndex);
    }

    public void removeUserAt(int rowIndex){
        users.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public void removeUser(User user){
        removeUserAt(users.indexOf(user));
    }

    public void addUser(User user){
        users.add(user);
        this.fireTableRowsInserted(users.indexOf(user), users.indexOf(user));
    }

    public Object getValueAt(int rowIndex, int columnIndex) {

        User user = users.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return user.getId();
            case 1:
                return user.getNickname();
            case 2:
                return user.getEmail();
            case 3:
                return user.getLastname();
            case 4:
                return user.getFirstname();
            case 5:
                return user.getRole().toString();
            case 6:
                return user.getRegdate();
            case 7:
                return user.getPoints();
            case 8:
                return user.getCash();
            case 9:
                return user.getStartamount();
            case 10:
                return user.getRijksregisternummer();
            default:
                return null;
        }

    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        User user = users.get(rowIndex);

        switch (columnIndex) {
            case 1:
                user.setNickname((String)aValue);
                break;
            case 2:
                user.setEmail((String)aValue);
                break;
            case 3:
                user.setLastname((String) aValue);
                break;
            case 4:
                user.setFirstname((String) aValue);
                break;
            case 5:
                user.setRole((User.Role) aValue);
                break;
            case 7:
                user.setPoints((Integer)aValue);
                break;
            case 8:
                user.setCash((Double)aValue);
                break;
            case 10:
                user.setRijksregisternummer((Long)aValue);
                break;
        }
    }

    @Override
    public String getColumnName(int col) {
        return columnTitles[col];
    }

    @Override
    public Class getColumnClass(int col) {
        return columnTypes[col];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return editableColumns[columnIndex];
    }
}
