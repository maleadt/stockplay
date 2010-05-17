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
                txtSearchUser.Attributes.Add("onKeyPress", "javascript:if (event.keyCode == 13) __doPostBack('" + btnSearchUser.UniqueID + "','')");
                txtSearchRanking.Attributes.Add("onKeyPress", "javascript:if (event.keyCode == 13) __doPostBack('" + btnSearchRanking.UniqueID + "','')");

                IDataAccess data = DataAccessFactory.GetDataAccess();
                if (Request.Params["event"] == null)
                {
                    Global.Disabled = true;

                    SearchUserP.Visible = true;
                    SearchRankingP.Visible = true;
                    EmptyNotification.Visible = false;

                    RankingGridView.Visible = true;
                    PointsTransactionGridView.Visible = false;

                    RankingGridView.DataSource = GenerateRankingTable(data.GetRanking(1, 20));
                    RankingGridView.DataBind();
                }
                else
                {
                    if (Request.Params["event"].Equals("CASH"))
                        Cashrank.Disabled = true;
                    else if (Request.Params["event"].Equals("PROFIT"))
                        Profitrank.Disabled = true;

                    SearchUserP.Visible = false;
                    SearchRankingP.Visible = false;
                    EmptyNotification.Visible = false;

                    RankingGridView.Visible = false;
                    PointsTransactionGridView.Visible = true;

                    DataTable table = GeneratePointsTransactionTable(data.GetRankingEvent(Request.Params["event"]));
                    DataView view = table.DefaultView;
                    view.Sort = "Points DESC";

                    PointsTransactionGridView.DataSource = view;
                    PointsTransactionGridView.DataBind();
                }
            }
	    }

        private DataTable GeneratePointsTransactionTable(List<IPointsTransaction> list)
        {
            //Bijbehorende users opvragen
            IDataAccess data = DataAccessFactory.GetDataAccess();

            List<int> userIDs = new List<int>();
            foreach (IPointsTransaction transaction in list)
                userIDs.Add(transaction.UserID);

            List<IUser> users = data.GetUserListById(userIDs.ToArray());

            //Usernames in een woordenboek steken om snel op te halen
            Dictionary<int, IUser> userDictionary = new Dictionary<int, IUser>();

            foreach (IUser user in users)
                userDictionary.Add(user.ID, user);

            DataTable transactionTable = new DataTable("PointsTransactionTable");

            transactionTable.Columns.Add("Username");
            transactionTable.Columns["Username"].DataType = typeof(string);
            transactionTable.Columns.Add("Points");
            transactionTable.Columns["Points"].DataType = typeof(int);
            transactionTable.Columns.Add("Comments");
            transactionTable.Columns["Comments"].DataType = typeof(string);

            foreach (IPointsTransaction transaction in list)
            {
                DataRow row = transactionTable.NewRow();
                row[0] = userDictionary[transaction.UserID].Nickname;
                row[1] = transaction.Delta;
                row[2] = transaction.Comments;

                transactionTable.Rows.Add(row);
            }

            return transactionTable;
        }

        private DataTable GenerateRankingTable(List<IRank> ranking)
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

            foreach (IRank rank in ranking)
            {
                DataRow row = rankingTable.NewRow();
                row[0] = userDictionary[rank.UserID].Nickname;
                row[1] = rank.Total;
                row[2] = rank.RankNumber;

                rankingTable.Rows.Add(row);
            }

            return rankingTable;
        }

        protected void SearchUser(object sender, EventArgs e)
        {
            IDataAccess data = DataAccessFactory.GetDataAccess();

            IUser user = data.GetUserByNickname(txtSearchUser.Text);

            if (user != null)
            {
                List<IRank> ranking = new List<IRank>();

                ranking.Add(data.GetRanking(user.ID));

                DataTable table = GenerateRankingTable(ranking);

                SearchUserP.Visible = true;
                SearchRankingP.Visible = true;
                EmptyNotification.Visible = false;

                RankingGridView.Visible = true;
                PointsTransactionGridView.Visible = false;

                RankingGridView.DataSource = table;
                RankingGridView.DataBind();
            }
            else
            {
                EmptyNotification.Visible = true;
                RankingGridView.Visible = false;
            }
        }

        protected void SearchRanking(object sender, EventArgs e)
        {
            IDataAccess data = DataAccessFactory.GetDataAccess();

            try
            {
                int rank = Convert.ToInt32(txtSearchRanking.Text);

                List<IRank> ranking = data.GetRanking(rank - 10, rank + 10);

                if (ranking.Count != 0)
                {
                    DataTable table = GenerateRankingTable(ranking);

                    SearchUserP.Visible = true;
                    SearchRankingP.Visible = true;
                    EmptyNotification.Visible = false;

                    RankingGridView.Visible = true;
                    PointsTransactionGridView.Visible = false;

                    RankingGridView.DataSource = table;
                    RankingGridView.DataBind();
                }
                else
                {
                    EmptyNotification.Visible = true;
                    RankingGridView.Visible = false;
                }
            }
            catch (Exception ex)
            {
                Response.Redirect("~/Ranking.aspx");
            }
        }
	}
}