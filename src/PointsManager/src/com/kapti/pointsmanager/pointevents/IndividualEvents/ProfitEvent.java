package com.kapti.pointsmanager.pointevents.IndividualEvents;

import com.kapti.client.finance.FinanceFactory;
import com.kapti.client.finance.Index;
import com.kapti.client.finance.Quote;
import com.kapti.client.user.PointsType;
import com.kapti.client.user.User;
import com.kapti.exceptions.StockPlayException;
import com.kapti.pointsmanager.util.Profit;
import java.text.DecimalFormat;
import org.apache.log4j.Logger;

/**
 * De speler krijgt punten indien zijn portfolio beter presteert dan de BEL-20.
 * Per procent winst over de BEL-20 krijgt hij MULTIPLIER punten erbij.
 *
 * @author Dieter
 */
public class ProfitEvent implements IIndividualEvent {

    private static Logger logger = Logger.getLogger(ProfitEvent.class);

    private static String BEL20 = "BE0389555039";
    private static String DESCRIPTION = "Beating the BEL-20";

    private static int MULTIPLIER = 10;

    private double percentage; //Percentage van laatste gebruiker

    public PointsType getType() {
        return PointsType.PROFIT;
    }

    public String getDescription() {
        DecimalFormat df = new DecimalFormat("#.##");
        return DESCRIPTION + " (" + df.format(percentage*100) + "%)";
    }

    public int getPoints(User user) {
        try {
            Profit profit = new Profit(user);

            Index bel20 = FinanceFactory.getInstance().getIndexById(BEL20);
            Quote latestQuote = FinanceFactory.getInstance().getLatestQuoteFromIndex(bel20);

            double profitPercentageBel20 = (latestQuote.getPrice() - latestQuote.getOpen())/latestQuote.getOpen();
            double profitPercentageUser = profit.getProfitPercentage();

            percentage = profitPercentageUser-profitPercentageBel20;

            if(profit.getProfit()>0 && profitPercentageUser > profitPercentageBel20) {
                return (int) ((profitPercentageUser - profitPercentageBel20) * 100 * MULTIPLIER);
            }
            else
                return 0;

        } catch (StockPlayException ex) {
            logger.error("Error when trying to calculate profit for user " + user.getId(), ex);

            return 0;
        }
    }
}
