/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.administration;

import com.kapti.administration.bo.finance.FinanceFactory;
import com.kapti.administration.tablemodels.SecuritiesTableModel;
import com.kapti.exceptions.StockPlayException;
import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;

/**
 *
 * @author Thijs
 */
public class SecuritiesListPanel extends JPanel {


    private static SecuritiesListPanel instance = new SecuritiesListPanel();

    public static SecuritiesListPanel getInstance() {
        return instance;
    }
    


    private SecuritiesListPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titel = new JLabel("Overzicht");
        titel.setFont(titel.getFont().deriveFont(Font.BOLD, 30));
        add(titel, BorderLayout.NORTH);


        try {
        JXTable securitiesTable = new JXTable(new SecuritiesTableModel(new FinanceFactory().getAllSecurities()));
        securitiesTable.setColumnControlVisible(true);
        securitiesTable.setShowGrid(false, false);
        securitiesTable.setHighlighters(HighlighterFactory.createAlternateStriping());
        securitiesTable.setAutoCreateRowSorter(true);
        securitiesTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);



        //we passen nog enkele weergave-dingen aan als het op editen aankomt
        TableColumn typeColumn = securitiesTable.getColumn(3);

        JComboBox typeComboBox = new JComboBox();
        typeComboBox.addItem("Aandeel");
        typeComboBox.addItem("Tracker");
        typeComboBox.addItem("Fonds");

        typeColumn.setCellEditor(new DefaultCellEditor(typeComboBox));



        JScrollPane securitiesTableScrollPane = new JScrollPane(securitiesTable);
        add(securitiesTableScrollPane, BorderLayout.CENTER);

        //de onderste balk


        add(new SecuritiesListActionPanel(), BorderLayout.SOUTH);
        } catch(StockPlayException ex){
            // TODO doe hier iets nuttigs
        }

        

    }

    private class SecuritiesListActionPanel extends JPanel{

        public SecuritiesListActionPanel(){
           setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
           setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

           JLabel selectedLabel = new JLabel("Aantal geselecteerde effecten: 0");
           add(selectedLabel);
           add(Box.createHorizontalGlue());
           add(new JButton("Schors effect"));
           add(new JButton("Verberg effect"));

        }


    }
}
