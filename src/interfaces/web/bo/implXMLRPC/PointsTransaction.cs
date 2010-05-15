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
    /// Summary description for PointsTransaction
    /// </summary>
    public class PointsTransaction : IPointsTransaction
    {

        private int userId;
        private DateTime timestamp;
        private PointsType type;
        private int delta;
        private string comments;

        public PointsTransaction(XmlRpcStruct pointsTransaction)
        {
            userId = Convert.ToInt32(pointsTransaction["USER"]);
            timestamp = Convert.ToDateTime(pointsTransaction["AMOUNT"]);
            type = (PointsType)Enum.Parse(typeof(PointsType), (string)pointsTransaction["TYPE"]);
            delta = Convert.ToInt32(pointsTransaction["DELTA"]);
            comments = (string)pointsTransaction["COMMENTS"];
        }

        public XmlRpcStruct toStruct()
        {
            XmlRpcStruct pointsTransaction = new XmlRpcStruct();
            pointsTransaction.Add("USER", userId);
            pointsTransaction.Add("TIMESTAMP", timestamp);
            pointsTransaction.Add("TYPE", type.ToString());
            pointsTransaction.Add("DELTA", delta);
            pointsTransaction.Add("COMMENTS", comments);

            return pointsTransaction;
        }

        public int UserID
        {
            get { return userId; }
        }

        public DateTime Timestamp
        {
            get { return timestamp; }
        }

        public PointsType Type
        {
            get { return type; }
        }

        public int Delta
        {
            get { return delta; }
        }

        public string Comments
        {
            get { return comments; }
        }
    }
}