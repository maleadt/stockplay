using System;
using System.Collections.ObjectModel;
using System.Collections.Generic;
interface IDataAccess
{
    //Securities
    List<Security> GetSecuritiesList();
    Security GetSecurityBySymbol(string symbol);
    List<Security> GetSecuritiesFromExchange(string id);

    //Quotes
    Quote GetQuoteFromSecurity(string symbol, DateTime time);
    List<Quote> GetQuotesFromSecurity(string symbol);
    List<Quote> GetQuotesIntervalFromSecurity(string symbol, DateTime start, DateTime end);

    //Exhanges
    Exchange getExchangeById(string id);
    List<Exchange> getExchanges();
}
