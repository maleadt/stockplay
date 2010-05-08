using System;
namespace StockPlay {
	public interface IOrder
	{
	    int ID { get; }
	    int UserID { get; }
	    string Isin { get; }
	    int Amount { get; }
	    double Price { get; }
        double SecondairyLimit { get; }
        string Type { get; }
	    string Status { get; }
	    DateTime CreationTime { get; }
	    DateTime ExpirationTime { get; }
	    DateTime ExecutionTime { get; }
    }
}