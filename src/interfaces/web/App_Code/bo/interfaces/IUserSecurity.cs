using System;
using System.Collections.Generic;

namespace StockPlay {
	public interface IUserSecurity
	{
	    int Amount { get; }
	    string Isin { get; }
	    int UserID { get; }
	}
}