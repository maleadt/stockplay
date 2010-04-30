/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.administration.actions;

import com.kapti.administration.MainFrame;
import com.kapti.client.finance.Exchange;
import com.kapti.client.finance.FinanceFactory;
import com.kapti.exceptions.StockPlayException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;
import javax.swing.Action;
import javax.swing.ImageIcon;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

/**
 *
 * @author Thijs
 */
public class ActionsFactory {

    private final ResourceBundle translations = ResourceBundle.getBundle("com/kapti/administration/translations");
    private static ActionsFactory instance = new ActionsFactory();
    private static FinanceFactory financeFactory = FinanceFactory.getInstance();

    private ActionsFactory() {
    }

    public static ActionsFactory getInstance() {
        return instance;
    }
    Collection<Action> statusActions = null;
    Collection<Action> securitiesActions = null;
    Collection<Action> usersActions = null;

    public Collection<Action> getStatusActions() {

        if (statusActions == null) {
            statusActions = new ArrayList<Action>();

            Action firstTitle = new ShowStatusAction(translations.getString("COMPONENTSMENU"), createImageIcon("server.png"), true);
            firstTitle.putValue(Layout.LEAVE_SPACE, false);

            statusActions.add(firstTitle);
            statusActions.add(new ShowStatusAction(translations.getString("SCRAPERMENUITEM"), createImageIcon("world.png")));
            statusActions.add(new ShowStatusAction(translations.getString("DATABASEMENUITEM"), createImageIcon("database.png")));
            statusActions.add(new ShowStatusAction(translations.getString("WEBSERVER"), createImageIcon("server.png")));

            statusActions.add(new ShowStatusAction(translations.getString("STATUSMENU"), createImageIcon("server.png"), true));


        }
        return statusActions;
    }

    public Collection<Action> getSecuritiesActions() {


        if (securitiesActions == null) {

            securitiesActions = new ArrayList<Action>();
            Action firstTitle = new ShowSecuritiesAction(translations.getString("EXCHANGESMENUITEM"), createImageIcon("money.png"), true);
            firstTitle.putValue(Layout.LEAVE_SPACE, false);
            securitiesActions.add(firstTitle);
            Collection<Exchange> exchanges = null;
            try {
                exchanges = financeFactory.getAllExchanges();
            } catch (StockPlayException ex) {
                JXErrorPane.showDialog(MainFrame.getInstance(), new ErrorInfo(translations.getString("ERROR_COMMUNICATION"), translations.getString("ERROR_FETCH_EXCHANGES"), null, null, ex, null, null));
            }

            for (Exchange exch : exchanges) {

                securitiesActions.add(new ShowSecuritiesByExchangeAction(exch.getName(), createImageIcon("money_euro.png"), exch));
            }



            securitiesActions.add(new ShowSecuritiesAction(translations.getString("EXCHANGESMENUITEM"), createImageIcon("money.png"), true));

        }

        return securitiesActions;
    }

    public Collection<Action> getUsersActions() {

        if (usersActions == null) {
            usersActions = new ArrayList<Action>();

            Action firstTitle = new ShowAllUsersAction(translations.getString("OVERVIEWMENUITEM"), createImageIcon("folder_user.png"), true);
            firstTitle.putValue(Layout.LEAVE_SPACE, false);

            usersActions.add(firstTitle);

            usersActions.add(new ShowAllUsersAction(translations.getString("SEARCH_ALFABETHICALLYMENUITEM"), createImageIcon("folder_user.png"), true));

            usersActions.add(new ShowUsersByLettersAction(translations.getString("A-E_MENUITEM"), createImageIcon("group.png"), 'a', 'e'));
            usersActions.add(new ShowUsersByLettersAction(translations.getString("F-J_MENUITEM"), createImageIcon("group.png"), 'f', 'j'));
            usersActions.add(new ShowUsersByLettersAction(translations.getString("K-O_MENUITEM"), createImageIcon("group.png"), 'k', 'o'));
            usersActions.add(new ShowUsersByLettersAction(translations.getString("P-S_MENUITEM"), createImageIcon("group.png"), 'p', 's'));
            usersActions.add(new ShowUsersByLettersAction(translations.getString("T-Z_MENUITEM"), createImageIcon("group.png"), 't', 'z'));

            usersActions.add(new ShowAllUsersAction(translations.getString("SEARCH_BY_REGDATE_MENUITEM"), createImageIcon("folder_user.png"), true));

            usersActions.add(new ShowUsersByRegdateAction(translations.getString("TODAYMENUITEM"), createImageIcon("group.png"), 1000 * 60 * 60 * 24));
            usersActions.add(new ShowUsersByRegdateAction(translations.getString("PASTWEEKMENUITEM"), createImageIcon("group.png"), 1000 * 60 * 60 * 24 * 7));
            usersActions.add(new ShowUsersByRegdateAction(translations.getString("PASTMONTHITEM"), createImageIcon("group.png"), 1000 * 60 * 60 * 24 * 31));
            usersActions.add(new ShowUsersByRegdateAction(translations.getString("PASTYEARITEM"), createImageIcon("group.png"), 1000 * 60 * 60 * 24 * 365));




        }

        return usersActions;
    }

    protected ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = getClass().getResource("/com/kapti/administration/images/" + path);

        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println(translations.getString("ERROR_FILE_UNFINDABLE") + path);
            return null;
        }
    }
}
