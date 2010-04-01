using System;
using System.Collections.Generic;
interface ISecurity
{
    Exchange Exchange { get; }
    Quote GetLatestQuote();
    Quote GetQuote(DateTime date);
    string Isin { get; }
    string Name { get; }
    string Symbol { get; }
    string Type { get; }
}
