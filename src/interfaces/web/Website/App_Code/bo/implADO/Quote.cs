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
    private double previous; //Slotkoers van de vorige dag

    //Verhandelde aandelen over bepaalde tijdsperiode
    private int volume;

    private double buy;
    private double sell;

    //Maximum en minimum van de dag
    private double low;
    private double high;


    public Quote(DateTime time, double price, double previous, int volume, double buy, double sell, double low, double high)
    {
        this.time = time;
        this.price = price;
        this.previous = previous;
        this.volume = volume;
        this.buy = buy;
        this.sell = sell;
        this.low = low;
        this.high = high;
    }

    public readonly DateTime Time
    {
        get
        {
            return time;
        }
    }

    public readonly double Price
    {
        get
        {
            return price;
        }
    }

    public readonly double Previous
    {
        get
        {
            return previous;
        }

    }

    public readonly int Volume
    {
        get
        {
            return volume;
        }
    }

    public readonly double Buy
    {
        get
        {
            return buy;
        }
    }

    public readonly double Sell
    {
        get
        {
            return sell;
        }
    }

    public readonly double Low
    {
        get
        {
            return low;
        }
    }

    public readonly double High
    {
        get
        {
            return high;
        }
    }
}
