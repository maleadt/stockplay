package com.kapti.pointsmanager;

import com.kapti.client.XmlRpcClientFactory;
import com.kapti.client.finance.FinanceFactory;
import com.kapti.client.finance.Quote;
import com.kapti.client.finance.Security;
import com.kapti.client.user.PortfolioFactory;
import com.kapti.client.user.Transaction;
import com.kapti.client.user.Transaction.Type;
import com.kapti.client.user.TransactionFactory;
import com.kapti.client.user.User;
import com.kapti.client.user.UserFactory;
import com.kapti.client.user.UserSecurity;
import com.kapti.exceptions.StockPlayException;
import com.kapti.pointsmanager.pointevents.ProfitEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.log4j.Logger;

/**
 *
 * @author Dieter
 */
public class Main {

    public static void main(String[] args) throws StockPlayException, XmlRpcException {

        Collection<User> users = UserFactory.getInstance().getAllUsers();

        Iterator<User> userIterator = users.iterator();

        while(userIterator.hasNext()) {
            User user = userIterator.next();

            ProfitEvent profitEvent = new ProfitEvent();

            user.setPoints( user.getPoints() + profitEvent.getPoints(user) );

            UserFactory.getInstance().makePersistent(user);
        }

    }
}
