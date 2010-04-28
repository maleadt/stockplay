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
using System.Net.Mail;
using log4net;
using log4net.Config;
using StockPlay;

namespace StockPlay.Web
{
	public partial class Register : System.Web.UI.Page
	{
	    protected void Page_Load(object sender, EventArgs e)
	    {
	        if (IsPostBack)
	        {
	            Page.Validate();
	            if (Page.IsValid)
	            {
	                StockplayMembershipProvider provider = (StockplayMembershipProvider) Membership.Provider;
	                MembershipCreateStatus status = new MembershipCreateStatus();
	                StockplayMembershipUser user = provider.CreateUser(UserName.Text, Password.Text, Email.Text, txtLastname.Text, txtFirstname.Text,
	                                    -1, out status);
	                if (status != MembershipCreateStatus.Success)
	                {
	                    ErrorLabel.Visible = true;
	                }
	                else
	                {
	                    ErrorLabel.Visible = false;
	                    TableRegister.Visible = false;
	                    FinishRegistration.Visible = true;

                        try
                        {
                            SendMail(user);
                        }
                        catch (Exception ex)
                        {
                            ILog sysLog = LogManager.GetLogger("Register");
                            XmlConfigurator.Configure();
                            sysLog.Error("Failed to send e-mail", ex);
                        }
	                }
	
	
	            }
	        }
	
	    }

        private void SendMail(StockplayMembershipUser user)
        {
            //Welkomstemail versturen met logingegevens
            MailMessage message = new MailMessage();

            message.From = new MailAddress(ConfigurationManager.AppSettings["EMAIL_ADDRESS"], "Stockplay Team");
            message.To.Add(new MailAddress(user.Email, user.Lastname + " " + user.Firstname));

            message.Subject = "Your StockPlay account has been registered";
            message.Body = "Dear " + user.Lastname + " " + user.Firstname + ",\nWelcome to StockPlay, your account is now registered!\nYou can use the "
                            + "following information to log in to your account:\nNickname: " + user.UserName + "\nPassword: " + Password.Text + "\n\nGreetings,\nthe Stockplay team.";

            SmtpClient client = new SmtpClient();
            client.Send(message);
        }

	    protected void btnCancel_Click(object sender, EventArgs e)
	    {
	        Response.Redirect("~/Default.aspx");
	    }
	}
}