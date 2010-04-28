using CookComputing.XmlRpc;

/// <summary>
/// Summary description for TransactionHandler
/// </summary>
public interface TransactionHandler : IXmlRpcProxy
{
    [XmlRpcMethod("User.Transaction.List")]
    XmlRpcStruct[] List(string iFilter);
}
