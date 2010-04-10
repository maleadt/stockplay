using CookComputing.XmlRpc;

/// <summary>
/// Summary description for IndexHandler
/// </summary>
public interface IndexHandler : IXmlRpcProxy
{
    [XmlRpcMethod("Index.List")]
    XmlRpcStruct[] List(string iFilter);

    [XmlRpcMethod("Index.Modify")]
    int Modify(string iFilter, XmlRpcStruct iDetails);

    [XmlRpcMethod("Index.Create")]
    int Create(XmlRpcStruct iDetails);
}
