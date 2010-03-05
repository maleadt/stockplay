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
using System.Collections.Generic;

public partial class SecuritiesOverview : System.Web.UI.Page
{

    private DataTable securitiesTable;

    protected void Page_Load(object sender, EventArgs e)
    {
        //Datatable en kolommen aanmaken
        securitiesTable = new DataTable();
        securitiesTable.Columns.Add("Symbool");
        securitiesTable.Columns["Symbool"].DataType = System.Type.GetType("System.String");
        securitiesTable.Columns.Add("Naam");
        securitiesTable.Columns["Naam"].DataType = System.Type.GetType("System.String");
        securitiesTable.Columns.Add("Type");
        securitiesTable.Columns["Type"].DataType = System.Type.GetType("System.String");
        securitiesTable.Columns.Add("Beurs");
        securitiesTable.Columns["Beurs"].DataType = System.Type.GetType("System.String");

        //Lijst van securities ophalen en invullen in datatable
        SecuritiesList dummySecurities = new SecuritiesList();
        List<Security> securitiesList = dummySecurities.GetSecurities();
        for (int i = 0; i < securitiesList.Count ; i++) 
        {
            DataRow rij = securitiesTable.NewRow();
            rij["Symbool"] = securitiesList[i].Symbol;
            rij["Naam"] = securitiesList[i].Name;
            rij["Type"] = securitiesList[i].Type;
            rij["Beurs"] = securitiesList[i].Exchange;

            securitiesTable.Rows.Add(rij);
        }

        gridViewSecurities.DataSource = securitiesTable;
        gridViewSecurities.DataBind();
    }
}
