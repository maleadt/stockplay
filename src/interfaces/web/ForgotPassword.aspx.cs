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
using System.Net.Mail;

namespace StockPlay.Web
	{
	public partial class ForgotPassword : MulticulturalPage
	{
	    protected void Page_Load(object sender, EventArgs e)
	    {

	    }

        protected void Submit_Click(object sender, EventArgs e)
        {
            IDataAccess data = DataAccessFactory.GetDataAccess();

            try
            {
                StockplayMembershipProvider provider = (StockplayMembershipProvider)Membership.Provider;
                string password = provider.ResetPassword(Username.Text, "");

                string sessionID = provider.ValidateUserSession(Username.Text, password);
                StockplayMembershipUser user = (StockplayMembershipUser) provider.GetUser(Username.Text, sessionID);

                //Email met nieuw paswoord sturen
                MailMessage message = new MailMessage();

                message.From = new MailAddress(ConfigurationManager.AppSettings["EMAIL_ADDRESS"], "Stockplay Team");
                message.To.Add(new MailAddress(user.Email, user.Lastname + " " + user.Firstname));

                message.Subject = (string)GetLocalResourceObject("Subject");
                message.Body =
                    (string)GetLocalResourceObject("Body1") + " " + user.Lastname + " " + user.Firstname + ",\n\n"
                    + (string)GetLocalResourceObject("Body2") + "\n"
                    + (string)GetLocalResourceObject("Body3") + " " + password
                    + "\n\n"
                    + (string)GetLocalResourceObject("Body4") + "\n"
                    + (string)GetLocalResourceObject("Body5");

                SmtpClient client = new SmtpClient();
                client.Send(message);

                Success.Visible = true;
                Recovery.Visible = false;
            }
            catch (Exception ex)
            {
                ErrorLabel.Visible = true;
            }
        }
	}
}