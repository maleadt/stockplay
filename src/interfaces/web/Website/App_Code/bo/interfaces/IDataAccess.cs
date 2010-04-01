using System;
using System.Collections.ObjectModel;
using System.Collections.Generic;
public interface IDataAccess
{
    //Securities
    List<Security> GetSecuritiesList();
    Security GetSecurityBySymbol(string isin);
    List<Security> GetSecuritiesFromExchange(string id);

    //Quotes
    Quote GetLatestQuoteFromSecurity(string isin);
    Quote GetQuoteFromSecurity(string isin, DateTime time);


    //Exhanges
    Exchange getExchangeBySymbol(string isin);
    List<Exchange> getExchanges();
}
