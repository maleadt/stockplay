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
using CookComputing.XmlRpc;
using StockPlay;

namespace StockPlay.implXMLRPC
{

    /// <summary>
    /// Summary description for Exchange
    /// </summary>
    public class Exchange : IExchange
    {
        private string symbol;
        private string name;
        private string location;

        public Exchange(string symbol, string name, string location)
        {
            this.symbol = symbol;
            this.name = name;
            this.location = location;
        }

        public Exchange(XmlRpcStruct exchange)
        {
            IDataAccess data = DataAccessFactory.GetDataAccess();

            this.symbol = (string)exchange["SYMBOL"];
            this.name = (string)exchange["NAME"];
            this.location = (string)exchange["LOCATION"];
        }

        public XmlRpcStruct toStruct()
        {
            XmlRpcStruct exchange = new XmlRpcStruct();
            exchange.Add("SYMBOl", symbol);
            exchange.Add("NAME", name);
            exchange.Add("LOCATION", location);

            return exchange;
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
            get { return location;  }
        }

        #endregion
    }
}
