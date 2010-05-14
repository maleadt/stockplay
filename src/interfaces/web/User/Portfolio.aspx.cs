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
using System.Collections.Generic;
using StockPlay;

namespace StockPlay.Web
	{
	public partial class User_Portfolio : System.Web.UI.Page
	{
	    protected void Page_Load(object sender, EventArgs e)
	    {
	        if (Request.Params["sell"] != null)
	        {
	            IDataAccess data = DataAccessFactory.GetDataAccess();
	
	            List<ISecurity> security = data.GetSecurityByIsin(Request.Params["sell"]);
                List<IUserSecurity> portfolio = data.GetUserSecurities((int)Session["userID"], (string)Session["sessionID"], (ISession)this.Master);
	
	            if (security != null && security.Count > 0 && portfolio != null && portfolio.Count > 0)
	            {
	                SellMessage.Visible = true;
	
	                SecurityName.InnerText = security[0].Name;
	                Price.InnerText = Convert.ToString(security[0].GetLatestQuote().Price);
	
	                int maxAmount = 0;
	                for (int i = 0; i < portfolio.Count; i++)
	                    if (security[0].Isin.Equals(portfolio[i].Isin))
	                        maxAmount = portfolio[i].Amount;
	
	                txtTotalAmount.Text = Convert.ToString(maxAmount);
	                txtAmountValidator.MaximumValue = Convert.ToString(maxAmount);
	            }
	            else
	                Response.Redirect("~/User/Portfolio.aspx");
	        }
	
	        if (!IsPostBack)
	        {
	            IDataAccess data = DataAccessFactory.GetDataAccess();
	
	            List<IUserSecurity> portfolio = data.GetUserSecurities((int) Session["userID"], (string) Session["sessionID"], (ISession) this.Master);
	
	            string[] isins = new string[portfolio.Count];
	            for (int i = 0; i < portfolio.Count; i++)
	                isins[i] = portfolio[i].Isin;
	
	
	            List<ISecurity> securities = null;
	            if (isins.Length > 0)
	                securities = data.GetSecurityByIsin(isins);
	
	            PortfolioGridview.DataSource = GenerateDataTable(portfolio, securities);
	            PortfolioGridview.DataBind();
            }
            else
            {
                if (OrderType.SelectedValue.Equals("direct"))
                {
                    txtPrice.Enabled = false;
                    lblVraagprijs.Visible = false;
                    txtAmount.Visible = true;
                    txtPrice.Visible = false;
                    lblOnderLimiet.Visible = false;
                    txtOnderLimiet.Visible = false;
                    lblBovenLimiet.Visible = false;
                    txtBovenLimiet.Visible = false;
                    lblBonuspunten.Visible = false;
                    txtBonuspunten.Visible = false;
                }
                if (OrderType.SelectedValue.Equals("limit"))
                {
                    txtPrice.Enabled = true;
                    lblVraagprijs.Visible = true;
                    txtAmount.Visible = true;
                    txtPrice.Visible = true;
                    lblOnderLimiet.Visible = false;
                    txtOnderLimiet.Visible = false;
                    lblBovenLimiet.Visible = false;
                    txtBovenLimiet.Visible = false;
                    lblBonuspunten.Visible = false;
                    txtBonuspunten.Visible = false;
                }
                if (OrderType.SelectedValue.Equals("bracket"))
                {
                    txtPrice.Enabled = false;
                    lblVraagprijs.Visible = false;
                    txtAmount.Visible = true;
                    txtPrice.Visible = false;
                    lblOnderLimiet.Visible = true;
                    txtOnderLimiet.Visible = true;
                    lblBovenLimiet.Visible = true;
                    txtBovenLimiet.Visible = true;
                    lblBonuspunten.Visible = false;
                    txtBonuspunten.Visible = false;

                }
                if (OrderType.SelectedValue.Equals("stoploss"))
                {
                    txtPrice.Enabled = true;
                    lblVraagprijs.Visible = true;
                    txtAmount.Visible = true;
                    txtPrice.Visible = true;
                    lblOnderLimiet.Visible = false;
                    txtOnderLimiet.Visible = false;
                    lblBovenLimiet.Visible = false;
                    txtBovenLimiet.Visible = false;
                    lblBonuspunten.Visible = false;
                    txtBonuspunten.Visible = false;
                }
                if (OrderType.SelectedValue.Equals("trailing"))
                {
                    txtPrice.Enabled = false;
                    lblVraagprijs.Visible = false;
                    txtAmount.Visible = true;
                    txtPrice.Visible = false;
                    lblOnderLimiet.Visible = false;
                    txtOnderLimiet.Visible = false;
                    lblBovenLimiet.Visible = false;
                    txtBovenLimiet.Visible = false;
                    lblBonuspunten.Visible = true;
                    txtBonuspunten.Visible = true;
                }
            }
	    }
	
	    private DataTable GenerateDataTable(List<IUserSecurity> portfolio, List<ISecurity> securities)
	    {
	        DataTable portfolioTable = new DataTable("Portfolio");
	
	        portfolioTable.Columns.Add("Isin");
	        portfolioTable.Columns["Isin"].DataType = typeof(string);
	        portfolioTable.Columns.Add("Security");
	        portfolioTable.Columns["Security"].DataType = typeof(string);
	        portfolioTable.Columns.Add("Quote");
	        portfolioTable.Columns["Quote"].DataType = typeof(double);
	        portfolioTable.Columns.Add("Amount");
	        portfolioTable.Columns["Amount"].DataType = typeof(int);
	
	        foreach (IUserSecurity security in portfolio)
	        {
	            DataRow row = portfolioTable.NewRow();
	            row[0] = security.Isin;
	            for (int i = 0; i < securities.Count; i++)
	            {
	                if (securities[i].Isin.Equals(security.Isin))
	                {
	                    row[1] = securities[i].Name;
	                    row[2] = securities[i].GetLatestQuote().Price;
	                }
	            }
	            row[3] = security.Amount;
	            portfolioTable.Rows.Add(row);
	        }
	
	        return portfolioTable;
	    }
	    protected void btnConfirm_Click(object sender, EventArgs e)
	    {
	        Page.Validate();
	
	        if (Page.IsValid && txtAmount.Text != "")
	        {
	            IDataAccess data = DataAccessFactory.GetDataAccess();
	
	            ISecurity security = data.GetSecurityByIsin(Request.Params["sell"])[0];
	            IQuote latestQuote = data.GetLatestQuoteFromSecurity(Request.Params["sell"]);
	
	            StockplayMembershipUser user = (StockplayMembershipUser)Membership.GetUser((int) Session["userID"]);

                if (OrderType.SelectedValue.Equals("direct"))
                    data.CreateOrder((int) Session["userID"], security.Isin, Convert.ToInt32(txtAmount.Text), 0, 0, "SELL_IMMEDIATE", (string) Session["sessionID"], (ISession) this.Master);

                if (OrderType.SelectedValue.Equals("limit"))
                    data.CreateOrder((int)Session["userID"], security.Isin, Convert.ToInt32(txtAmount.Text), Convert.ToDouble(txtPrice.Text), 0, "SELL", (string)Session["sessionID"], (ISession)this.Master);

                if (OrderType.SelectedValue.Equals("bracket"))
                    data.CreateOrder((int)Session["userID"], security.Isin, Convert.ToInt32(txtAmount.Text), Convert.ToDouble(txtOnderLimiet.Text), Convert.ToDouble(txtBovenLimiet.Text), "BRACKET_LIMIT_SELL", (string)Session["sessionID"], (ISession)this.Master);

                if (OrderType.SelectedValue.Equals("stoploss"))
                    data.CreateOrder((int)Session["userID"], security.Isin, Convert.ToInt32(txtAmount.Text), Convert.ToDouble(txtPrice.Text), 0, "STOP_LOSS_SELL", (string)Session["sessionID"], (ISession)this.Master);

                if (OrderType.SelectedValue.Equals("trailing"))
                    data.CreateOrder((int)Session["userID"], security.Isin, Convert.ToInt32(txtAmount.Text), Convert.ToDouble(txtBonuspunten.Text), 0, "TRAILING_STOP_SELL", (string)Session["sessionID"], (ISession)this.Master);

	            Response.Redirect("~/User/OrdersOverview.aspx");
	        }
	    }
	
	    protected void btnCancel_Click(object sender, EventArgs e)
	    {
	        Response.Redirect("~/User/Portfolio.aspx");
	    }
	}
}