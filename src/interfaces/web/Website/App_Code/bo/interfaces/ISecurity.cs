using System;
using System.Collections.Generic;
interface ISecurity
{
    Exchange Exchange { get; }
    Quote getLatestQuote();
    List<Quote> getQuotes(DateTime begin, DateTime eind, TimeSpan interval);
    string Name { get; }
    string Symbol { get; }
    string Type { get; }
}
