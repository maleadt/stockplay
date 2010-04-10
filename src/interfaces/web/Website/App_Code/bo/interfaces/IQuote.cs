using System;
public interface IQuote
{
    double Change { get; }

    double Ask { get; }
    double High { get; }
    double Low { get; }
    double Open { get; }
    double Price { get; }
    double Bid { get; }
    DateTime Time { get; }
    int Volume { get; }
}
