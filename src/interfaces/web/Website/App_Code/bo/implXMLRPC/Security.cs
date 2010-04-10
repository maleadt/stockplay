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
using System.Collections.Generic;
using CookComputing.XmlRpc;

namespace implXMLRPC
{

    /// <summary>
    /// Summary description for Security
    /// </summary>
    public class Security : ISecurity
    {

        private string isin;
        private string symbol;
        private string name;

        private IExchange exchange;
        private IQuote latestQuote;

        private bool visible;
        private bool suspended;

        private bool quoteUnavailable; //Houdt bij ofdat de quote beschikbaar was, om te voorkomen dat nieuwe opvragingen niet nogmaals een query versturen

        public Security(string isin, string symbol, string name, string type, bool visible, bool suspended, IExchange exchange)
        {
            this.isin = isin;
            this.symbol = symbol;
            this.name = name;
            this.exchange = exchange;
            this.visible = visible;
            this.suspended = suspended;

            this.quoteUnavailable = false;
        }

        public Security(XmlRpcStruct security)
        {
            IDataAccess data = DataAccessFactory.GetDataAccess();

            this.isin = (string)security["ISIN"];
            this.symbol = (string)security["SYMBOL"];
            this.name = (string)security["NAME"];
            this.exchange = data.GetExchangeBySymbol((string)security["EXCHANGE"]);
            this.visible = (Convert.ToInt32(security["VISIBLE"]) == 1) ? true : false;
            this.suspended = (Convert.ToInt32(security["SUSPENDED"]) == 1) ? true : false;

            this.quoteUnavailable = false;
        }

        public Security(XmlRpcStruct security, XmlRpcStruct quote) : this(security)
        {
            if (quote == null)
                this.quoteUnavailable = true;
            else
                this.latestQuote = new Quote(quote);
        }

        public XmlRpcStruct toStruct()
        {
            XmlRpcStruct security = new XmlRpcStruct();
            security.Add("ISIN", isin);
            security.Add("SYMBOL", symbol);
            security.Add("NAME", name);
            security.Add("EXCHANGE", exchange.Name);
            security.Add("VISIBLE", visible == true ? 1 : 0);
            security.Add("SUSPENDED", suspended == true ? 1 : 0);

            return security;
        }

        #region ISecurity Members

        public IQuote GetLatestQuote()
        {
            if (latestQuote == null && quoteUnavailable == false)
            {
                IDataAccess data = DataAccessFactory.GetDataAccess();
                latestQuote = data.GetLatestQuoteFromSecurity(isin);
            }
                
            return latestQuote;
        }

        public List<IQuote> GetDailyQuotes(DateTime minDate, DateTime maxDate)
        {
            return new List<IQuote>();
        }

        public IQuote GetQuote(DateTime date)
        {
            throw new NotImplementedException();
        }

        public string Isin
        {
            get { return isin; }
        }

        public string Name
        {
            get { return name; }
        }

        public string Symbol
        {
            get { return symbol; }
        }

        public IExchange Exchange
        {
            get { return exchange; }
        }

        public bool Visible
        {
            get { return visible; }
        }

        public bool Suspended
        {
            get { return suspended; }
        }

        #endregion
    }
}
