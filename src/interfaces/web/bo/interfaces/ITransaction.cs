using System;
using System.Collections.Generic;

namespace StockPlay {
	public interface ITransaction
	{
	    int ID { get; }
	    int UserID { get; }
	    DateTime Timestamp { get; }
	    string Isin { get; }
	    string Type { get; }
	    int Amount { get; }
	    double Price { get; }
	}
}