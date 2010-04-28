using CookComputing.XmlRpc;

/// <summary>
/// Summary description for PortfolioHandler
/// </summary>
public interface PortfolioHandler : IXmlRpcProxy
{
    [XmlRpcMethod("User.Portfolio.List")]
    XmlRpcStruct[] List(string iFilter);
}
