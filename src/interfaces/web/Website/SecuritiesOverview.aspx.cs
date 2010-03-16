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

public partial class SecuritiesOverview : System.Web.UI.Page
{

    protected void Page_Load(object sender, EventArgs e)
    {
        IDataAccess data = DataAccess.GetInstance();

        DataTable securitiesTable = GenerateDataTable(data.GetSecuritiesList());

        SecuritiesGridview.DataSource = securitiesTable;
        SecuritiesGridview.DataBind();
    }
    protected void SecuritiesGridview_Sorting(object sender, GridViewSortEventArgs e)
    {
        
    }

    private DataTable GenerateDataTable(List<Security> securities)
    {
        DataTable securitiesTable = new DataTable("Securities");

        securitiesTable.Columns.Add("Symbol");
        securitiesTable.Columns.Add("Name");
        securitiesTable.Columns.Add("Exchange");
        securitiesTable.Columns.Add("Quote");

        foreach (Security security in securities)
        {
            DataRow row = securitiesTable.NewRow();
            row[0] = security.Symbol;
            row[1] = security.Name;
            row[2] = "test";//security.Exchange.Name;
            row[3] = 0;//security.GetLatestQuote().Buy;

            securitiesTable.Rows.Add(row);
        }

        return securitiesTable;
    }
}
