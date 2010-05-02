package com.kapti.pointsmanager;

import com.kapti.client.XmlRpcClientFactory;
import com.kapti.client.finance.FinanceFactory;
import com.kapti.client.finance.Quote;
import com.kapti.client.user.PortfolioFactory;
import com.kapti.client.user.Transaction;
import com.kapti.client.user.Transaction.Type;
import com.kapti.client.user.TransactionFactory;
import com.kapti.client.user.User;
import com.kapti.client.user.UserFactory;
import com.kapti.client.user.UserSecurity;
import com.kapti.exceptions.StockPlayException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;

/**
 *
 * @author Dieter
 */
public class Main {

    private static int userId = 200; //testuser Dieter

    public static void main(String[] args) throws StockPlayException, XmlRpcException {

        XmlRpcClient client = XmlRpcClientFactory.getXmlRpcClient();

        User user = UserFactory.getInstance().getUserById(userId);
        
        //Vanaf het huidig tijdstip...
        GregorianCalendar eindTijd = new GregorianCalendar();
        eindTijd.setTime(new Date());

        //... tot 24 uur geleden
        GregorianCalendar startTijd = new GregorianCalendar();
        startTijd.setTime(new Date());
        startTijd.add(Calendar.DATE, -1);

        SimpleDateFormat formaat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");


        //Portfolio en transactie van vandaag ophalen
        Collection<UserSecurity> portfolio = PortfolioFactory.getInstance().getPortfolioByUser(user);
        Collection<Transaction> transactions = TransactionFactory.getInstance().getTransactionByFilter(  "USERID == '" +
                                                                                                        user.getId() +
                                                                                                        "' && TIMEST <= '" +
                                                                                                        formaat.format(eindTijd.getTime()) +
                                                                                                        "'d && TIMEST >= '" +
                                                                                                        formaat.format(startTijd.getTime()) +
                                                                                                        "'d");

        //Collectie omzetten naar twee HashMaps met de BUY/SELL transacties
        HashMap<Integer, Transaction> buyTransactions = getTransactionsMap(transactions, Type.BUY);
        HashMap<Integer, Transaction> sellTransactions = getTransactionsMap(transactions, Type.SELL);

        //Portfolio zetten we ook om makkelijk te kunnen zoeken op ISIN
        HashMap<String, UserSecurity> portfolioMap = getPortfolioMap(portfolio);
        
        double totaleWinst = 0;

        //Winst berekenen van de koop/verkooporders die bij elkaar horen
        totaleWinst += koopVerkoopWinst(buyTransactions, sellTransactions);

        //Resterende kooporders (die aangekocht zijn in de loop van de dag) berekenen
        totaleWinst += koopWinst(buyTransactions, portfolioMap);

        //Resterende verkooporders (die verkocht zijn in de loop van de dag) berekenen
        totaleWinst += verkoopWinst(sellTransactions);

        //Winst op bestaande portfolio berekenen
        totaleWinst += portfolioWinst(portfolioMap);

        System.out.println(totaleWinst);
    }
    
    static double koopVerkoopWinst(HashMap<Integer, Transaction> buyTransactions,
                                   HashMap<Integer, Transaction> sellTransactions) {
        double winst = 0;

        //Buytransactions overlopen en kijken welke hiervan al reeds verkocht zijn
        //Van deze berekenen we de winst die in deze periode is gemaakt
        for(Integer buyTransactionId : buyTransactions.keySet()) {
            Transaction buy = buyTransactions.get(buyTransactionId);
            
            //Alle SELL transacties overlopen
            for(Integer sellTransactionId : sellTransactions.keySet()) {
                Transaction sell = sellTransactions.get(sellTransactionId);

                //Controleren ofdat het over dezelfde ISIN gaat en ofdat SELL wel na de BUY kwam
                if(sell.getSecurity().getISIN().equals(buy.getSecurity().getISIN())
                   && sell.getTime().after(buy.getTime())) {

                    //Als we minder of evenveel kochten als verkochten, dan zetten we SELL op 0
                    if(sell.getAmount() <= buy.getAmount()) {
                        winst += (sell.getPrice() - buy.getPrice()) * sell.getAmount();

                        buy.setAmount( buy.getAmount() - sell.getAmount() );
                        sell.setAmount(0);
                    }
                    //Anders zetten we BUY op 0 en herrekenen SELL
                    else {
                        winst += (sell.getPrice() - buy.getPrice()) * (sell.getAmount() - buy.getAmount());

                        sell.setAmount( sell.getAmount() - buy.getAmount() );
                        buy.setAmount(0);
                    }
                }
            }

            //Sell transacties met amount 0 worden verwijderd
            for(Integer sellTransactionId : sellTransactions.keySet()) {
                Transaction sell = sellTransactions.get(sellTransactionId);

                if(sell.getAmount() == 0)
                    sellTransactions.remove(sell.getId());
            }
        }
        
        //Buy transactions die leeg zijn wegsmijten
        for(Integer buyTransactionId : buyTransactions.keySet()) {
            Transaction buy = buyTransactions.get(buyTransactionId);

            if(buy.getAmount() == 0)
                buyTransactions.remove(buy.getId());
        }
        
        return winst;
    }

    static double koopWinst(HashMap<Integer, Transaction> buyTransactions, HashMap<String, UserSecurity> portfolio) throws StockPlayException {
        double winst = 0;

        for(Integer buyTransactionId : buyTransactions.keySet()) {
            Transaction transaction = buyTransactions.get(buyTransactionId);

            Quote latestQuote = FinanceFactory.getInstance().getLatestQuoteFromSecurity(transaction.getSecurity());

            //Portfolio herrekenen zodat aangekocht securities in deze periode niet dubbel gerekend worden
            if(portfolio.containsKey(transaction.getSecurity().getISIN())) {
                UserSecurity userSecurity = portfolio.get(transaction.getSecurity().getISIN());
                userSecurity.setAmount( userSecurity.getAmount() - transaction.getAmount() );
            }

            winst += (latestQuote.getPrice() - transaction.getPrice()) * transaction.getAmount();
        }

        return winst;
    }

    static double verkoopWinst(HashMap<Integer, Transaction> sellTransactions) throws StockPlayException {
        double winst = 0;

        for(Integer sellTransactionId : sellTransactions.keySet()) {
            Transaction transaction = sellTransactions.get(sellTransactionId);

            Quote latestQuote = FinanceFactory.getInstance().getLatestQuoteFromSecurity(transaction.getSecurity());

            winst += (transaction.getPrice() - latestQuote.getOpen()) * transaction.getAmount();
        }

        return winst;
    }

    static double portfolioWinst(HashMap<String, UserSecurity> portfolio) throws StockPlayException {
        double winst = 0;

        for(String isin : portfolio.keySet()) {
            UserSecurity userSecurity = portfolio.get(isin);

            Quote latestQuote = FinanceFactory.getInstance().getLatestQuoteFromSecurity(userSecurity.getSecurity());

            winst += (latestQuote.getPrice() - latestQuote.getOpen()) * userSecurity.getAmount();
        }

        return winst;
    }

    private static HashMap<Integer, Transaction> getTransactionsMap(Collection<Transaction> transactions, Type type) {

        HashMap<Integer, Transaction> transactionsMap = new HashMap<Integer, Transaction>();
        Iterator<Transaction> iterator = transactions.iterator();

        while(iterator.hasNext()) {
            Transaction transaction = iterator.next();
            if(transaction.getType() == type)
            transactionsMap.put(transaction.getId(), transaction);
        }

        return transactionsMap;
    }

    private static HashMap<String, UserSecurity> getPortfolioMap(Collection<UserSecurity> portfolio) {

        HashMap<String, UserSecurity> portfolioMap = new HashMap<String, UserSecurity>();
        Iterator<UserSecurity> iterator = portfolio.iterator();

        while(iterator.hasNext()) {
            UserSecurity userSecurity = iterator.next();
            portfolioMap.put(userSecurity.getSecurity().getISIN(), userSecurity);
        }

        return portfolioMap;
    }

}
