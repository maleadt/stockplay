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

public partial class Register : System.Web.UI.Page
{
    protected void Page_Load(object sender, EventArgs e)
    {

    }
    protected void CreateUser1_CreatedUser(object sender, EventArgs e)
    {
        //TODO Eigen CreateUser pagina maken, zodat de user vanaf de eerste keer goed aangemaakt wordt!!
        StockplayMembershipUser user = (StockplayMembershipUser) Membership.GetUser(CreateUser1.UserName);
        user.Lastname =  ((TextBox) CreateUser1.CreateUserStep.ContentTemplateContainer.FindControl("txtLastname")).Text;
        user.Firstname =  ((TextBox) CreateUser1.CreateUserStep.ContentTemplateContainer.FindControl("txtFirstname")).Text;
        Membership.UpdateUser(user);
    }
}
