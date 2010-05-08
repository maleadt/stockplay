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
	public partial class User_Overview : System.Web.UI.Page
	{
	    protected void Page_Load(object sender, EventArgs e)
	    {
	        if (!IsPostBack)
	        {
                StockplayMembershipProvider provider = (StockplayMembershipProvider)Membership.Provider;
	            StockplayMembershipUser user = (StockplayMembershipUser) provider.GetUser(User.Identity.Name,
                                                                                          (string) Session["sessionID"]);
	            Username.InnerText = user.UserName;
	            Lastname.InnerText = user.Lastname;
	            Firstname.InnerText = user.Firstname;
	            Email.InnerText = user.Email;
	
	            Balance.InnerText = Convert.ToString(user.Cash);
	
	            txtLastname.Text = user.Lastname;
	            txtFirstname.Text = user.Firstname;
	            txtEmail.Text = user.Email;
	        }
	    }
	    protected void btnUpdate_Click(object sender, EventArgs e)
	    {
	        StockplayMembershipUser user = (StockplayMembershipUser) Membership.GetUser(User.Identity.Name);
	
	        Page.Validate();
	
	        if (Page.IsValid)
	        {
	            if (Membership.Provider.ChangePassword(user.UserName, OldPassword.Text, NewPassword.Text))
	            {
	                ErrorLabel.Visible = false;
	                user.Firstname = txtFirstname.Text;
	                user.Lastname = txtLastname.Text;
	                user.Email = txtEmail.Text;
	
	                Membership.UpdateUser(user);
	            }
	            else
	            {
	                ErrorLabel.Visible = true;
	            }
	        }
	        
	    }
	}
}