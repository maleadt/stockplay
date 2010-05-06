package com.kapti.pointsmanager.util;

import com.kapti.client.finance.FinanceFactory;
import com.kapti.client.finance.Quote;
import com.kapti.client.user.PortfolioFactory;
import com.kapti.client.user.Transaction;
import com.kapti.client.user.Transaction.Type;
import com.kapti.client.user.TransactionFactory;
import com.kapti.client.user.User;
import com.kapti.client.user.UserSecurity;
import com.kapti.exceptions.StockPlayException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Hulpklasse om de berekening van winst en winstpercentage te berekenen voor
 * een gebruiker.
 *
 * @author Dieter
 */
public class Profit {
    
    private double profit = 0;
    private double profitPercentage = 0;

    private int amount = 0; //Hoeveelheid aandelen (om gewicht van effecten in rekening te brengen bij percentageberekening)

    private User user;

    public Profit(User user) throws StockPlayException {
        this.user = user;

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

        //Winst berekenen van de koop/verkooporders die bij elkaar horen
        koopVerkoopWinst(buyTransactions, sellTransactions);

        //Resterende kooporders (die aangekocht zijn in de loop van de dag) berekenen
        koopWinst(buyTransactions, portfolioMap);

        //Resterende verkooporders (die verkocht zijn in de loop van de dag) berekenen
        verkoopWinst(sellTransactions);

        //Winst op bestaande portfolio berekenen
        portfolioWinst(portfolioMap);
    }

    /*
     * GETTERS
     */

    public User getUser() {
        return user;
    }

    public double getProfit() {
        return profit;
    }

    public double getProfitPercentage() {
        return profitPercentage;
    }

    /*
     * WINSTBERKENINGEN
     */

    private void koopVerkoopWinst(HashMap<Integer, Transaction> buyTransactions,
                                    HashMap<Integer, Transaction> sellTransactions) {

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

                        profit += (sell.getPrice() - buy.getPrice()) * sell.getAmount();
                        
                        //Winstpercentage herrekenen (rekening houden met gewichten van de aandelen)
                        profitPercentage = ( amount * profitPercentage + sell.getAmount() * (sell.getPrice()-buy.getPrice())/buy.getPrice() )
                                                            / ( amount + sell.getAmount() );

                        amount += sell.getAmount();

                        buy.setAmount( buy.getAmount() - sell.getAmount() );
                        sell.setAmount(0);
                    }
                    //Anders zetten we BUY op 0 en herrekenen SELL
                    else {
                        //Winst bijtellen
                        profit += (sell.getPrice() - buy.getPrice()) * (sell.getAmount() - buy.getAmount());

                        profitPercentage = ( amount * profitPercentage + sell.getAmount() * (sell.getPrice()-buy.getPrice())/buy.getPrice() )
                                                            / ( amount + (sell.getAmount() - buy.getAmount()) );
                        
                        amount += sell.getAmount() - buy.getAmount();

                        sell.setAmount( sell.getAmount() - buy.getAmount() );
                        buy.setAmount(0);
                    }
                }
            }

            //Sell transacties met amount 0 worden verwijderd
            Iterator<Integer> sellIterator = sellTransactions.keySet().iterator();

            while(sellIterator.hasNext()) {
                int sellId = sellIterator.next();

                if(sellTransactions.get(sellId).getAmount() == 0)
                    sellIterator.remove();
            }
        }

        //Buy transactions die leeg zijn wegsmijten
        Iterator<Integer> buyIterator = sellTransactions.keySet().iterator();

        while(buyIterator.hasNext()) {
            int buyId = buyIterator.next();

            if(buyTransactions.get(buyId).getAmount() == 0)
                buyIterator.remove();
        }
    }

    private void koopWinst(HashMap<Integer, Transaction> buyTransactions, HashMap<String, UserSecurity> portfolio) throws StockPlayException {

        for(Integer buyTransactionId : buyTransactions.keySet()) {
            Transaction transaction = buyTransactions.get(buyTransactionId);

            Quote latestQuote = FinanceFactory.getInstance().getLatestQuoteFromSecurity(transaction.getSecurity());

            //Portfolio herrekenen zodat aangekocht securities in deze periode niet dubbel gerekend worden
            if(portfolio.containsKey(transaction.getSecurity().getISIN())) {
                UserSecurity userSecurity = portfolio.get(transaction.getSecurity().getISIN());
                userSecurity.setAmount( userSecurity.getAmount() - transaction.getAmount() );
            }

            profit += (latestQuote.getPrice() - transaction.getPrice()) * transaction.getAmount();

            profitPercentage = ( amount * profitPercentage + transaction.getAmount() * (latestQuote.getPrice()-transaction.getPrice())/transaction.getPrice()  )
                                                    / ( amount + transaction.getAmount() );

            amount += transaction.getAmount();
        }
    }

    private void verkoopWinst(HashMap<Integer, Transaction> sellTransactions) throws StockPlayException {

        for(Integer sellTransactionId : sellTransactions.keySet()) {
            Transaction transaction = sellTransactions.get(sellTransactionId);

            Quote latestQuote = FinanceFactory.getInstance().getLatestQuoteFromSecurity(transaction.getSecurity());

            profit += (transaction.getPrice() - latestQuote.getOpen()) * transaction.getAmount();

            profitPercentage = ( amount * profitPercentage + transaction.getAmount() * (transaction.getPrice()-latestQuote.getPrice())/transaction.getPrice()  )
                                                    / ( amount + transaction.getAmount() );

            amount += transaction.getAmount();
        }
    }

    private void portfolioWinst(HashMap<String, UserSecurity> portfolio) throws StockPlayException {

        for(String isin : portfolio.keySet()) {
            UserSecurity userSecurity = portfolio.get(isin);

            Quote latestQuote = FinanceFactory.getInstance().getLatestQuoteFromSecurity(userSecurity.getSecurity());

            profit += (latestQuote.getPrice() - latestQuote.getOpen()) * userSecurity.getAmount();

            profitPercentage = ( amount * profitPercentage + userSecurity.getAmount() * (latestQuote.getPrice() - latestQuote.getOpen())/latestQuote.getOpen()  )
                                                / ( amount + userSecurity.getAmount() );

            amount += userSecurity.getAmount();
        }
    }

    /*
     * CONVERSIE
     */

    private HashMap<Integer, Transaction> getTransactionsMap(Collection<Transaction> transactions, Type type) {

        HashMap<Integer, Transaction> transactionsMap = new HashMap<Integer, Transaction>();
        Iterator<Transaction> iterator = transactions.iterator();

        while(iterator.hasNext()) {
            Transaction transaction = iterator.next();
            if(transaction.getType() == type)
            transactionsMap.put(transaction.getId(), transaction);
        }

        return transactionsMap;
    }

    private HashMap<String, UserSecurity> getPortfolioMap(Collection<UserSecurity> portfolio) {

        HashMap<String, UserSecurity> portfolioMap = new HashMap<String, UserSecurity>();
        Iterator<UserSecurity> iterator = portfolio.iterator();

        while(iterator.hasNext()) {
            UserSecurity userSecurity = iterator.next();
            portfolioMap.put(userSecurity.getSecurity().getISIN(), userSecurity);
        }

        return portfolioMap;
    }
}
