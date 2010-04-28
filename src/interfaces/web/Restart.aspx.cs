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
using StockPlay;

namespace StockPlay.Web
{
	public partial class Restart : System.Web.UI.Page
	{
	    protected void Page_Load(object sender, EventArgs e)
	    {
	
	    }
	    protected void RestartButton_Click(object sender, EventArgs e)
	    {
	        //Pad van de Web.config file ophalen
	        string configPath = Server.MapPath("~/Web.config");
	        //Indien de configfile veranderd, dan start de webapplicatie opnieuw op.
	        //Daarom veranderen we de lastwritetime
	        System.IO.File.SetLastWriteTime(configPath, DateTime.Now);
	
	        Session.Abandon();
	        Response.Redirect("~/Default.aspx");
	    }
	}
}