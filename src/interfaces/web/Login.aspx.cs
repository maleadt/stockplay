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
using System.Collections.Generic;

namespace StockPlay.Web
	{
	public partial class Login : System.Web.UI.Page
	{
	    protected void Page_Load(object sender, EventArgs e)
	    {
	
	    }
	    protected void btnLogin_Click(object sender, EventArgs e)
	    {
            StockplayMembershipProvider provider = (StockplayMembershipProvider) Membership.Provider;

            //Proberen in te loggen (als de gebruiker foute gegevens opgeeft krijgen we een 'invalid
            //credentials' exception van de backend)
            try
            {
                string sessionID = provider.ValidateUserSession(txtUsername.Text, txtPassword.Text);

                if (!sessionID.Equals(""))
                {
                    Session["sessionID"] = sessionID;
                    Session["userID"] = ((StockplayMembershipUser)provider.GetUser(txtUsername.Text, sessionID)).ID;

                    FormsAuthentication.RedirectFromLoginPage(txtUsername.Text, chkRememberMe.Enabled);
                }
            }
            catch (Exception ex)
            {
                // TODO Foutbericht tonen
            }
	    }
	}
}