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

        ViewState["securities"] = securitiesTable;
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
            row[2] = security.Exchange.Name;
            row[3] = security.GetLatestQuote().Buy;

            securitiesTable.Rows.Add(row);
        }

        return securitiesTable;
    }

    /* Sortering en paginering mogelijk maken zonder datasourcecontrol:
     * BRON: http://forums.asp.net/p/956540/1177923.aspx
     */
    private string ConvertSortDirectionToSql(SortDirection sortDirection)
    {
        string newSortDirection = String.Empty;

        switch (sortDirection)
        {
            case SortDirection.Ascending:
                newSortDirection = "ASC";
                break;

            case SortDirection.Descending:
                newSortDirection = "DESC";
                break;
        }

        return newSortDirection;
    }

    protected void SecuritiesGridview_PageIndexChanging(object sender, GridViewPageEventArgs e)
    {
        SecuritiesGridview.PageIndex = e.NewPageIndex;
        SecuritiesGridview.DataBind();
    }

    protected void SecuritiesGridview_Sorting(object sender, GridViewSortEventArgs e)
    {
        DataTable dataTable = SecuritiesGridview.DataSource as DataTable;

        if (dataTable != null)
        {
            DataView dataView = new DataView(dataTable);
            dataView.Sort = e.SortExpression + " " + ConvertSortDirectionToSql(e.SortDirection);

            SecuritiesGridview.DataSource = dataView;
            SecuritiesGridview.DataBind();
        }
    }

}
