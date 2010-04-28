using System;
using System.Collections.Generic;

namespace StockPlay {
	public interface IUser
	{
	    int ID { get; }
	    string Nickname { get; set; }
	    string Password { get; set; }
	    string Email { get; set;  }
	    string Lastname { get; set; }
	    string Firstname { get; set;  }
	    DateTime Regdate { get; }
	    bool IsAdmin { get; }
	    int Points { get; }
	    double StartAmount { get; }
	    double Cash { get; }
	    long RRN { get; set; }
	}
}