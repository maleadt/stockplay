<%@ WebService Language="C#" Class="WebService" %>

using System;
using System.Web;
using System.Web.Services;
using System.Collections.Generic;
using System.Web.Services.Protocols;
using System.Web.Script.Services;
using System.Web.Script.Serialization;
using StockPlay;
using StockPlay.implXMLRPC;

[WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
[ScriptService]
public class WebService  : System.Web.Services.WebService {

    [WebMethod]
    [ScriptMethod(ResponseFormat = ResponseFormat.Json)]
    public string getData(string isin, double from, double to) {
        IDataAccess data = DataAccessFactory.GetDataAccess();
        List<IQuote> quotes = data.GetQuotesFromSecurity(isin, Helpers.ConvertFromUnixTimestamp(from / 1000), Helpers.ConvertFromUnixTimestamp(to / 1000));
        
        if (Application[isin] == null) {
            List<ISecurity> securities = data.GetSecurityByIsin(isin);
            if (securities.Count > 0) {
                ISecurity security = data.GetSecurityByIsin(isin)[0];
                Application[isin] = security.Name;
            } else {
                List<IIndex> indexes = data.GetIndexesByIsin(isin);
                if (indexes.Count > 0)
                {
                    IIndex index = data.GetIndexesByIsin(isin)[0];
                    Application[isin] = index.Name;
                }
                else
                {
                    Application[isin] = "??";
                }
            }
        }
        
        Plot plot = new Plot();
        plot.name = (String) Application[isin];

        List<DateTime> ranges = data.GetRange(isin);

        plot.min = Helpers.ConvertToUnixTimestamp(ranges[0]) * 1000;
        plot.max = Helpers.ConvertToUnixTimestamp(ranges[1]) * 1000;

        List<Double> list;
        foreach (IQuote quote in quotes) {
            list = new List<double>();
            list.Add(Helpers.ConvertToUnixTimestamp(quote.Time)*1000);
            list.Add(quote.Price);
            plot.data.Add(list);
        }
            
        return new JavaScriptSerializer().Serialize(plot);
    }

    [WebMethod]
    [ScriptMethod(ResponseFormat = ResponseFormat.Json)]
    public string getVolumes(string isin, double from, double to)
    {
        IDataAccess data = DataAccessFactory.GetDataAccess();
        List<IQuote> quotes = data.GetQuotesFromSecurity(isin, Helpers.ConvertFromUnixTimestamp(from / 1000), Helpers.ConvertFromUnixTimestamp(to / 1000));

        Plot plot = new Plot();

        List<Double> list;
        foreach (IQuote quote in quotes)
        {
            list = new List<double>();
            list.Add(Helpers.ConvertToUnixTimestamp(quote.Time) * 1000);
            list.Add(quote.Volume);
            plot.data.Add(list);
        }

        return new JavaScriptSerializer().Serialize(plot);
    }    
}