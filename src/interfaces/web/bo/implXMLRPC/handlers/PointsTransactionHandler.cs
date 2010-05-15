using CookComputing.XmlRpc;

namespace StockPlay.implXMLRPC.handlers
{
    /// <summary>
    /// Summary description for PointsTransactionHandler
    /// </summary>
    public interface PointsTransactionHandler : IXmlRpcProxy
    {
        [XmlRpcMethod("User.Points.Ranking")]
        XmlRpcStruct[] Ranking(string iFilter);

        [XmlRpcMethod("User.Points.EventRanking")]
        XmlRpcStruct[] EventRanking(string iFilter);

        [XmlRpcMethod("User.Points.List")]
        XmlRpcStruct[] List(string iFilter);

        [XmlRpcMethod("User.Points.CreateTransaction")]
        int CreateTransaction(XmlRpcStruct iDetails);

        [XmlRpcMethod("User.Points.DeleteTransaction")]
        int DeleteTransaction(XmlRpcStruct iDetails);
    }
}