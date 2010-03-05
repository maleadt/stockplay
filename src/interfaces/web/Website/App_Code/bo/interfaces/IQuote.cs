using System;
interface IQuote
{
    double Buy { get; }
    double High { get; }
    double Low { get; }
    double Open { get; }
    double Price { get; }
    double Sell { get; }
    DateTime Time { get; }
    int Volume { get; }
}
