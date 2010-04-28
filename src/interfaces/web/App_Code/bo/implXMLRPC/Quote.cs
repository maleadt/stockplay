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
using StockPlay;

namespace StockPlay.implXMLRPC
{

    /// <summary>
    /// Summary description for Quote
    /// </summary>
    public class Quote : IQuote
    {
        private DateTime time;
        private string isin;

        private double price;
        private double open; //Slotkoers van de vorige dag

        //Verhandelde aandelen over bepaalde tijdsperiode
        private int volume;

        private double ask;
        private double bid;

        //Maximum en minimum van de dag
        private double low;
        private double high;

        public Quote(XmlRpcStruct quote)
        {
            this.time = (DateTime)quote["TIME"];
            this.isin = (String)quote["ISIN"];
            this.price = Convert.ToDouble(quote["PRICE"]);
            this.open = Convert.ToDouble(quote["OPEN"]);
            this.volume = Convert.ToInt32(quote["VOLUME"]);
            this.ask = Convert.ToDouble(quote["ASK"]);
            this.bid = Convert.ToDouble(quote["BID"]);
            this.low = Convert.ToDouble(quote["LOW"]);
            this.high = Convert.ToDouble(quote["HIGH"]);
        }

        public XmlRpcStruct toStruct()
        {
            XmlRpcStruct quote = new XmlRpcStruct();
            quote.Add("TIME", time);
            quote.Add("ISIN", isin);
            quote.Add("PRICE", price);
            quote.Add("OPEN", open);
            quote.Add("VOLUME", volume);
            quote.Add("ASK", ask);
            quote.Add("BID", bid);
            quote.Add("LOW", low);
            quote.Add("HIGH", high);

            return quote;
        }

        #region IQuote Members

        public double Change
        {
            get
            {
                return Math.Round((price - open) / price * 100, 2);
            }
            
        }

        public double Ask
        {
            get { return ask; }
        }

        public double High
        {
            get { return high; }
        }

        public double Low
        {
            get { return low; }
        }

        public double Open
        {
            get { return open; }
        }

        public double Price
        {
            get { return price; }
        }

        public double Bid
        {
            get { return bid; }
        }

        public DateTime Time
        {
            get { return time; }
        }

        public int Volume
        {
            get { return volume; }
        }

        public String Isin
        {
            get { return isin; }
        }

        #endregion
    }
}
