/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.administration;

import com.kapti.client.finance.Exchange;
import com.kapti.client.finance.FinanceFactory;
import com.kapti.client.finance.Security;
import com.kapti.administration.tablemodels.SecuritiesTableModel;
import com.kapti.client.finance.Index;
import com.kapti.exceptions.StockPlayException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import org.apache.log4j.Logger;
import org.jdesktop.jxlayer.JXLayer;
import org.jdesktop.swingx.JXErrorPane;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;

import org.jdesktop.jxlayer.plaf.ext.LockableUI;
import org.jdesktop.swingx.error.ErrorInfo;

/**
 *
 * @author Thijs
 */
public class SecuritiesListPanel extends JPanel implements TableModelListener, ListSelectionListener, ActionListener {

    private static final String REFRESH_ACTION = "REFRESH";
    private static final String SHOW_SECURITY_ACTION = "SHOW_SECURITY";
    private static final String HIDE_SECURITY_ACTION = "HIDE_SECURITY";
    private static final String RESUME_SECURITY_ACTION = "RESUME_SECURITY";
    private static final String SUSPEND_SECURITY_ACTION = "SUSPEND_SECURITY";
    private static final String SAVE_ACTION = "SAVE";
    private static Logger logger = Logger.getLogger(SecuritiesListPanel.class);
    private FinanceFactory finFactory = FinanceFactory.getInstance();
    private final ResourceBundle translations = ResourceBundle.getBundle("com/kapti/administration/translations");
    private static SecuritiesListPanel instance = new SecuritiesListPanel();
    private JLabel titleLabel = new JLabel(translations.getString("OVERVIEW"));
    private JLabel selectedLabel = new JLabel(translations.getString("SELECTED_SECURITIES_COUNT") + " 0");
    private LockableUI lockUI = null;
    private JXLayer<JComponent> busyLayer = null;
    private JXTable securitiesTable = null;
    private SecuritiesTableModel securitiesTableModel = null;
    private JButton showSecurity = new JButton(translations.getString("SHOW_SECURITY"));
    private JButton hideSecurity = new JButton(translations.getString("HIDE_SECURITY"));
    private JButton resumeSecurity = new JButton(translations.getString("RESUME_SECURITY"));
    private JButton suspendSecurity = new JButton(translations.getString("SUSPEND_SECURITY"));
    private JButton saveButton = new JButton(translations.getString("SAVE") + " (0)");
    private JButton refreshButton = new JButton(translations.getString("REFRESH"));
    //filtering
    private ExchangeRowFilter exchangeRowFilter = null;
    private IndexRowFilter indexRowFilter = null;
    private StringRowFilter stringRowFilter = null;
    private JLabel searchLabel = new JLabel(translations.getString("SEARCH_SECURITY"));
    private JTextField searchField = new JTextField();

    public static SecuritiesListPanel getInstance() {
        return instance;
    }

    private SecuritiesListPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 30));
        add(titleLabel, BorderLayout.NORTH);


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

        TableColumn exchangeColumn = securitiesTable.getColumn(2);
        JComboBox exchangesComboBox = new JComboBox();
        try {
            for (Exchange exch : finFactory.getAllExchanges()) {
                exchangesComboBox.addItem(exch);
            }
        } catch (StockPlayException ex) {
            JXErrorPane.showDialog(new Exception(translations.getString("ERROR_RENDERING_SECURITIESCOMBOBOX"), ex));
        }

        exchangeColumn.setCellEditor(new DefaultCellEditor(exchangesComboBox));

        JScrollPane securitiesTableScrollPane = new JScrollPane(securitiesTable);

        //we voegen de tabel toe aan de busylayer, zodat deze kan blokkeren tijdens het inladen
        lockUI = new LockableUI();

        busyLayer = new JXLayer<JComponent>(securitiesTableScrollPane, lockUI);
        busyLayer.setUI(lockUI);
        add(busyLayer, BorderLayout.CENTER);
        lockUI.setLocked(true);

        searchField.getDocument().addDocumentListener(new DocumentListener() {

            private void updateSearchFilter() {
                if (searchField.getText().length() > 0) {
                    stringRowFilter = new StringRowFilter(searchField.getText());
                } else {
                    stringRowFilter = null;
                }

                updateFilters();
            }

            public void insertUpdate(DocumentEvent e) {
                updateSearchFilter();
            }

            public void removeUpdate(DocumentEvent e) {
                updateSearchFilter();
            }

            public void changedUpdate(DocumentEvent e) {
                //niet nodig
            }
        });


        //de onderste balk
        add(new SecuritiesListActionPanel(), BorderLayout.SOUTH);

        //we stellen de acties in
        refreshButton.setActionCommand(REFRESH_ACTION);
        refreshButton.addActionListener(this);

        showSecurity.setActionCommand(SHOW_SECURITY_ACTION);
        showSecurity.addActionListener(this);

        hideSecurity.setActionCommand(HIDE_SECURITY_ACTION);
        hideSecurity.addActionListener(this);

        resumeSecurity.setActionCommand(RESUME_SECURITY_ACTION);
        resumeSecurity.addActionListener(this);

        suspendSecurity.setActionCommand(SUSPEND_SECURITY_ACTION);
        suspendSecurity.addActionListener(this);

        saveButton.setActionCommand(SAVE_ACTION);
        saveButton.addActionListener(this);
        saveButton.setEnabled(false);

        //we laden hier onze gegevens in

        fetchSecuritiesTable();
    }

    public void tableChanged(TableModelEvent e) {
        checkButtons();
        saveButton.setText(translations.getString("SAVE") + " (" + securitiesTableModel.getChangedRowsCount() +  ")");
        saveButton.setEnabled(securitiesTableModel.getChangedRowsCount() > 0);
    }

    public void valueChanged(ListSelectionEvent e) {
        selectedLabel.setText(translations.getString("SELECTED_SECURITIES_COUNT") + " " + securitiesTable.getSelectedRowCount());
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

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(REFRESH_ACTION)) {
            fetchSecuritiesTable();

        } else if (e.getActionCommand().equals(SHOW_SECURITY_ACTION)) {
            for (int rownr : securitiesTable.getSelectedRows()) {

                Security sec = securitiesTableModel.getSecurityAt(rownr);
                sec.setVisible(true);
            }
        } else if (e.getActionCommand().equals(HIDE_SECURITY_ACTION)) {
            for (int rownr : securitiesTable.getSelectedRows()) {
                Security sec = securitiesTableModel.getSecurityAt(rownr);
                sec.setVisible(false);
            }
        } else if (e.getActionCommand().equals(RESUME_SECURITY_ACTION)) {
            for (int rownr : securitiesTable.getSelectedRows()) {

                Security sec = securitiesTableModel.getSecurityAt(rownr);
                sec.setSuspended(false);
            }
        } else if (e.getActionCommand().equals(SUSPEND_SECURITY_ACTION)) {
            for (int rownr : securitiesTable.getSelectedRows()) {
                Security sec = securitiesTableModel.getSecurityAt(rownr);
                sec.setSuspended(true);
            }
        } else if (e.getActionCommand().equals(SAVE_ACTION)) {
            for (Security sec : securitiesTableModel.getSecurities()) {
                try {
                    if (!finFactory.makePersistent(sec)) {
                        throw new StockPlayException("Saving securities failed");
                    }
                } catch (StockPlayException ex) {
                    logger.error(translations.getString("ERROR_SAVING_SECURITY"), ex);
                    JXErrorPane.showDialog(null, new ErrorInfo(translations.getString("ERROR"), translations.getString("ERROR_SAVING_SECURITIES"), "", "", null, null, null));
                }
            }
        }

    }

    private void fetchSecuritiesTable() {
        SwingWorker<Collection<Security>, Void> worker = new SwingWorker<Collection<Security>, Void>() {

            @Override
            protected Collection<Security> doInBackground() throws Exception {
                return FinanceFactory.getInstance().getAllSecurities();
            }

            @Override
            protected void done() {
                try {
                    lockUI.setLocked(true);

                    securitiesTableModel.setSecurities(get());
                    securitiesTableModel.fireTableDataChanged();
                    lockUI.setLocked(false);

                } catch (InterruptedException ex) {
                    JXErrorPane.showDialog(new Exception(translations.getString("ERROR_FETCH_USERS_THREAD"), ex));
                } catch (ExecutionException ex) {
                    JXErrorPane.showDialog(new Exception(translations.getString("ERROR_FETCH_USERS_EXECUTION_EXCEPTION"), ex));
                }
            }
        };

        worker.execute();
    }

    private class SecuritiesListActionPanel extends JPanel {

        public SecuritiesListActionPanel() {
            setLayout(new BorderLayout());


            add(new JPanel() {

                {
                    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
                    setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

                    add(selectedLabel);

                    add(Box.createHorizontalGlue());
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


                    add(searchLabel);

                    add(searchField);
                    add(Box.createHorizontalStrut(300));
                    add(Box.createHorizontalGlue());
                    add(refreshButton);
                    add(saveButton);
                }
            }, BorderLayout.SOUTH);
        }
    }

    public void filterByExchange(Exchange exchange) {

        if (exchange == null) {
            exchangeRowFilter = null;
        } else {
            indexRowFilter = null;
            exchangeRowFilter = new ExchangeRowFilter(exchange);
        }
        updateFilters();
    }

    public void filterByIndex(Index index) {
        if (index == null) {
            indexRowFilter = null;
        } else {
            exchangeRowFilter = null;
            indexRowFilter = new IndexRowFilter(index);
        }
        updateFilters();
    }

    private void updateFilters() {
        List<RowFilter<TableModel, Integer>> filters = new ArrayList<RowFilter<TableModel, Integer>>();

        String title = translations.getString("OVERVIEW");

        if (exchangeRowFilter != null) {
            filters.add(exchangeRowFilter);
            title = translations.getString("SECURITIES_OF") + " '" + exchangeRowFilter.getExchange().getName() + "'";
        }

        if (indexRowFilter != null) {
            filters.add(indexRowFilter);
            title = translations.getString("SECURITIES_OF") + " '" + indexRowFilter.getIndex().getName() + "'";

        }
        if (stringRowFilter != null) {
            filters.add(stringRowFilter);
            title = title.concat(" " + translations.getString("FILTERED"));
        }

        if (!filters.isEmpty()) {
            RowFilter<TableModel, Integer> comboFilter = RowFilter.andFilter(filters);
            securitiesTable.setRowFilter(comboFilter);
        } else {
            securitiesTable.setRowFilter(null);
        }

        titleLabel.setText(title);

    }

    private class ExchangeRowFilter extends javax.swing.RowFilter<TableModel, Integer> {

        private Exchange exchange;

        public ExchangeRowFilter(Exchange exchange) {
            this.exchange = exchange;
        }

        public Exchange getExchange() {
            return exchange;
        }

        @Override
        public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {
            SecuritiesTableModel model = (SecuritiesTableModel) entry.getModel();
            Security sec = model.getSecurityAt(entry.getIdentifier());
            return sec.getExchange().equals(exchange);
        }
    }

    private class IndexRowFilter extends javax.swing.RowFilter<TableModel, Integer> {

        private Collection<Security> securitiesFromIndex;
        private Index index;

        public Index getIndex() {
            return index;
        }

        public IndexRowFilter(Index index) {
            try {
                this.index = index;
                securitiesFromIndex = FinanceFactory.getInstance().getSecuritiesFromIndex(index);
            } catch (StockPlayException ex) {
                logger.error("Error while loading securities from index", ex);
            }

        }

        @Override
        public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {
            SecuritiesTableModel model = (SecuritiesTableModel) entry.getModel();

            Security sec = model.getSecurityAt(entry.getIdentifier());

            return securitiesFromIndex.contains(sec);
        }
    }

    private class StringRowFilter extends javax.swing.RowFilter<TableModel, Integer> {

        String str;

        public StringRowFilter(String str) {
            this.str = str;
        }

        @Override
        public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {
            SecuritiesTableModel model = (SecuritiesTableModel) entry.getModel();
            Security sec = model.getSecurityAt(entry.getIdentifier());

            Pattern p = Pattern.compile(str + ".*", Pattern.CASE_INSENSITIVE);

            if (p.matcher(sec.getName()).matches()) {
                return true;
            }

            if (p.matcher(sec.getSymbol()).matches()) {
                return true;
            }

            if (p.matcher(sec.getISIN()).matches()) {
                return true;
            }

            return false;
        }
    }
}
