using System;
using System.Collections.Generic;
namespace StockPlay
{
    public interface IIndex
    {
        IExchange Exchange { get; }
        string Isin { get; }
        string Name { get; }
        string Symbol { get; }
    }
}