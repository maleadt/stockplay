using System;
namespace StockPlay
{
    public interface IRank
    {
        int UserID { get; }
        int Total { get; }
        int RankNumber { get; }
    }
}