using CookComputing.XmlRpc;

/// <summary>
/// Behandelt de XML-RPC aanvragen die betrekking hebben op de gebruikers.
/// </summary>
public interface UserHandler : IXmlRpcProxy
{
    [XmlRpcMethod("User.Hello")]
    int Hello(string client, int protocol);

    [XmlRpcMethod("User.Create")]
    int Create(XmlRpcStruct user);

    [XmlRpcMethod("User.Details")]
    XmlRpcStruct[] Details(string iFilter);

    [XmlRpcMethod("User.List")]
    XmlRpcStruct[] List(string iFilter);

    [XmlRpcMethod("User.Modify")]
    int Modify(string iFilter, XmlRpcStruct user);

    [XmlRpcMethod("User.Remove")]
    int Remove(string iFilter);

    [XmlRpcMethod("User.Validate")]
    string Validate(string nickname, string password);
}
