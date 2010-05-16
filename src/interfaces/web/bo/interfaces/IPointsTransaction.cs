using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace StockPlay
{
    public interface IPointsTransaction
    {
        int UserID { get; }
        DateTime Timestamp { get; }
        PointsType Type { get; }
        int Delta { get; }
        string Comments { get; }
    }

    public enum PointsType
    {
        PROFIT,
        CASH,
        BEL20,
        MANUAL
    }
}
