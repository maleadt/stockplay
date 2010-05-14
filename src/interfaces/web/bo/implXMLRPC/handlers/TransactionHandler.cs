using CookComputing.XmlRpc;

namespace StockPlay.implXMLRPC.handlers
{
	/// <summary>
	/// Summary description for TransactionHandler
	/// </summary>
	public interface TransactionHandler : IXmlRpcProxy
	{
	    [XmlRpcMethod("User.Transaction.List")]
	    XmlRpcStruct[] List(string iFilter);
	}
}
