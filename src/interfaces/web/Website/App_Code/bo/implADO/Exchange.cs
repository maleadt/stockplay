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

/// <summary>
/// Summary description for Exchange
/// </summary>
public class Exchange : IExchange
{

    private string symbol, name, location;

	public Exchange(string symbol, string name, string location)
	{
		this.symbol = symbol;
        this.name = name;
        this.location = location;
	}

    #region IExchange Members

    public string Symbol
    {
        get { return symbol; }
    }

    public string Name
    {
        get { return name; }
    }

    public string Location
    {
        get { return location; }
    }

    #endregion
}
