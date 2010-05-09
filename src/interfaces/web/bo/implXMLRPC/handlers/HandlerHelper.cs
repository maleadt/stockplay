using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using CookComputing.XmlRpc;
using System.Net;
using System.Text;

namespace web.bo.implXMLRPC.handlers
{
    public static class HandlerHelper
    {

        public static ExchangeHandler getPublicExchangeHandler(string xmlRpcUrl) 
        {
            ExchangeHandler handler = XmlRpcProxyGen.Create<ExchangeHandler>();
            handler.Url = xmlRpcUrl;

            handler.EnableCompression = true;

            return handler;
        }

        public static OrderHandler getPublicOrderHandler(string xmlRpcUrl)
        {
            OrderHandler handler = XmlRpcProxyGen.Create<OrderHandler>();
            handler.Url = xmlRpcUrl;
            handler.EnableCompression = true;

            return handler;
        }

        public static IndexHandler getPublicIndexHandler(string xmlRpcUrl)
        {
            IndexHandler handler = XmlRpcProxyGen.Create<IndexHandler>();
            handler.Url = xmlRpcUrl;
            handler.EnableCompression = true;

            return handler;
        }

        public static PortfolioHandler getPublicPortfolioHandler(string xmlRpcUrl)
        {
            PortfolioHandler handler = XmlRpcProxyGen.Create<PortfolioHandler>();
            handler.Url = xmlRpcUrl;
            handler.EnableCompression = true;

            return handler;
        }

        public static SecurityHandler getPublicSecurityHandler(string xmlRpcUrl)
        {
            SecurityHandler handler = XmlRpcProxyGen.Create<SecurityHandler>();
            handler.Url = xmlRpcUrl;
            handler.EnableCompression = true;

            return handler;
        }

        public static TransactionHandler getPublicTransactionHandler(string xmlRpcUrl)
        {
            TransactionHandler handler = XmlRpcProxyGen.Create<TransactionHandler>();
            handler.Url = xmlRpcUrl;
            handler.EnableCompression = true;

            return handler;
        }

        public static UserHandler getPublicUserHandler(string xmlRpcUrl)
        {
            UserHandler handler = XmlRpcProxyGen.Create<UserHandler>();
            handler.Url = xmlRpcUrl;
            handler.EnableCompression = true;

            return handler;
        }

        public static ExchangeHandler getPrivateExchangeHandler(string xmlRpcUrl, string sessionID)
        {
            ExchangeHandler handler = getPublicExchangeHandler(xmlRpcUrl);
            handler.Headers.Add("Authorization", "Basic " + getBase64SessionID(sessionID));

            return handler;
        }

        public static OrderHandler getPrivateOrderHandler(string xmlRpcUrl, string sessionID)
        {
            OrderHandler handler = getPublicOrderHandler(xmlRpcUrl);
            handler.Headers.Add("Authorization", "Basic " + getBase64SessionID(sessionID));

            return handler;
        }

        public static IndexHandler getPrivateIndexHandler(string xmlRpcUrl, string sessionID)
        {
            IndexHandler handler = getPublicIndexHandler(xmlRpcUrl);
            handler.Headers.Add("Authorization", "Basic " + getBase64SessionID(sessionID));

            return handler;
        }

        public static PortfolioHandler getPrivatePortfolioHandler(string xmlRpcUrl, string sessionID)
        {
            PortfolioHandler handler = getPublicPortfolioHandler(xmlRpcUrl);
            handler.Headers.Add("Authorization", "Basic " + getBase64SessionID(sessionID));

            return handler;
        }

        public static SecurityHandler getPrivateSecurityHandler(string xmlRpcUrl, string sessionID)
        {
            SecurityHandler handler = getPublicSecurityHandler(xmlRpcUrl);
            handler.Headers.Add("Authorization", "Basic " + getBase64SessionID(sessionID));

            return handler;
        }

        public static TransactionHandler getPrivateTransactionHandler(string xmlRpcUrl, string sessionID)
        {
            TransactionHandler handler = getPublicTransactionHandler(xmlRpcUrl);
            handler.Headers.Add("Authorization", "Basic " + getBase64SessionID(sessionID));

            return handler;
        }

        public static UserHandler getPrivateUserHandler(string xmlRpcUrl, string sessionID)
        {
            UserHandler handler = getPublicUserHandler(xmlRpcUrl);
            handler.Headers.Add("Authorization", "Basic " + getBase64SessionID(sessionID));

            return handler;
        }

        private static string getBase64SessionID(string sessionID)
        {
            byte[] stringArray = Encoding.UTF8.GetBytes(sessionID + ":");
            Convert.ToBase64String(stringArray);

            return Convert.ToBase64String(stringArray);
        }
    }
}