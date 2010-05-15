using System;
using System.Collections;
using System.Configuration;
using System.Data;
using System.Linq;
using System.Web;
using System.Web.Security;
using System.Web.UI;
using System.Web.UI.HtmlControls;
using System.Web.UI.WebControls;
using System.Web.UI.WebControls.WebParts;
using StockPlay;
using System.Collections.Generic;

namespace StockPlay.Web
{
	public partial class Ranking : MulticulturalPage
	{
	    protected void Page_Load(object sender, EventArgs e)
	    {
            if (!IsPostBack)
            {
                IDataAccess data = DataAccessFactory.GetDataAccess();
                if(Request.Params["event"] == null)
                    RankingGridView.DataSource = GenerateDataTable(data.GetRanking(1, 20));
                else
                    RankingGridView.DataSource = GenerateDataTable(data.GetRankingEvent(Request.Params["event"]));
                RankingGridView.DataBind();
            }
	    }

        private DataTable GenerateDataTable(List<IRank> ranking)
        {
            //Bijbehorende users opvragen
            IDataAccess data = DataAccessFactory.GetDataAccess();

            List<int> userIDs = new List<int>();
            foreach (IRank rank in ranking)
                userIDs.Add(rank.UserID);

            List<IUser> users = data.GetUserListById(userIDs.ToArray());

            //Usernames in een woordenboek steken om snel op te halen
            Dictionary<int, IUser> userDictionary = new Dictionary<int, IUser>();

            foreach (IUser user in users)
                userDictionary.Add(user.ID, user);

            DataTable rankingTable = new DataTable("RankingTable");

            rankingTable.Columns.Add("Username");
            rankingTable.Columns["Username"].DataType = typeof(string);
            rankingTable.Columns.Add("Total");
            rankingTable.Columns["Total"].DataType = typeof(int);
            rankingTable.Columns.Add("Rank");
            rankingTable.Columns["Rank"].DataType = typeof(int);
            rankingTable.Columns.Add("Cash");
            rankingTable.Columns["Cash"].DataType = typeof(double);

            foreach (IRank rank in ranking)
            {
                DataRow row = rankingTable.NewRow();
                row[0] = userDictionary[rank.UserID].Nickname;
                row[1] = rank.Total;
                row[2] = rank.RankNumber;
                row[3] = userDictionary[rank.UserID].Cash;

                rankingTable.Rows.Add(row);
            }

            return rankingTable;
        }

        protected void TransactionGridvie_RowDatabound(object sender, GridViewRowEventArgs e)
        {
            if (e.Row.RowType == DataControlRowType.DataRow)
            {
                if(Request.Params["event"] == null || Request.Params["event"].Equals("Cash"))
                    e.Row.Cells[3].Visible = false;
            }

            if (e.Row.RowType == DataControlRowType.Header)
            {
                if (Request.Params["event"] == null || Request.Params["event"].Equals("Cash"))
                    e.Row.Cells[3].Visible = false;
            }
        }
	}
}