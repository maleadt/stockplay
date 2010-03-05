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
/// Summary description for Quote
/// </summary>
public class Quote : IQuote
{
    private DateTime time;
    private double price;
    private double open; //Slotkoers van de vorige dag = 

    //Verhandelde aandelen over bepaalde tijdsperiode
    private int volume;

    private double buy;
    private double sell;

    //Maximum en minimum van de dag
    private double low;
    private double high;


    public Quote(DateTime time, double price, double open, int volume, double buy, double sell, double low, double high)
    {
        this.time = time;
        this.price = price;
        this.open = open;
        this.volume = volume;
        this.buy = buy;
        this.sell = sell;
        this.low = low;
        this.high = high;
    }

    public DateTime Time
    {
        get
        {
            return time;
        }
    }

    public double Price
    {
        get
        {
            return price;
        }
    }

    public double Open
    {
        get
        {
            return open;
        }

    }

    public int Volume
    {
        get
        {
            return volume;
        }
    }

    public double Buy
    {
        get
        {
            return buy;
        }
    }

    public double Sell
    {
        get
        {
            return sell;
        }
    }

    public double Low
    {
        get
        {
            return low;
        }
    }

    public double High
    {
        get
        {
            return high;
        }
    }
}
