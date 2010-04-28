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
using System.Net.Mail;

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


            }
        }

    }
    //protected void CreateUser1_CreatedUser(object sender, EventArgs e)
    //{
    //    //TODO Eigen CreateUser pagina maken, zodat de user vanaf de eerste keer goed aangemaakt wordt!!
    //    StockplayMembershipUser user = (StockplayMembershipUser) Membership.GetUser(CreateUser1.UserName);
    //    user.Lastname =  ((TextBox) CreateUser1.CreateUserStep.ContentTemplateContainer.FindControl("txtLastname")).Text;
    //    user.Firstname =  ((TextBox) CreateUser1.CreateUserStep.ContentTemplateContainer.FindControl("txtFirstname")).Text;
    //    Membership.UpdateUser(user);

    //    //Welkomstemail versturen met logingegevens
    //    MailMessage message = new MailMessage();

    //    message.From = new MailAddress(ConfigurationManager.AppSettings["EMAIL_ADDRESS"], "Stockplay Team");
    //    message.To.Add(new MailAddress(user.Email, user.Lastname + " " + user.Firstname));

    //    message.Subject = "Your StockPlay account has been registered";
    //    message.Body = "Dear " + user.Lastname + " " + user.Firstname + ",\nWelcome to StockPlay, your account is now registered!\nYou can use the "
    //                    + "following information to log in to your account:\nNickname: " + user.UserName + "\nPassword: TODO\n\nGreetings,\nthe Stockplay team.";

    //    SmtpClient client = new SmtpClient();
    //    client.Send(message);
    //}
    protected void btnCancel_Click(object sender, EventArgs e)
    {
        Response.Redirect("~/Default.aspx");
    }
}
