using CookComputing.XmlRpc;

namespace StockPlay.implXMLRPC.handlers
{
	/// <summary>
	/// Summary description for SecurityHandler
	/// </summary>
	public interface SecurityHandler : IXmlRpcProxy
	{
	    [XmlRpcMethod("Finance.Security.List")]
	    XmlRpcStruct[] List();
	
	    [XmlRpcMethod("Finance.Security.List")]
	    XmlRpcStruct[] List(string iFilter);
	
	    [XmlRpcMethod("Finance.Security.ListIndexes")]
	    XmlRpcStruct[] ListIndexes(string iFilter);
	
	    [XmlRpcMethod("Finance.Security.Modify")]
	    int Modify(string iFilter, XmlRpcStruct iDetails);
	
	    [XmlRpcMethod("Finance.Security.Create")]
	    int Create(XmlRpcStruct iDetails);
	
	    [XmlRpcMethod("Finance.Security.Remove")]
	    int Remove(string iFilter);
	
	    [XmlRpcMethod("Finance.Security.QuoteRange")]
	    System.DateTime[] getRange(string isin);
	
	    [XmlRpcMethod("Finance.Security.Quotes")]
	    XmlRpcStruct[] Quotes(string iFilter);
	
	    [XmlRpcMethod("Finance.Security.Quotes")]
	    XmlRpcStruct[] Quotes(System.DateTime iFrom, System.DateTime iTo, int iSpan, string iFilter);
	
	    [XmlRpcMethod("Finance.Security.LatestQuotes")]
	    XmlRpcStruct[] LatestQuotes(string iFilter);
	
	    [XmlRpcMethod("Finance.Security.Update")]
	    XmlRpcStruct[] Update(XmlRpcStruct iDetails);
	}
}
