<%@ WebService Language="C#" Class="WebService" %>

using System;
using System.Web;
using System.Web.Services;
using System.Collections.Generic;
using System.Web.Services.Protocols;
using System.Web.Script.Services;
using System.Web.Script.Serialization;
using StockPlay;
using StockPlay.implXMLRPC; // TODO: ook interface voor maken?

[WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
[ScriptService]
public class WebService  : System.Web.Services.WebService {

    [WebMethod]
    [ScriptMethod(ResponseFormat = ResponseFormat.Json)]
    public string getData(string isin, double from, double to) {
        IDataAccess data = DataAccessFactory.GetDataAccess();
        List<IQuote> quotes = data.GetQuotesFromSecurity(isin, Helpers.ConvertFromUnixTimestamp(from / 1000), Helpers.ConvertFromUnixTimestamp(to / 1000));
        ISecurity security = data.GetSecurityByIsin(isin)[0];

        Plot plot = new Plot();
        plot.name = security.Name;
        plot.min = Helpers.ConvertToUnixTimestamp(data.GetFirstTime(isin)) * 1000;
        plot.max = Helpers.ConvertToUnixTimestamp(data.GetLatestTime(isin)) * 1000;

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