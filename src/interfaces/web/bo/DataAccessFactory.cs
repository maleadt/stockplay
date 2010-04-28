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

/// <summary>
/// Heeft slechts één statische methode die een referentie teruggeeft naar een instantie van een IDataAccess afgeleide.
/// Het type DataAccess is vastgelegd in het web.config bestand in de Appsettings-sectie onder de sleutel 'DATA_ACCESS_TYPE'
/// </summary>
namespace StockPlay
{
	public class DataAccessFactory
	{
	
	    public static IDataAccess GetDataAccess()
	    {
	        string dataAccessType = ConfigurationManager.AppSettings["DATA_ACCESS_TYPE"];
	        if (dataAccessType.Equals("XMLRPC"))
	        {
	            return implXMLRPC.DataAccess.GetInstance();
	        }
	        else
	        {
	            throw new Exception("unknown data access type specified");
	        }
	    }
	
	}
}