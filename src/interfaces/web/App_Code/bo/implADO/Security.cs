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

namespace implADO
{

    /// <summary>
    /// Summary description for Security
    /// </summary>
    public class Security : ISecurity
    {
        private string isin;
        private string symbol;
        private string name;

        private bool visible;
        private bool suspended;

        private IExchange exchange;
        private IQuote latestQuote;

        public Security(string isin, string symbol, string name, bool visible, bool suspended, IExchange exchange)
        {
            this.isin = isin;
            this.symbol = symbol;
            this.name = name;

            this.visible = visible;
            this.suspended = suspended;

            this.exchange = exchange;
        }

        public Security(string isin, string symbol, string name, bool visible, bool suspended, IExchange exchange, IQuote quote)
            : this(isin, symbol, name, visible, suspended, exchange)
        {
            this.latestQuote = quote;
        }

        public IQuote GetLatestQuote()
        {
            if (latestQuote == null)
            {
                IDataAccess data = DataAccess.GetInstance();
                latestQuote = data.GetLatestQuoteFromSecurity(isin);
                return latestQuote;
            }
            else
            {
                return latestQuote;
            }
        }

        //Haalt de quote die het dichtst bij date ligt (er is geen garantie dat deze quote op dezelfde dag is)
        public IQuote GetQuote(DateTime date)
        {
            IDataAccess data = DataAccess.GetInstance();
            return data.GetQuoteFromSecurity(isin, date);
        }

        //Geeft van iedere dag 1 quote tussen mindate en maxdate
        public List<IQuote> GetDailyQuotes(DateTime minDate, DateTime maxDate)
        {
            IDataAccess data = DataAccess.GetInstance();
            return data.GetDailyQuotesFromSecurity(isin, minDate, maxDate);
        }



        public string Isin
        {
            get
            {
                return isin;
            }
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

        public IExchange Exchange
        {
            get
            {
                return exchange;
            }
        }

        public bool Visible
        {
            get
            {
                return Visible;
            }
        }

        public bool Suspended
        {
            get
            {
                return suspended;
            }
        }
    }
}