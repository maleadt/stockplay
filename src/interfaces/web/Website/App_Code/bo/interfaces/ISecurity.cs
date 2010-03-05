using System;
interface ISecurity
{
    Exchange Exchange { get; }
    Quote getLatestQuote();
    System.Collections.ObjectModel.Collection<Quote> getQuotes(DateTime begin, DateTime eind, TimeSpan interval);
    string Name { get; }
    string Symbol { get; }
    string Type { get; }
}
