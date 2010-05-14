using CookComputing.XmlRpc;

namespace StockPlay.implXMLRPC.handlers
{
	/// <summary>
	/// Summary description for OrderHandler
	/// </summary>
	public interface OrderHandler : IXmlRpcProxy
	{
	    [XmlRpcMethod("User.Order.List")]
	    XmlRpcStruct[] List(string iFilter);
	
	    [XmlRpcMethod("User.Order.Create")]
	    int Create(XmlRpcStruct iDetails);
	
	    [XmlRpcMethod("User.Order.Cancel")]
	    int Cancel(string iFilter);
	}
}