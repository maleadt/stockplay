package com.kapti.administration.tablemodels;

import java.util.Date;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Dieter
 */
public class UsersTableModel extends AbstractTableModel {

    private final static String[] kolomTitels = {"Gebruikersnaam", "Registratiedatum", "Laatste activiteit", "E-mail"};
    private final static Class[] kolomTypes = {String.class, Date.class, Date.class, String.class};
    private Object[][] dummyData = {    {"Tim Besard", new Date(), new Date(), "tim.besard@gmail.com"},
                                        {"Dieter Deforce", new Date(), new Date(), "deforcedieter@hotmail.com"},
                                        {"Thijs Walcarius", new Date(), new Date(), "thijs.walcarius@gmail.com"},
                                        {"Laurens Van Acker", new Date(), new Date(), "laurens@van.acker.be"}};


    public int getRowCount() {
        return dummyData.length;
    }

    public int getColumnCount() {
        return kolomTitels.length;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return dummyData[rowIndex][columnIndex];
    }

    @Override
    public String getColumnName(int col) {
        return kolomTitels[col];
    }

    @Override
    public Class getColumnClass(int col) {
        return kolomTypes[col];
    }
}