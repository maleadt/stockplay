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
using CookComputing.XmlRpc;

/// <summary>
/// Summary description for Order
/// </summary>
public class Order : IOrder
{

    private int id;
    private int userId;
    private string isin;
    private int amount;
    private double price;
    private string type;
    private string status;
    private DateTime creationTime;
    private DateTime expirationTime;
    private DateTime executionTime;

    public Order(int id, int userId, string isin, int amount, double price, string type,
                    string status, DateTime creationTime, DateTime expirationTime, DateTime executionTime)
    {
        this.id = id;
        this.userId = userId;
        this.isin = isin;
        this.amount = amount;
        this.price = price;
        this.type = type;
        this.status = status;
        this.creationTime = creationTime;
        this.expirationTime = expirationTime;
        this.executionTime = executionTime;
    }

	public Order(XmlRpcStruct order)
	{
        id = Convert.ToInt32(order["ID"]);
        userId = Convert.ToInt32(order["USER"]);
        isin = (string) order["ISIN"];
        amount = Convert.ToInt32(order["AMOUNT"]);
        price = Convert.ToDouble(order["PRICE"]);
        type = (string)order["TYPE"];
        status = (string)order["STATUS"];
        creationTime = Convert.ToDateTime(order["CREATIONTIME"]);
        expirationTime = Convert.ToDateTime(order["EXPIRATIONTIME"]);
        executionTime = Convert.ToDateTime(order["EXECUTIONTIME"]);
	}

    public XmlRpcStruct toStruct()
    {
        XmlRpcStruct order = new XmlRpcStruct();
        order.Add("USER", userId);
        order.Add("ISIN", isin);
        order.Add("AMOUNT", amount);
        order.Add("PRICE", price);
        order.Add("TYPE", type);
        order.Add("STATUS", status);
        order.Add("CREATIONTIME", creationTime);
        if (expirationTime != DateTime.MinValue) order.Add("EXPIRATIONTIME", expirationTime);
        if (executionTime != DateTime.MinValue) order.Add("EXECUTIONTIME", executionTime);

        return order;
    }

    #region IOrder Members

    public int ID
    {
        get { return id; }
    }

    public int UserID
    {
        get { return userId; }
    }

    public string Isin
    {
        get { return isin; }
    }

    public int Amount
    {
        get { return amount; }
    }

    public double Price
    {
        get { return price; }
    }

    public string Type
    {
        get { return type; }
    }

    public string Status
    {
        get { return status; }
    }

    public DateTime CreationTime
    {
        get { return creationTime; }
    }

    public DateTime ExpirationTime
    {
        get { return expirationTime; }
    }

    public DateTime ExecutionTime
    {
        get { return executionTime; }
    }

    #endregion
}
