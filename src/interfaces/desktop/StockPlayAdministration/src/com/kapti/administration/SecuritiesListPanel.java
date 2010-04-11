/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.administration;

import com.kapti.administration.bo.finance.Exchange;
import com.kapti.administration.bo.finance.FinanceFactory;
import com.kapti.administration.bo.finance.Security;
import com.kapti.administration.tablemodels.SecuritiesTableModel;
import com.kapti.exceptions.StockPlayException;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Stack;
import java.util.concurrent.ExecutionException;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import org.apache.log4j.Level;
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
public class SecuritiesListPanel extends JPanel implements TableModelListener, ListSelectionListener {

    private static Logger logger = Logger.getLogger(SecuritiesListPanel.class);
    private FinanceFactory finFactory = new FinanceFactory();
    private static SecuritiesListPanel instance = new SecuritiesListPanel();
    private JLabel selectedLabel = new JLabel();
    private JXTable securitiesTable = null;
    private SecuritiesTableModel securitiesTableModel = null;
    private JButton showSecurity = new JButton("Toon effect");
    private JButton hideSecurity = new JButton("Verberg effect");
    private JButton resumeSecurity = new JButton("Herstel effect");
    private JButton suspendSecurity = new JButton("Schors effect");
    private JButton saveButton = new JButton("Opslaan");
    private JButton undoButton = new JButton("Ongedaan maken");
    private Stack<SecurityState> undoStack = new Stack<SecurityState>();

    public static SecuritiesListPanel getInstance() {
        return instance;
    }

    private void AddSecurityStateToUndoStack(int rowIndex) {
        try {
            undoStack.add(new SecurityState(rowIndex, (Security) securitiesTableModel.getSecurityAt(rowIndex).clone()));
        } catch (CloneNotSupportedException ex) {
        }
        undoButton.setEnabled(true);
    }

    private SecuritiesListPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titel = new JLabel("Overzicht");
        titel.setFont(titel.getFont().deriveFont(Font.BOLD, 30));
        add(titel, BorderLayout.NORTH);


        //We stellen hier onze securities-JXTable in
        securitiesTableModel = new SecuritiesTableModel();

        securitiesTable = new JXTable(securitiesTableModel);
        securitiesTable.setColumnControlVisible(true);
        securitiesTable.setShowGrid(false, false);
        securitiesTable.setHighlighters(HighlighterFactory.createAlternateStriping());
        securitiesTable.setAutoCreateRowSorter(true);
        securitiesTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        securitiesTable.getSelectionModel().addListSelectionListener(this);

        securitiesTableModel.addTableModelListener(this);



        //we passen nog enkele weergave-dingen aan als het op editen aankomt
        TableColumn typeColumn = securitiesTable.getColumn(4);

        JComboBox typeComboBox = new JComboBox();
        for (Security.SecurityType type : Security.SecurityType.values()) {
            typeComboBox.addItem(type);
        }

        typeColumn.setCellEditor(new DefaultCellEditor(typeComboBox));

        TableColumn exchangeColumn = securitiesTable.getColumn(2);

        JComboBox exchangesComboBox = new JComboBox();
        try {
            for (Exchange exch : finFactory.getAllExchanges()) {
                exchangesComboBox.addItem(exch);
            }
        } catch (StockPlayException ex) {
            JXErrorPane.showDialog(new Exception("An exception occured while rendering the exchanges-combobox", ex));
        }

        exchangeColumn.setCellEditor(new DefaultCellEditor(exchangesComboBox));


        //securitiesTable.getColumn(5).setCellEditor(new DefaultCellEditor(new JCheckBox()));
        //securitiesTable.getColumn(6).setCellEditor(new DefaultCellEditor(new JCheckBox()));

        JScrollPane securitiesTableScrollPane = new JScrollPane(securitiesTable);
        add(securitiesTableScrollPane, BorderLayout.CENTER);

        //de onderste balk


        add(new SecuritiesListActionPanel(), BorderLayout.SOUTH);


        //we stellen de acties in

        showSecurity.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                for (int rownr : securitiesTable.getSelectedRows()) {
                    AddSecurityStateToUndoStack(rownr);
                    Security sec = securitiesTableModel.getSecurityAt(rownr);
                    sec.setVisible(true);
                }

            }
        });


        hideSecurity.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        for (int rownr : securitiesTable.getSelectedRows()) {
                            AddSecurityStateToUndoStack(rownr);
                            Security sec = securitiesTableModel.getSecurityAt(rownr);
                            sec.setVisible(false);
                        }
                    }
                });
        resumeSecurity.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        for (int rownr : securitiesTable.getSelectedRows()) {
                            AddSecurityStateToUndoStack(rownr);
                            Security sec = securitiesTableModel.getSecurityAt(rownr);
                            sec.setSuspended(false);
                        }
                    }
                });

        suspendSecurity.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        for (int rownr : securitiesTable.getSelectedRows()) {
                            AddSecurityStateToUndoStack(rownr);
                            Security sec = securitiesTableModel.getSecurityAt(rownr);
                            sec.setSuspended(true);
                        }
                    }
                });


        saveButton.addActionListener(new ActionListener() {

            boolean success = true;

            public void actionPerformed(ActionEvent e) {
                for (Security sec : securitiesTableModel.getSecurities()) {
                    try {
                        if (!finFactory.makePersistent(sec)) {
                            success = false;
                        }

                    } catch (XmlRpcException ex) {
                        logger.error("Error while saving changes to security", ex);
                        success = false;
                    }
                }

                if (success) {
                    undoStack.clear();
                    undoButton.setEnabled(false);
                    saveButton.setEnabled(false);
                } else {
                    JXErrorPane.showDialog(null, new ErrorInfo("Error", "Error while saving changes to securities", "", "", null, null, null));
                }
            }
        });


        undoButton.setEnabled(false);
        undoButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                SecurityState ss = undoStack.pop();

                securitiesTableModel.setSecurityAt(ss.getRowNumber(), ss.getSecurity());
                if (undoStack.empty()) {
                    undoButton.setEnabled(false);
                }
            }
        });

        //we laden hier onze gegevens in

        SwingWorker<Collection<Security>, Void> worker = new SwingWorker<Collection<Security>, Void>() {

            @Override
            protected Collection<Security> doInBackground() throws Exception {
                return new FinanceFactory().getAllSecurities();
            }

            @Override
            protected void done() {
                try {
                    securitiesTableModel.setSecurities(get());

                    securitiesTableModel.fireTableDataChanged();

                } catch (InterruptedException ex) {
                    JXErrorPane.showDialog(new Exception("An error occured while fetching the securities: the thread got interrupted", ex));
                } catch (ExecutionException ex) {
                    JXErrorPane.showDialog(new Exception("An error occured while fetching the securities: execution exception", ex));
                }
            }
        };

        worker.execute();

    }

    public void tableChanged(TableModelEvent e) {
        checkButtons();
    }

    public void valueChanged(ListSelectionEvent e) {
        selectedLabel.setText("Aantal geselecteerde effecten: " + securitiesTable.getSelectedRowCount());
        checkButtons();

    }

    private void checkButtons() {
        boolean show = false, hide = false, resume = false, suspend = false;
        for (int rowIndex : securitiesTable.getSelectedRows()) {
            Security sec = securitiesTableModel.getSecurityAt(rowIndex);

            if (sec.isVisible()) {
                hide = true;
            } else {
                show = true;
            }

            if (sec.isSuspended()) {
                resume = true;
            } else {
                suspend = true;
            }
        }

        showSecurity.setEnabled(show);
        hideSecurity.setEnabled(hide);
        resumeSecurity.setEnabled(resume);
        suspendSecurity.setEnabled(suspend);
    }

    private class SecuritiesListActionPanel extends JPanel {

        public SecuritiesListActionPanel() {
            setLayout(new BorderLayout());


            add(new JPanel() {

                {
                    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
                    setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

                    add(resumeSecurity);
                    add(suspendSecurity);

                    add(Box.createHorizontalStrut(10));

                    add(showSecurity);
                    add(hideSecurity);

                }
            }, BorderLayout.NORTH);


            add(new JPanel() {

                {
                    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
                    add(selectedLabel);
                    add(Box.createHorizontalGlue());

                    add(saveButton);
                    add(undoButton);
                }
            }, BorderLayout.SOUTH);
        }
    }

    public void setFilter(Exchange exchange) {
        if (exchange == null) {
            securitiesTable.setRowFilter(null);
        } else {
            securitiesTable.setRowFilter(new ExchangeRowFilter(exchange));
        }
    }

    private class ExchangeRowFilter extends javax.swing.RowFilter<TableModel, Integer> {

        private Exchange exchange;

        public ExchangeRowFilter(Exchange exchange) {
            this.exchange = exchange;
        }

        @Override
        public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {
            SecuritiesTableModel model = (SecuritiesTableModel) entry.getModel();

            Security sec = model.getSecurityAt(entry.getIdentifier());

            return sec.getExchange().equals(exchange);
        }
    }
}
