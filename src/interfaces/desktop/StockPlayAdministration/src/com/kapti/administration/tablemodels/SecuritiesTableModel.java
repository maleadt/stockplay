package com.kapti.administration.tablemodels;

import com.kapti.administration.bo.finance.Security;
import java.util.Collection;
import java.util.Date;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Dieter
 */
public class SecuritiesTableModel extends AbstractTableModel {

    private final static String[] columnTitles = {"Symbool", "Beurs", "Naam", "Type", "Zichtbaar", "Geschorst"};
    private final static Class[] columnTypes = {String.class, String.class, String.class, String.class, Boolean.class, Boolean.class};
    private final static boolean[] editableColumns = new boolean[]{false, false, true, true, true, true};
    //Andere opmaak van datums kan bekommen worden met een "SimpleDateFormat" toe te passen
    //maar geen enkel formaat dat ik ingeef wordt ooit geaccepteerd...
    Security[] securities = new Security[] {};

    public SecuritiesTableModel(Collection<Security> securities) {

        this.securities = securities.toArray(this.securities);
    }

    public int getRowCount() {
        return securities.length;
    }

    public int getColumnCount() {
        return columnTitles.length;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {

        Security security = securities[rowIndex];

        switch (columnIndex) {
            case 0:
                return security.getSymbol();
            case 1:
                return security.getExchange().getName();
            case 2:
                return security.getName();

            case 3:
                return security.getType();
            case 4:
                return security.isVisible();
            case 5:
                return security.isSuspended();
            default:
                return null;
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
