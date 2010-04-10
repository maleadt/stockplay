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
using System.Xml.Linq;
using System.Collections.Generic;

public partial class User_Portfolio : System.Web.UI.Page
{
    protected void Page_Load(object sender, EventArgs e)
    {
        if (!IsPostBack)
        {
            IDataAccess data = DataAccessFactory.GetDataAccess();

            List<IUserSecurity> portfolio = data.GetUserSecurities(((StockplayMembershipUser)Membership.GetUser(User.Identity.Name)).ID);
            PortfolioGridview.DataSource = GenerateDataTable(portfolio);
            PortfolioGridview.DataBind();
        }
    }

    private DataTable GenerateDataTable(List<IUserSecurity> portfolio)
    {
        DataTable portfolioTable = new DataTable("Portfolio");

        portfolioTable.Columns.Add("Isin");
        portfolioTable.Columns["Isin"].DataType = typeof(string);
        portfolioTable.Columns.Add("Amount");
        portfolioTable.Columns["Amount"].DataType = typeof(int);

        foreach (IUserSecurity security in portfolio)
        {
            DataRow row = portfolioTable.NewRow();
            row[0] = security.Isin;
            row[1] = security.Amount;
            portfolioTable.Rows.Add(row);
        }

        return portfolioTable;
    }
}
