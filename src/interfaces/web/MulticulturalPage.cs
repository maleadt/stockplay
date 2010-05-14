using System;
using System.Data;
using System.Configuration;
using System.Linq;
using System.Web;
using System.Web.Security;
using System.Web.UI;
using System.Web.UI.HtmlControls;
using System.Web.UI.WebControls;
using System.Web.UI.WebControls.WebParts;
using System.Threading;
using System.Globalization;

/// <summary>
/// Hier overschrijven de methode om de Culture in te stellen, zodat de gebruiker kan
/// kiezen in welke taal hij de website wil bekijken.
/// </summary>
namespace StockPlay
{
    public class MulticulturalPage : System.Web.UI.Page
    {

        protected override void  InitializeCulture()
        {
 	        base.InitializeCulture();

            if (Session["culture"] != null)
            {
                string culture = (string)Session["culture"];

                Thread.CurrentThread.CurrentCulture = CultureInfo.CreateSpecificCulture(culture);
                Thread.CurrentThread.CurrentUICulture = new CultureInfo(culture);
            }
        }

    }
}