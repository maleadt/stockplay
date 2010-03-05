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
using System.Xml.Linq;
using System.Collections.Generic;

/// <summary>
/// Summary description for SecurtiesList
/// </summary>
public class SecuritiesList
{
    public List<Security> GetSecurities()
    {
        List<Security> securities = new List<Security>();
        securities.Add(new Security("ABC","ABC aandeel", "aandeel", new Exchange()));
        securities.Add(new Security("DEF","DEF aandeel", "aandeel", new Exchange()));
        securities.Add(new Security("GHI","GHI optie", "optie", new Exchange()));
        securities.Add(new Security("JKL","JKL tracker", "tracker", new Exchange()));

        return securities;
    }
}
