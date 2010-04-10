using CookComputing.XmlRpc;

/// <summary>
/// Summary description for ExchangeHandler
/// </summary>
public interface ExchangeHandler : IXmlRpcProxy
{
    [XmlRpcMethod("Finance.Exchange.List")]
    XmlRpcStruct[] List();

    [XmlRpcMethod("Finance.Exchange.List")]
    XmlRpcStruct[] List(string iFilter);

    [XmlRpcMethod("Finance.Exchange.Modify")]
    int Modify(string iFilter, XmlRpcStruct iDetails);

    [XmlRpcMethod("Finance.Exchange.Create")]
    int Create(XmlRpcStruct iDetails);
}
