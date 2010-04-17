/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.administration;

import com.kapti.administration.ValueChangeWithReasonDialog.ComputeChange;
import com.kapti.administration.bo.user.User;
import com.kapti.administration.bo.user.UserFactory;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.MaskFormatter;
import org.apache.log4j.Logger;
import org.apache.xmlrpc.XmlRpcException;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

/**
 *
 * @author Thijs
 */
public class EditUserDialog extends JDialog implements ActionListener {

    private static Logger logger = Logger.getLogger(EditUserDialog.class);
    private static final String SAVE_ACTION = "SAVE";
    private static final String CANCEL_ACTION = "CANCEL";
    private UserFactory userFactory = UserFactory.getInstance();

    private String title;
    private User user;
    private JTextField nicknameField;
    private boolean passwordEdited = false;
    private JPasswordField passwordField;
    private JTextField lastnameField;
    private JTextField firstnameField;
    private JComboBox roleField;
    private JTextField emailField;
    private MaskFormatter rrnMask;
    private JFormattedTextField rrnField;
    private JFormattedTextField regdateField;
    private JFormattedTextField pointsField;
    private JFormattedTextField cashField;
    private JFormattedTextField startamountField;
    private JButton changeCashButton;
    private JButton changePointsButton;
    private JButton changePasswordButton;
    //, , PASSWORD, , , , REGDATE, ADMIN, POINTS, STARTAMOUNT, CASH, RRN
    private JButton saveButton;
    private JButton cancelButton;
    private boolean success;
    private ValueChange<Integer> pointsChange = new ValueChange<Integer>(0);
    private ValueChange<Double> cashChange = new ValueChange<Double>(0.0);

    public EditUserDialog(Frame owner, User user, String title, boolean mustChangePassword) {
        super(owner);
        this.user = user;
        this.title = title;
        this.passwordEdited = mustChangePassword;
        createDialog();
    }

    public EditUserDialog(Frame owner, User user, String title) {
        this(owner, user, title, false);
    }

    public EditUserDialog(User user) {
        this.user = user;
        createDialog();
    }

    private void createDialog() {

        JPanel contentPane = new JPanel();
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        setContentPane(contentPane);

        setLayout(new GridBagLayout());
        setLocationRelativeTo(this.getOwner());

        GridBagConstraints cTitel = new GridBagConstraints();
        cTitel.gridwidth = 10;
        cTitel.ipady = 25;
        cTitel.gridx = 0;
        cTitel.gridy = 0;
        cTitel.anchor = GridBagConstraints.LINE_START;
        JLabel titel = new JLabel(title);
        titel.setFont(titel.getFont().deriveFont(Font.BOLD, 20));

        add(titel, cTitel);

        GridBagConstraints cItemLabel = new GridBagConstraints();
        cItemLabel.anchor = GridBagConstraints.LINE_END;
        cItemLabel.gridx = 0;
        cItemLabel.gridy = 1;
        cItemLabel.insets = new Insets(5, 0, 0, 10);


        GridBagConstraints cItem = new GridBagConstraints();
        cItem.gridx = 1;
        cItem.gridy = 1;
        cItem.gridwidth = 2;
        cItem.fill = GridBagConstraints.HORIZONTAL;
        cItem.insets = new Insets(5, 10, 0, 0);


        GridBagConstraints cButton = (GridBagConstraints) cItem.clone();
        cButton.gridwidth = 1;
        cButton.gridx += 3;

        JLabel nicknameLabel = new JLabel("Nickname:");
        add(nicknameLabel, cItemLabel);


        nicknameField = new JTextField(user.getNickname());
        add(nicknameField, cItem);

        cItemLabel.gridy++;
        cItem.gridy++;
        cButton.gridy++;

        JLabel passwordLabel = new JLabel("Paswoord:");
        add(passwordLabel, cItemLabel);

        passwordField = new JPasswordField("********");
        passwordField.setEditable(false);
        add(passwordField, cItem);

        changePasswordButton = new JButton("Verander paswoord");
        changePasswordButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                passwordEdited = true;
                passwordField.setText("");
                passwordField.setEchoChar('*');
                passwordField.setEditable(true);
                passwordField.requestFocus();

                changePasswordButton.setEnabled(false);
            }
        });


        //indien reeds doorgegeven dat het paswoord moet veranderd worden, deze button direct (de)activeren
        if (passwordEdited) {
            changePasswordButton.doClick();
        }
        add(changePasswordButton, cButton);


        cItemLabel.gridy++;
        cItem.gridy++;
        cButton.gridy++;

        JLabel lastnameLabel = new JLabel("Achternaam:");
        add(lastnameLabel, cItemLabel);

        lastnameField = new JTextField(user.getLastname());
        add(lastnameField, cItem);

        cItemLabel.gridy++;
        cItem.gridy++;
        cButton.gridy++;

        JLabel firstnameLabel = new JLabel("Voornaam:");
        add(firstnameLabel, cItemLabel);

        firstnameField = new JTextField(user.getFirstname());
        add(firstnameField, cItem);

        cItemLabel.gridy++;
        cItem.gridy++;
        cButton.gridy++;

        JLabel emailLabel = new JLabel("Emailadres:");
        add(emailLabel, cItemLabel);

        emailField = new JTextField(user.getEmail());

        emailField.setInputVerifier(new InputVerifier() {

            private final Pattern emailPattern = Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?");

            @Override
            public boolean verify(JComponent input) {
                return emailPattern.matcher(((JTextField) input).getText()).matches();
            }
        });

        add(emailField, cItem);


        cItemLabel.gridy++;
        cItem.gridy++;
        cButton.gridy++;

        JLabel roleLabel = new JLabel("Rol:");
        add(roleLabel, cItemLabel);

        roleField = new JComboBox(User.Role.values());
        roleField.setSelectedItem(user.getRole());
        add(roleField, cItem);

        cItemLabel.gridy++;
        cItem.gridy++;
        cButton.gridy++;

        JLabel rrnLabel = new JLabel("Rijksregisternummer:");
        add(rrnLabel, cItemLabel);
        try {
            rrnMask = new MaskFormatter("##.##.##-###.##");
            rrnMask.setValueContainsLiteralCharacters(false);

            rrnField = new JFormattedTextField();
            if (user.getRijksregisternummer() != null) {
                rrnField.setValue(user.getRijksregisternummer().toString());
            }
            rrnMask.install(rrnField);

            add(rrnField, cItem);


        } catch (ParseException ex) {
        }

        cItemLabel.gridy++;
        cItem.gridy++;
        cButton.gridy++;

        JLabel regdateLabel = new JLabel("Registratiedatum:");
        add(regdateLabel, cItemLabel);

        regdateField = new JFormattedTextField(DateFormat.getDateInstance());
        if (user.getRegdate() != null) {
            regdateField.setValue(user.getRegdate());
        }
        regdateField.setEditable(false);
        add(regdateField, cItem);

        cItemLabel.gridy++;
        cItem.gridy++;
        cButton.gridy++;

        JLabel startamountLabel = new JLabel("Startbedrag:");
        add(startamountLabel, cItemLabel);

        startamountField = new JFormattedTextField(NumberFormat.getCurrencyInstance());
        startamountField.setValue(user.getStartamount());
        startamountField.setEditable(false);
        add(startamountField, cItem);

        cItemLabel.gridy++;
        cItem.gridy++;
        cButton.gridy++;

        JLabel cashLabel = new JLabel("Cash:");
        add(cashLabel, cItemLabel);

        cashField = new JFormattedTextField(NumberFormat.getCurrencyInstance());
        if (user.getCash() != null) {
            cashField.setValue(user.getCash());
        }
        cashField.setEditable(false);
        add(cashField, cItem);



        changeCashButton = new JButton("Verander cash");

        changeCashButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                ValueChangeWithReasonDialog dialog = new ValueChangeWithReasonDialog<Double>("Cash", user.getCash(), NumberFormat.getInstance(), new SpinnerNumberModel(0.0, -1000000, 1000000, 0.1), new ComputeChange<Double>() {

                    public Double sum(Double t1, Double t2) {
                        return t1 + t2;
                    }
                }, cashChange);

                dialog.setVisible(true);

                if (dialog.isSuccess()) {
                    cashChange = dialog.getValueChange();
                }

            }
        });


        add(changeCashButton, cButton);


        cItemLabel.gridy++;
        cItem.gridy++;
        cButton.gridy++;

        JLabel pointsLabel = new JLabel("Punten:");
        add(pointsLabel, cItemLabel);


        pointsField = new JFormattedTextField(NumberFormat.getIntegerInstance());
        if (user.getPoints() != null) {
            pointsField.setValue(user.getPoints());
        }
        pointsField.setEditable(false);
        add(pointsField, cItem);

        cItem.gridx++;

        changePointsButton = new JButton("Verander punten");

        changePointsButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                ValueChangeWithReasonDialog dialog = new ValueChangeWithReasonDialog<Integer>("Punten", user.getPoints(), NumberFormat.getInstance(), new SpinnerNumberModel(0, -10000, 10000, 1), new ComputeChange<Integer>() {

                    public Integer sum(Integer t1, Integer t2) {
                        return t1 + t2;
                    }
                }, pointsChange);

                dialog.setVisible(true);

                if (dialog.isSuccess()) {
                    pointsChange = dialog.getValueChange();
                }

            }
        });

        add(changePointsButton, cButton);

        cItem.gridx--;

        cItem.gridwidth = 1;
        cItem.gridy++;

        saveButton = new JButton("Opslaan");
        saveButton.setActionCommand(SAVE_ACTION);
        saveButton.addActionListener(this);

        add(saveButton, cItem);

        getRootPane().setDefaultButton(saveButton);

        cItem.gridx++;

        cancelButton = new JButton("Annuleren");
        cancelButton.setActionCommand(CANCEL_ACTION);
        cancelButton.addActionListener(this);
        add(cancelButton, cItem);

        pack();


    }

    public User getUser() {
        return user;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(SAVE_ACTION)) {

            user.setNickname(nicknameField.getText());
            if(passwordEdited)
                user.setPassword(new String(passwordField.getPassword()));
            user.setLastname(lastnameField.getText());
            user.setFirstname(firstnameField.getText());
            user.setEmail(emailField.getText());
            if (rrnField.getValue() != null) {
                user.setRijksregisternummer(new Long((String) rrnField.getValue()));
            }
            user.setRole((User.Role) roleField.getSelectedItem());

            try {
                userFactory.makePersistent(user);
                success = true;
            } catch (XmlRpcException ex) {
                logger.error(ex);
                JXErrorPane.showDialog(this, new ErrorInfo("Error while saving changes to user", "An exception occured while saving the changes to user " + user.getId(), null, null, ex, null, null));

            }
        }

        this.setVisible(false);
    }

    public boolean isSuccess() {
        return success;
    }
}
