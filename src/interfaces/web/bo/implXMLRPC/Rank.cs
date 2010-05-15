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
    /// Summary description for Rank
    /// </summary>
    public class Rank : IRank
    {

        private int id;
        private int total;
        private int rank;

        public Rank(int id, int total, int rank)
        {
            this.id = id;
            this.total = total;
            this.rank = rank;
        }

        public Rank(XmlRpcStruct rank)
        {
            IDataAccess data = DataAccessFactory.GetDataAccess();

            this.id = Convert.ToInt32(rank["ID"]);
            this.total = Convert.ToInt32(rank["TOTAL"]);
            this.rank = Convert.ToInt32(rank["RANK"]);
        }

        #region ISecurity Members

        public int UserID
        {
            get { return id; }
        }

        public int Total
        {
            get { return total; }
        }

        public int RankNumber
        {
            get { return rank; }
        }

        #endregion
    }
}