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

public partial class User_CreateOrder : System.Web.UI.Page
{
    protected void Page_Load(object sender, EventArgs e)
    {
        if (!IsPostBack)
        {
            if (Request.Params["ISIN"] != null)
            {
                IDataAccess data = DataAccessFactory.GetDataAccess();

                ISecurity security = data.GetSecurityByIsin(Request.Params["ISIN"])[0];
                IQuote latestQuote = data.GetLatestQuoteFromSecurity(Request.Params["ISIN"]);

                StockplayMembershipUser user = (StockplayMembershipUser) Membership.GetUser(User.Identity.Name);

                if (security != null && latestQuote != null)
                {
                    Security.InnerText = security.Name;
                    Quote.InnerText = Convert.ToString(latestQuote.Price);

                    Cash.InnerText = Convert.ToString(user.Cash);

                    txtQuote.Text = Convert.ToString(latestQuote.Price);
                    Total.Text = Convert.ToString(0);
                }
                else
                    Response.Redirect("~/SecuritiesOverview.aspx");
            }
            else
                Response.Redirect("~/SecuritiesOverview.aspx");
        }
    }

    protected void btnContinue_Click(object sender, EventArgs e)
    {
        Page.Validate();
        if (Page.IsValid)
        {
            btnConfirm.Visible = true;
            btnContinue.Visible = false;
            Notification.Visible = true;
            Total.Text = Convert.ToString(Convert.ToDouble(txtQuote.Text) * Convert.ToInt32(txtAmount.Text));
            NewBalance.InnerText = Convert.ToString(Convert.ToDouble(Cash.InnerText) - Convert.ToDouble(Total.Text));
        }
    }

    protected void btnConfirm_Click(object sender, EventArgs e)
    {
        IDataAccess data = DataAccessFactory.GetDataAccess();

        ISecurity security = data.GetSecurityByIsin(Request.Params["ISIN"])[0];
        IQuote latestQuote = data.GetLatestQuoteFromSecurity(Request.Params["ISIN"]);
        StockplayMembershipUser user = (StockplayMembershipUser)Membership.GetUser(User.Identity.Name);

        data.CreateOrder(user.ID, security.Isin, Convert.ToInt32(txtAmount.Text), latestQuote.Price, "BUY");

        Response.Redirect("~/User/OrdersOverview.aspx");
    }

    protected void btnCancel_Click(object sender, EventArgs e)
    {
        Response.Redirect("~/User/OrdersOverview.aspx");
    }
}
