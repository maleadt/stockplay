using System;
using System.Collections.Generic;
namespace StockPlay {
	public interface ISecurity
	{
	    IExchange Exchange { get; }
	    IQuote GetLatestQuote();
	    IQuote GetQuote(DateTime date);
	    List<IQuote> GetDailyQuotes(DateTime minDate, DateTime maxDate);
	    string Isin { get; }
	    string Name { get; }
	    string Symbol { get; }
	    bool Visible { get; }
	    bool Suspended { get; }
	}
}