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
        DataAccess data = (DataAccess) Application["DataAccess"];
        List<Exchange> lijst = data.getExchanges();

        SecuritiesGridview.DataSource = lijst;
        SecuritiesGridview.DataBind();
    }
    protected void SecuritiesGridview_Sorting(object sender, GridViewSortEventArgs e)
    {
        Console.WriteLine(e.SortDirection);
    }
}
