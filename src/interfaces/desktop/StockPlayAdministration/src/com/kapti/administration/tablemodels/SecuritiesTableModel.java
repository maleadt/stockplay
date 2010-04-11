package com.kapti.administration.tablemodels;

import com.kapti.administration.bo.finance.Exchange;
import com.kapti.administration.bo.finance.Security;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Dieter
 */
public class SecuritiesTableModel extends AbstractTableModel {

    private final static String[] columnTitles = {"ISIN", "Symbool", "Beurs", "Naam", "Type", "Zichtbaar", "Geschorst"};
    private final static Class[] columnTypes = {String.class, String.class, String.class, String.class, String.class, Boolean.class, Boolean.class};
    private final static boolean[] editableColumns = new boolean[]{false, false, false, true, true, true, true};
    //Andere opmaak van datums kan bekommen worden met een "SimpleDateFormat" toe te passen
    //maar geen enkel formaat dat ik ingeef wordt ooit geaccepteerd...
    Security[] securities = new Security[]{};

    public SecuritiesTableModel() {
    }

    public void setSecurities(Collection<Security> securities) {
        this.securities = securities.toArray(this.securities);
        for (int i = 0; i < securities.size(); i++) {
            this.securities[i].addPropertyChangeListener(new SecurityPropertyChangeListener(this, i));

        }
    }

    private class SecurityPropertyChangeListener implements PropertyChangeListener {

        private int index;
        private SecuritiesTableModel model;

        public SecurityPropertyChangeListener(SecuritiesTableModel model, int index) {
            this.model = model;
            this.index = index;
        }

        public void propertyChange(PropertyChangeEvent evt) {
            model.fireTableRowsUpdated(index, index);
        }
    }

    public Security[] getSecurities() {
        return securities;
    }

    public int getRowCount() {
        return securities.length;
    }

    public int getColumnCount() {
        return columnTitles.length;
    }

    public Security getSecurityAt(int rowIndex) {
        return securities[rowIndex];
    }

    public void setSecurityAt(int rowIndex, Security security) {
        securities[rowIndex] = security;
        this.fireTableRowsUpdated(rowIndex, rowIndex);
    }

    public Object getValueAt(int rowIndex, int columnIndex) {

        Security security = securities[rowIndex];

        switch (columnIndex) {
            case 0:
                return security.getISIN();
            case 1:
                return security.getSymbol();
            case 2:
                return security.getExchange().getName();
            case 3:
                return security.getName();

            case 4:
                return security.getType();
            case 5:
                return security.isVisible();
            case 6:
                return security.isSuspended();
            default:
                return null;
        }

    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Security security = securities[rowIndex];

        switch (columnIndex) {

            case 2:
                security.setExchange((Exchange) aValue);
                break;
            case 3:
                security.setName((String) aValue);
                break;
            case 4:
                security.setType((Security.SecurityType) aValue);
                break;
            case 5:
                security.setVisible((Boolean) aValue);
                break;
            case 6:
                security.setSuspended((Boolean) aValue);
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
