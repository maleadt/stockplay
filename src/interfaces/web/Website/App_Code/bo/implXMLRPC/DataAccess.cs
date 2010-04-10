using System;
using System.Data;
using System.Configuration;
using System.Linq;
using System.Web;
using System.Web.Security;
using System.Web.UI;
using System.Web.UI.HtmlControls;
using System.Web.UI.WebControls;
using System.Web.UI.WebControls.WebParts;
using System.Xml.Linq;
using System.Collections.Generic;
using CookComputing.XmlRpc;

namespace implXMLRPC
{

    /// <summary>
    /// Summary description for DataAccess
    /// </summary>
    public class DataAccess : IDataAccess
    {
        private static DataAccess instance;

        //Handlerinstanties
        private ExchangeHandler exchangeHandler;
        private IndexHandler indexHandler;
        private OrderHandler orderHandler;
        private PortfolioHandler portfolioHandler;
        private SecurityHandler securityHandler;
        private TransactionHandler transactionHandler;
        private UserHandler userHandler;

        private string xmlRpcUrl;

        private Dictionary<string, IExchange> exchangeCache;

        private static object lockSingleton = new object(); //Object om dubbele locking te voorzien voor de instance

        private DataAccess()
        {
            xmlRpcUrl = ConfigurationManager.AppSettings["XML_RPC_SERVER"];

            exchangeHandler = XmlRpcProxyGen.Create<ExchangeHandler>();
            exchangeHandler.Url = xmlRpcUrl;
            exchangeHandler.EnableCompression = true;
            indexHandler = XmlRpcProxyGen.Create<IndexHandler>();
            indexHandler.Url = xmlRpcUrl;
            indexHandler.EnableCompression = true;
            orderHandler = XmlRpcProxyGen.Create<OrderHandler>();
            orderHandler.Url = xmlRpcUrl;
            orderHandler.EnableCompression = true;
            portfolioHandler = XmlRpcProxyGen.Create<PortfolioHandler>();
            portfolioHandler.Url = xmlRpcUrl;
            portfolioHandler.EnableCompression = true;
            securityHandler = XmlRpcProxyGen.Create<SecurityHandler>();
            securityHandler.Url = xmlRpcUrl;
            securityHandler.EnableCompression = true;
            transactionHandler = XmlRpcProxyGen.Create<TransactionHandler>();
            transactionHandler.Url = xmlRpcUrl;
            transactionHandler.EnableCompression = true;
            userHandler = XmlRpcProxyGen.Create<UserHandler>();
            userHandler.Url = xmlRpcUrl;
            userHandler.EnableCompression = true;

            exchangeCache = new Dictionary<string, IExchange>();
        }

        public static IDataAccess GetInstance()
        {
            if (instance == null)
            {
                lock (lockSingleton)
                {
                    if (instance == null)
                        instance = new DataAccess();
                }
            }
            return instance;
        }

        #region IDataAccess Members

        /**
         * SECURITIES
         */
        public List<ISecurity> GetSecuritiesList()
        {
            List<ISecurity> securities = new List<ISecurity>();

            XmlRpcStruct[] querySecurities = securityHandler.List();
            XmlRpcStruct[] queryQuotes = securityHandler.LatestQuotes("ISIN NOT '0'");
            Dictionary<string, XmlRpcStruct> quoteDictionary = new Dictionary<string, XmlRpcStruct>();
            foreach (XmlRpcStruct quote in queryQuotes)
                quoteDictionary.Add((string)quote["ISIN"], quote);

            foreach(XmlRpcStruct security in querySecurities)
            {
                XmlRpcStruct quote = null;
                if(quoteDictionary.ContainsKey((string)security["ISIN"]))
                    quote = quoteDictionary[(string)security["ISIN"]];
                securities.Add(new Security(security, quote));
            }

            return securities;
        }

        public ISecurity GetSecurityByIsin(string isin)
        {
            ISecurity security = null;

            XmlRpcStruct[] query = securityHandler.List("ISIN EQUALS '" + isin + "'");
            if(query.Length>0)
                security = new Security(query[0], null);

            return security;
        }

        // TODO - Methode testen (is deze wel nodig?)
        public List<ISecurity> GetSecuritiesFromExchange(string id)
        {
            List<ISecurity> securities = new List<ISecurity>();

            XmlRpcStruct[] query = securityHandler.List("EXCHANGE EQUALS '" + id + "'");
            foreach (XmlRpcStruct security in query)
                securities.Add(new Security(security));

            return securities;
        }

        /**
         * QUOTES
         */
        public IQuote GetLatestQuoteFromSecurity(string isin)
        {
            IQuote quote = null;

            XmlRpcStruct[] query = securityHandler.LatestQuotes("ISIN EQUALS '" + isin + "'");
            if(query.Length>0)
                quote = new Quote(query[0]);

            return quote;
        }

        public List<IQuote> GetDailyQuotesFromSecurity(string isin, DateTime minDate, DateTime maxDate)
        {
            return new List<IQuote>();
        }

        // TODO - Methode testen ( wat moet deze methode teruggeven? de output zal leeg zijn tenzij de timestamp EXACT overeenkomt )
        public IQuote GetQuoteFromSecurity(string isin, DateTime time)
        {
            IQuote quote = null;

            XmlRpcStruct[] query = securityHandler.Quotes("(ISIN EQUALS '" + isin + "') AND (TIMESTAMP EQUALS " + time + ")");
            if (query.Length > 0)
                quote = new Quote(query[0]);

            return quote;
        }

        /**
         * EXCHANGE
         */
        public IExchange GetExchangeBySymbol(string symbol)
        {
            IExchange exchange = null;

            if (exchangeCache.ContainsKey(symbol))
                exchange = exchangeCache[symbol];
            else
            {
                XmlRpcStruct[] query = exchangeHandler.List("SYMBOL EQUALS '" + symbol + "'");
                exchange = new Exchange(query[0]);
                exchangeCache.Add(symbol, exchange);
            }

            return exchange;
        }

        // TODO - Testen
        public List<IExchange> GetExchanges()
        {
            List<IExchange> exchanges = new List<IExchange>();

            XmlRpcStruct[] query = exchangeHandler.List();
            foreach(XmlRpcStruct exchange in query)
                exchanges.Add(new Exchange(exchange));

            return exchanges;
        }

        /**
         * USERS
         */
        public void CreateUser(int id, string nickname, string password, string email, bool isAdmin, string lastname, string firstname, DateTime regTime,
                                long rrn, int points, double startAmount, double cash)
        {
            User user = new User(id, nickname, password, email, isAdmin, lastname, firstname, regTime, rrn, points, startAmount, cash);
            XmlRpcStruct userStruct = user.toStruct();
            userHandler.Create(userStruct);
        }

        public bool RemoveUser(string nickname)
        {
            try
            {
                userHandler.Remove("NICKNAME EQUALS '" + nickname + "'");
            }
            catch (Exception e)
            {
                return false;
            }
            return false;
        }

        public bool UpdateUser(IUser user)
        {
            XmlRpcStruct userStruct = ((User)user).toStruct();

            try
            {
                userHandler.Modify("NICKNAME EQUALS '" + user.Nickname + "'", userStruct);
            }
            catch (Exception e)
            {
                return false;
            }

            return true;
        }

        public IUser GetUserByNickname(string nickname)
        {
            IUser user = null;

            XmlRpcStruct[] userStruct = userHandler.List("NICKNAME EQUALS '" + nickname + "'");
            if (userStruct.Length > 0)
                user = new User(userStruct[0]);

            return user;
        }

        public bool ValidateUser(string nickname, string password)
        {
            return userHandler.Validate(nickname, password);
        }

        public List<IUserSecurity> GetUserSecurities(int id)
        {
            List<IUserSecurity> userSecurities = new List<IUserSecurity>();

            XmlRpcStruct[] portfolioStruct = portfolioHandler.List("USERID EQUALS '" + id + "'");

            foreach (XmlRpcStruct userSecurity in portfolioStruct)
                userSecurities.Add(new UserSecurity(userSecurity));

            return userSecurities;
        }

        public List<ITransaction> GetUserTransactions(int id)
        {
            List<ITransaction> userTransactions = new List<ITransaction>();

            XmlRpcStruct[] transactionStruct = transactionHandler.List("USERID EQUALS '" + id + "'");

            foreach(XmlRpcStruct userTransaction in userTransactions)
                userTransactions.Add(new Transaction(userTransaction));

            return userTransactions;
        }

        public void CreateOrder(int userId, string isin, int amount, double price, string type)
        {
            Order order = new Order(-1, userId, isin, amount, price, type, "ACCEPTED", DateTime.Now, DateTime.MinValue, DateTime.MinValue);
            orderHandler.Create(order.toStruct());
        }

        public List<IOrder> GetUserOrders(int id)
        {
            List<IOrder> userOrders = new List<IOrder>();

            XmlRpcStruct[] orderStruct = orderHandler.List("USERID EQUALS '" + id + "'");

            foreach (XmlRpcStruct userOrder in orderStruct)
                userOrders.Add(new Order(userOrder));

            return userOrders;
        }

        public void CancelOrder(int orderId)
        {
            orderHandler.Cancel("ID EQUALS '" + orderId + "'");
        }

        #endregion
    }
}