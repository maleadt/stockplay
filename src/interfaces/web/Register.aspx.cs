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
	public partial class Register : MulticulturalPage
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
	                provider.CreateUser(UserName.Text, Password.Text, Email.Text,
                                        txtLastname.Text, txtFirstname.Text,- 1, out status);

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
                            SendMail();
                        }
                        catch (Exception ex)
                        {
                            ILog sysLog = LogManager.GetLogger("Register");
                            sysLog.Error("Failed to send e-mail", ex);
                        }
	                }
	            }
	        }
	
	    }

        //TODO - Vertalen van emailbericht
        private void SendMail()
        {
            //Welkomstemail versturen met logingegevens
            MailMessage message = new MailMessage();

            message.From = new MailAddress(ConfigurationManager.AppSettings["EMAIL_ADDRESS"], "Stockplay Team");
            message.To.Add(new MailAddress(Email.Text, txtLastname.Text + " " + txtFirstname.Text));

            message.Subject = (string) GetLocalResourceObject("Subject");
            message.Body = 
                (string)GetLocalResourceObject("Body1") + " " + txtLastname.Text + " " + txtFirstname.Text + ",\n\n"
                + (string)GetLocalResourceObject("Body2") + "\n"
                + (string)GetLocalResourceObject("Body3") + "\n" + (string)GetLocalResourceObject("Body4") + " " + UserName.Text + "\n"
                +(string)GetLocalResourceObject("Body5") + Password.Text
                + "\n\n"
                +(string)GetLocalResourceObject("Body6") + "\n"
                + (string)GetLocalResourceObject("Body7");

            SmtpClient client = new SmtpClient();
            client.Send(message);
        }

	    protected void btnCancel_Click(object sender, EventArgs e)
	    {
	        Response.Redirect("~/Default.aspx");
	    }
	}
}