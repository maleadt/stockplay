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

public partial class SecurityDetail : System.Web.UI.Page
{
    protected void Page_Load(object sender, EventArgs e)
    {
        if(Request.Params["param"] != null) {
            IDataAccess data = DataAccess.GetInstance();

            try
            {
                Security security = data.GetSecurityBySymbol(Request.Params["param"]);
                GeneratePage(security);
            }
            catch (Exception ex)
            {
                Response.Redirect("~/SecuritiesOverview.aspx");
            }
        }
        else
            Response.Redirect("~/SecuritiesOverview.aspx");
    }

    private void GeneratePage(Security security)
    {
        Quote latestQuote = security.GetLatestQuote();
        Name.InnerText = security.Name;
        Value.InnerText = Convert.ToString(latestQuote.Price);
        Exchange.InnerText = security.Exchange.Name;
        //ISIN.InnerText = security.ISIN;
        Open.InnerText = Convert.ToString(latestQuote.Open);
        High.InnerText = Convert.ToString(latestQuote.High);
        Low.InnerText = Convert.ToString(latestQuote.Low);
    }
}
