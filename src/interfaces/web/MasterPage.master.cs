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

namespace StockPlay.Web
	{
	public partial class MasterPage : System.Web.UI.MasterPage, ISession
	{
	    protected void Page_Load(object sender, EventArgs e)
	    {
            if (Session["culture"] != null)
            {

            }
	    }

        //Gevonden op http://forums.asp.net/t/969928.aspx?PageIndex=1
        protected void DropDownList1_SelectedIndexChanged(object sender, EventArgs e)
        {
            Session["culture"] = ((DropDownList)sender).SelectedValue;
            ((DropDownList)sender).SelectedIndex = 0;
            
            //Pagina vernieuwen
            Response.Redirect(Page.Request.Url.ToString());
        }

        public void handleSessionTimeout()
        {
            FormsAuthentication.SignOut();
            Response.Redirect("~/Login.aspx");
        }
    }
}