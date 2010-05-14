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
    /// Summary description for Transaction
    /// </summary>
    public class Transaction : ITransaction
    {
        private int id;
        private int userId;
        private DateTime timestamp;
        private string isin;
        private string type;
        private int amount;
        private double price;

        public Transaction(XmlRpcStruct transaction)
        {
            id = Convert.ToInt32(transaction["ID"]);
            userId = Convert.ToInt32(transaction["USER"]);
            timestamp = (DateTime) (transaction["TIME"]);
            isin = (string) (transaction["ISIN"]);
            type = (string) (transaction["TYPE"]);
            amount = Convert.ToInt32(transaction["AMOUNT"]);
            price = Convert.ToDouble(transaction["PRICE"]);
        }

        #region ITransaction Members

        public int ID
        {
            get { return id; }
        }

        public int UserID
        {
            get { return userId; }
        }

        public DateTime Timestamp
        {
            get { return timestamp; }
        }

        public string Isin
        {
            get { return isin; }
        }

        public string Type
        {
            get { return type; }
        }

        public int Amount
        {
            get { return amount; }
        }

        public double Price
        {
            get { return price; }
        }

        #endregion
    }
}
