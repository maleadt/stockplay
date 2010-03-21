using System;
using System.Collections.ObjectModel;
using System.Collections.Generic;
public interface IDataAccess
{
    //Securities
    List<Security> GetSecuritiesList();
    Security GetSecurityBySymbol(string symbol);
    List<Security> GetSecuritiesFromExchange(string id);

    //Quotes
    Quote GetLatestQuoteFromSecurity(string symbol);
    Quote GetQuoteFromSecurity(string symbol, DateTime time);


    //Exhanges
    Exchange getExchangeBySymbol(string symbol);
    List<Exchange> getExchanges();
}
