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

namespace implXMLRPC
{

    /// <summary>
    /// Summary description for UserSecurity
    /// </summary>
    public class UserSecurity : IUserSecurity
    {
        private int amount;
        private string isin;
        private int userID;

        public UserSecurity(XmlRpcStruct userSecurity)
        {
            this.amount = Convert.ToInt32(userSecurity["AMOUNT"]);
            this.isin = (string) userSecurity["ISIN"];
            this.userID = Convert.ToInt32(userSecurity["USER"]);
        }

        public int Amount
        {
            get { return amount; }
        }

        public string Isin
        {
            get { return isin; }
        }

        public int UserID
        {
            get { return userID; }
        }
    }
}
