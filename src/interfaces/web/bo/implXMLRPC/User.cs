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
    /// Summary description for User
    /// </summary>
    public class User : IUser
    {
        private int id;
        private string nickname;
        private string password;
        private string email;
        private string lastname;
        private string firstname;
        private DateTime regDate;
        private bool isAdmin;
        private int points;
        private double startAmount;
        private double cash;
        private long rrn;

        public User(int id, string nickname, string password, string email, bool isAdmin, string lastname, string firstname, DateTime regDate,
                    long rrn, int points, double startAmount, double cash)
        {
            this.id = id;
            this.nickname = nickname;
            this.password = password;
            this.email = email;
            this.isAdmin = isAdmin;
            this.lastname = lastname;
            this.firstname = firstname;
            this.regDate = regDate;
            this.rrn = rrn;
            this.points = points;
            this.startAmount = startAmount;
            this.cash = cash;
        }

        public User(XmlRpcStruct user)
        {
            this.id = Convert.ToInt32(user["ID"]);
            this.nickname = (string) user["NICKNAME"];
            this.password = (string) user["PASSWORD"];
            this.email = (string) user["EMAIL"];
            this.isAdmin = Convert.ToBoolean(user["ISADMIN"]);
            this.lastname = (string) user["LASTNAME"];
            this.firstname = (string) user["FIRSTNAME"];
            this.regDate = (DateTime) user["REGDATE"];
            this.rrn = Convert.ToInt64((string)user["RRN"]);
            this.points = Convert.ToInt32(user["POINTS"]);
            this.startAmount = Convert.ToDouble(user["STARTAMOUNT"]);
            this.cash = Convert.ToDouble(user["CASH"]);
        }

        public XmlRpcStruct toStruct()
        {
            XmlRpcStruct user = new XmlRpcStruct();
            user.Add("NICKNAME", nickname);
            if(password != null) user.Add("PASSWORD", password);
            user.Add("EMAIL", email);
            if(lastname != null) user.Add("LASTNAME", lastname);
            if(firstname != null) user.Add("FIRSTNAME", firstname);
            if(rrn != -1) user.Add("RRN", Convert.ToString(rrn));

            return user;
        }

        #region IUser Members

        public int ID
        { 
            get { return id; }
        }

        public string Nickname
        {
            get { return nickname; }
            set { this.nickname = value; }
        }

        public string Password
        {
            get { return password; }
            set { this.password = value; }
        }

        public string Email
        {
            get { return email; }
            set { this.email = value; }
        }

        public string Lastname
        {
            get { return lastname; }
            set { this.lastname = value; }
        }

        public string Firstname
        {
            get { return firstname; }
            set { this.firstname = value; }
        }

        public DateTime Regdate
        {
            get { return regDate; }
        }

        public bool IsAdmin
        {
            get { return isAdmin; }
        }

        public int Points
        {
            get { return points; }
        }

        public double StartAmount
        {
            get { return startAmount; }
        }

        public double Cash
        {
            get { return cash; }
        }

        public long RRN
        {
            get { return rrn; }
            set { this.rrn = value; }
        }

        #endregion
    }
}

