package com.kapti.administration;

import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.Format;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ValueChangeWithReasonDialog<T extends Number> extends JDialog implements ActionListener, ChangeListener {

    private ComputeChange changeComputer;
    private SpinnerModel valueSpinnerModel;
    private Format valueFormatter;
    private String valueName;
    private T initialValue;
    private boolean success = false;
    private ValueChange<T> valueChange;
    private JFormattedTextField currentValueField;
    private JFormattedTextField newValueField;
    private JSpinner changeSpinner;
    private JTextField reasonField;
    private JButton saveButton;

    public boolean isSuccess() {
        return success;
    }



    public ValueChangeWithReasonDialog( String valueName, T initialValue, Format valueFormatter, SpinnerModel valueSpinnerModel, ComputeChange changeComputer, ValueChange<T> valueChange) {
        this.valueName = valueName;
        this.initialValue = initialValue;
        this.valueFormatter = valueFormatter;
        this.valueSpinnerModel = valueSpinnerModel;
        this.changeComputer = changeComputer;
        this.valueChange = valueChange;

        setLayout(new GridBagLayout());
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setLocationRelativeTo(null);


        GridBagConstraints cTitel = new GridBagConstraints();
        cTitel.anchor = GridBagConstraints.CENTER;
        cTitel.fill = GridBagConstraints.HORIZONTAL;
        cTitel.gridwidth = GridBagConstraints.REMAINDER;
        cTitel.insets = new Insets(10,10,10,10);
        cTitel.gridx = 0;
        cTitel.gridy = 0;

        JLabel titelLabel = new JLabel("Verander waarde van " + valueName);
        titelLabel.setFont(titelLabel.getFont().deriveFont(Font.BOLD, 17));
        add(titelLabel, cTitel);

        GridBagConstraints cLabel = new GridBagConstraints();
        cLabel.anchor = GridBagConstraints.LINE_END;
        cLabel.insets = new Insets(5, 10, 0, 0);
        cLabel.gridx = 0;
        cLabel.gridy = 1;

        GridBagConstraints cItem = new GridBagConstraints();
        cItem.fill = GridBagConstraints.HORIZONTAL;
        cItem.gridx = 1;
        cItem.gridy = 1;
        cItem.insets = new Insets(5, 0, 0, 0);

        JLabel currentValueLabel = new JLabel("Huidige waarde:");
        add(currentValueLabel, cLabel);

        currentValueField = new JFormattedTextField(valueFormatter);
        currentValueField.setValue(initialValue);
        currentValueField.setEditable(false);
        add(currentValueField, cItem);

        cLabel.gridy++;
        cItem.gridy++;

        JLabel changeLabel = new JLabel("Verandering:");
        add(changeLabel, cLabel);

        changeSpinner = new JSpinner(valueSpinnerModel);
        changeSpinner.setValue(valueChange.getDelta());
        changeSpinner.addChangeListener(this);

        add(changeSpinner, cItem);

        cLabel.gridy++;
        cItem.gridy++;

        JLabel newValueLabel = new JLabel("Nieuwe waarde:");
        add(newValueLabel, cLabel);

        newValueField = new JFormattedTextField(valueFormatter);
        newValueField.setValue(initialValue);
        newValueField.setEditable(false);
        add(newValueField, cItem);

        //we updaten de huidige waarde
        updateNewValue();

        cLabel.gridy++;
        cItem.gridy++;

        JLabel reasonLabel = new JLabel("Reden:");
        add(reasonLabel, cLabel);

        cItem.insets = new Insets(15, 0, 0, 0);

        reasonField = new JTextField();
        reasonField.setColumns(20);
        reasonField.setText(valueChange.getReason());

        add(reasonField, cItem);


        cItem.gridy++;
        saveButton = new JButton("Save");

        saveButton.addActionListener(this);
        add(saveButton, cItem);

        getRootPane().setDefaultButton(saveButton);

        pack();

    }

    public ValueChange getValueChange() {
        return valueChange;
    }

    public void actionPerformed(ActionEvent e) {

        //we controleren of de vleden correct zijn ingevuld
        if (reasonField.getText().length() < 5) {

            JOptionPane.showMessageDialog(reasonField, "Gelieve een (voldoende lange) reden van de wijziging in te geven!", "Fout: reden te kort", JOptionPane.WARNING_MESSAGE);
            reasonField.requestFocus();

        }
        else {
            valueChange.setDelta((T) changeSpinner.getValue());
            valueChange.setReason(reasonField.getText());

            success = true;
            setVisible(false);
        }
    }

    public void stateChanged(ChangeEvent e) {
        updateNewValue();
    }

    private void updateNewValue() {
        valueChange.setDelta((T) changeSpinner.getValue());
        newValueField.setValue(changeComputer.sum(initialValue, (T) changeSpinner.getValue()));
    }

    public interface ComputeChange<T> {

        T sum(T t1, T t2);
    }
}
