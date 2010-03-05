using System;
interface IQuote
{
    double Buy { get; }
    double High { get; }
    double Low { get; }
    double Previous { get; }
    double Price { get; }
    double Sell { get; }
    DateTime Time { get; }
    int Volume { get; }
}
