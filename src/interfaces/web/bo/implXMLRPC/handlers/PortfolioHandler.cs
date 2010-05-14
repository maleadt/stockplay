using CookComputing.XmlRpc;

namespace StockPlay.implXMLRPC.handlers
{
	/// <summary>
	/// Summary description for PortfolioHandler
	/// </summary>
	public interface PortfolioHandler : IXmlRpcProxy
	{
	    [XmlRpcMethod("User.Portfolio.List")]
	    XmlRpcStruct[] List(string iFilter);
	}
}
