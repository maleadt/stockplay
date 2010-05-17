using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using StockPlay;
using System.Data;

namespace web.User
{
    public partial class PointsTransaction : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {
                IDataAccess data = DataAccessFactory.GetDataAccess();

                FillPointsTables(data.GetPointTransactions((int)Session["userID"], (string)Session["sessionID"], (ISession)this.Master));

                List<IUser> user = data.GetUserListById((int) Session["userID"]);
                TotalPoints.Text = Convert.ToString(user[0].Points);
            }
        }

        private void FillPointsTables(List<IPointsTransaction> points)
        {
            DataTable newPointsTable = new DataTable("NewPointsTransactions");
            DataTable pointsTable = new DataTable("PointsTransactions");

            MakeColumns(newPointsTable);
            MakeColumns(pointsTable);

            foreach (IPointsTransaction point in points)
            {
                DataRow row;
                if (DateTime.Now.Subtract(point.Timestamp) < TimeSpan.FromDays(1))
                    row = newPointsTable.NewRow();
                else
                    row = pointsTable.NewRow();

                row[0] = point.Type.ToString();
                row[1] = point.Delta;
                row[2] = point.Timestamp;
                row[3] = point.Comments;

                if (DateTime.Now.Subtract(point.Timestamp) < TimeSpan.FromDays(1))
                    newPointsTable.Rows.Add(row);
                else
                    pointsTable.Rows.Add(row);
            }

            if (newPointsTable.Rows.Count > 0)
                NewPoints.Visible = true;
            else
                NewPoints.Visible = false;

            PointsGridView.DataSource = pointsTable;
            PointsGridView.DataBind();

            NewPointsGridView.DataSource = newPointsTable;
            NewPointsGridView.DataBind();
        }

        private void MakeColumns(DataTable table)
        {
            table.Columns.Add("Type");
            table.Columns["Type"].DataType = typeof(string);
            table.Columns.Add("Points");
            table.Columns["Points"].DataType = typeof(int);
            table.Columns.Add("Date");
            table.Columns["Date"].DataType = typeof(DateTime);
            table.Columns.Add("Comments");
            table.Columns["Comments"].DataType = typeof(string);
        }
    }
}