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
using System.Collections.Generic;
using CookComputing.XmlRpc;
using StockPlay;

namespace StockPlay.implXMLRPC
{

    /// <summary>
    /// Summary description for Security
    /// </summary>
    public class Index : IIndex
    {
        private string isin;
        private string symbol;
        private string name;

        private IExchange exchange;

        public Index(string isin, string symbol, string name, IExchange exchange)
        {
            this.isin = isin;
            this.symbol = symbol;
            this.name = name;
            this.exchange = exchange;
        }

        public Index(XmlRpcStruct index)
        {
            IDataAccess data = DataAccessFactory.GetDataAccess();

            this.isin = (string)index["ISIN"];
            this.symbol = (string)index["SYMBOL"];
            this.name = (string)index["NAME"];
            this.exchange = data.GetExchangeBySymbol((string)index["EXCHANGE"]);
        }

        public XmlRpcStruct toStruct()
        {
            XmlRpcStruct security = new XmlRpcStruct();
            security.Add("ISIN", isin);
            security.Add("SYMBOL", symbol);
            security.Add("NAME", name);
            security.Add("EXCHANGE", exchange.Name);

            return security;
        }

        #region ISecurity Members

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

        #endregion
    }
}