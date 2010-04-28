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
using StockPlay;

namespace StockPlay.Web
	{
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
	
	                if (security != null && latestQuote != null && user != null)
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
	            double total = Convert.ToDouble(txtQuote.Text) * Convert.ToInt32(txtAmount.Text);
	            if (total > Convert.ToDouble(Cash.InnerText))
	                ErrorLabel.Visible = true;
	            else
	            {
	                btnConfirm.Visible = true;
	                btnContinue.Visible = false;
	                Notification.Visible = true;
	                Total.Text = Convert.ToString(total);
	                NewBalance.InnerText = Convert.ToString(Convert.ToDouble(Cash.InnerText) - Convert.ToDouble(Total.Text));
	            }
	        }
	    }
	
	    protected void btnConfirm_Click(object sender, EventArgs e)
	    {
	        Page.Validate();
	        if (Page.IsValid)
	        {
	            double total = Convert.ToDouble(txtQuote.Text) * Convert.ToInt32(txtAmount.Text);
	            if (total > Convert.ToDouble(Cash.InnerText))
	                ErrorLabel.Visible = true;
	            else
	            {
	                IDataAccess data = DataAccessFactory.GetDataAccess();
	
	                ISecurity security = data.GetSecurityByIsin(Request.Params["ISIN"])[0];
	
	                StockplayMembershipUser user = (StockplayMembershipUser)Membership.GetUser(User.Identity.Name);
	
	                data.CreateOrder(user.ID, security.Isin, Convert.ToInt32(txtAmount.Text), Convert.ToDouble(txtQuote.Text), "BUY");
	
	                Response.Redirect("~/User/OrdersOverview.aspx");
	            }
	        }
	    }
	
	    protected void btnCancel_Click(object sender, EventArgs e)
	    {
	        Response.Redirect("~/User/OrdersOverview.aspx");
	    }
	}

}