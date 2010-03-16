using System;
using System.Data;
using System.Configuration;
using System.Linq;
using System.Web;
using System.Web.Security;
using System.Web.UI;
using System.Web.UI.HtmlControls;
using System.Web.UI.WebControls;
using System.Web.UI.WebControls.WebParts;
using System.Xml.Linq;
using System.Collections.ObjectModel;
using System.Collections.Generic;

/// <summary>
/// Summary description for Security
/// </summary>
public class Security : ISecurity
{

    private string symbol;
    private string name;
    private string type;

    private Exchange exchange;


	public Security(string symbol, string name, string type, Exchange exchange)
	{
        this.symbol = symbol;
        this.name = name;
        this.type = type;
        this.exchange = exchange;
	}

    public Quote GetLatestQuote()
    {
        DataAccess data = DataAccess.GetInstance();
        return data.GetLatestQuoteFromSecurity(symbol);
    }

    public List<Quote> GetQuotes(DateTime begin, DateTime eind, TimeSpan interval)
    {
        return null;
    }

    public string Symbol
    {
        get
        {
            return symbol;
        }
    }

    public string Name
    {
        get
        {
            return name;
        }
    }

    public string Type
    {
        get
        {
            return type;
        }
    }

    public Exchange Exchange
    {
        get
        {
            return exchange;
        }
    }
}
