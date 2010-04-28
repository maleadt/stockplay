using System;
namespace StockPlay {
	public interface IExchange
	{
	    string Symbol { get; }
	    string Name { get; }
	    string Location { get; }
	}
}