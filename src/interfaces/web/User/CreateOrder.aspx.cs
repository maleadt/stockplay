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

namespace StockPlay.Web
{
	public partial class User_CreateOrder : MulticulturalPage
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

                    StockplayMembershipProvider provider = (StockplayMembershipProvider)Membership.Provider;
	                StockplayMembershipUser user = (StockplayMembershipUser) provider.GetUser(User.Identity.Name,
                                                                                              (string) Session["sessionID"]);
	
	                if (security != null && latestQuote != null && user != null)
	                {
	                    Security.InnerText = security.Name;
	                    Quote.InnerText = latestQuote.Price.ToString("#0.00");
	
	                    Cash.InnerText = user.Cash.ToString("#0.00");

                        txtQuote.Text = latestQuote.Price.ToString("#0.00");
	                    Total.Text = Convert.ToString(0);
                    }
	                else
	                    Response.Redirect("~/SecuritiesOverview.aspx");
	            }
	            else
	                Response.Redirect("~/SecuritiesOverview.aspx");
            }
            else {
                if (OrderType.SelectedValue.Equals("direct")) {
                    txtQuote.Enabled = false;
                    lblPara4.Visible = true;
                    txtAmount.Visible = true;
                    lblMultiply.Visible = true;
                    txtQuote.Visible = true;
                    lblEquals.Visible = true;
                    Total.Visible = true;
                    lblOnderLimiet.Visible = false;
                    txtOnderLimiet.Visible = false;
                    lblBovenLimiet.Visible = false;
                    txtBovenLimiet.Visible = false;
                    lblBonuspunten.Visible = false;
                    txtBonuspunten.Visible = false;
                }
                if (OrderType.SelectedValue.Equals("limit"))
                {
                    txtQuote.Enabled = true;
                    lblPara4.Visible = true;
                    txtAmount.Visible = true;
                    lblMultiply.Visible = true;
                    txtQuote.Visible = true;
                    lblEquals.Visible = true;
                    Total.Visible = true;
                    lblOnderLimiet.Visible = false;
                    txtOnderLimiet.Visible = false;
                    lblBovenLimiet.Visible = false;
                    txtBovenLimiet.Visible = false;
                    lblBonuspunten.Visible = false;
                    txtBonuspunten.Visible = false;
                }
                if (OrderType.SelectedValue.Equals("bracket"))
                {
                    txtQuote.Enabled = false;
                    lblPara4.Visible = true;
                    txtAmount.Visible = true;
                    lblMultiply.Visible = false;
                    txtQuote.Visible = false;
                    lblEquals.Visible = false;
                    Total.Visible = false;
                    lblOnderLimiet.Visible = true;
                    txtOnderLimiet.Visible = true;
                    lblBovenLimiet.Visible = true;
                    txtBovenLimiet.Visible = true;
                    lblBonuspunten.Visible = false;
                    txtBonuspunten.Visible = false;

                }
                if (OrderType.SelectedValue.Equals("stoploss"))
                {
                    txtQuote.Enabled = true;
                    lblPara4.Visible = true;
                    txtAmount.Visible = true;
                    lblMultiply.Visible = true;
                    txtQuote.Visible = true;
                    lblEquals.Visible = true;
                    Total.Visible = true;
                    lblOnderLimiet.Visible = false;
                    txtOnderLimiet.Visible = false;
                    lblBovenLimiet.Visible = false;
                    txtBovenLimiet.Visible = false;
                    lblBonuspunten.Visible = false;
                    txtBonuspunten.Visible = false;
                }
                if (OrderType.SelectedValue.Equals("trailing"))
                {
                    txtQuote.Enabled = false;
                    lblPara4.Visible = true;
                    txtAmount.Visible = true;
                    lblMultiply.Visible = false;
                    txtQuote.Visible = false;
                    lblEquals.Visible = false;
                    Total.Visible = false;
                    lblOnderLimiet.Visible = false;
                    txtOnderLimiet.Visible = false;
                    lblBovenLimiet.Visible = false;
                    txtBovenLimiet.Visible = false;
                    lblBonuspunten.Visible = true;
                    txtBonuspunten.Visible = true;
                }
            }
	    }
	
	    protected void btnContinue_Click(object sender, EventArgs e)
	    {
	        Page.Validate();
	        if (Page.IsValid)
	        {
                double total = 0;

                if (OrderType.SelectedValue.Equals("direct"))
                    total = Convert.ToDouble(txtQuote.Text) * Convert.ToInt32(txtAmount.Text);

                if (OrderType.SelectedValue.Equals("limit"))
                    total = Convert.ToDouble(txtQuote.Text) * Convert.ToInt32(txtAmount.Text);

                if (OrderType.SelectedValue.Equals("bracket"))
                    total = Convert.ToDouble(txtBovenLimiet.Text) * Convert.ToInt32(txtAmount.Text);

                if (OrderType.SelectedValue.Equals("stoploss"))
                    total = Convert.ToDouble(txtQuote.Text) * Convert.ToInt32(txtAmount.Text);

                if (OrderType.SelectedValue.Equals("trailing"))
                    total = (Convert.ToDouble(txtQuote.Text) + Convert.ToDouble(txtBonuspunten.Text)) * Convert.ToInt32(txtAmount.Text);

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

                    int userID = (int) Session["userID"];
                    string sessionID = (string) Session["sessionID"];

                    if (OrderType.SelectedValue.Equals("direct"))
                        data.CreateOrder(userID, security.Isin, Convert.ToInt32(txtAmount.Text), Convert.ToDouble(txtQuote.Text), 0, "BUY_IMMEDIATE", sessionID, (ISession)this.Master);

                        if (OrderType.SelectedValue.Equals("limit"))
                            data.CreateOrder(userID, security.Isin, Convert.ToInt32(txtAmount.Text), Convert.ToDouble(txtQuote.Text), 0, "BUY", sessionID, (ISession)this.Master);

                            if (OrderType.SelectedValue.Equals("bracket"))
                                data.CreateOrder(userID, security.Isin, Convert.ToInt32(txtAmount.Text), Convert.ToDouble(txtOnderLimiet.Text), Convert.ToDouble(txtBovenLimiet.Text), "BRACKET_LIMIT_BUY", sessionID, (ISession)this.Master);

                                if (OrderType.SelectedValue.Equals("stoploss"))
                                    data.CreateOrder(userID, security.Isin, Convert.ToInt32(txtAmount.Text), Convert.ToDouble(txtQuote.Text), 0, "STOP_LOSS_BUY", sessionID, (ISession)this.Master);

                                    if (OrderType.SelectedValue.Equals("trailing"))
                                        data.CreateOrder(userID, security.Isin, Convert.ToInt32(txtAmount.Text), Convert.ToDouble(txtBonuspunten.Text), 0, "TRAILING_STOP_BUY", sessionID, (ISession)this.Master);

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