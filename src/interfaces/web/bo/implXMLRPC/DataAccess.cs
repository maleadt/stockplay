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
using System.Collections.Generic;
using CookComputing.XmlRpc;
using log4net;
using log4net.Config;
using System.Text;
using StockPlay;
using web.bo.implXMLRPC.handlers;

namespace StockPlay.implXMLRPC
{
    /// <summary>
    /// Summary description for DataAccess
    /// </summary>
    public class DataAccess : IDataAccess
    {
        //
        // Member data
        //

        private static DataAccess instance;

        private readonly ILog sysLog = LogManager.GetLogger(typeof(DataAccess));

        //Handlerinstanties die gebruikt worden voor de publiek toegankelijke requests
        private ExchangeHandler publicExchangeHandler;
        private IndexHandler publicIndexHandler;
        private OrderHandler publicOrderHandler;
        private PortfolioHandler publicPortfolioHandler;
        private SecurityHandler publicSecurityHandler;
        private TransactionHandler publicTransactionHandler;
        private UserHandler publicUserHandler;

        private string publicXmlRpcUrl;
        private string privateXmlRpcUrl;

        private Dictionary<string, IExchange> exchangeCache;

        private static object lockSingleton = new object(); //Object om dubbele locking te voorzien voor de instance


        //
        // Constructie
        //

        private DataAccess()
        {
            XmlConfigurator.Configure();

            sysLog.Info("DataAccess startup");

            publicXmlRpcUrl = ConfigurationManager.AppSettings["PUBLIC_XML_RPC_SERVER"];
            privateXmlRpcUrl = ConfigurationManager.AppSettings["PRIVATE_XML_RPC_SERVER"];

            publicExchangeHandler = HandlerHelper.getPublicExchangeHandler(publicXmlRpcUrl);
            publicIndexHandler = HandlerHelper.getPublicIndexHandler(publicXmlRpcUrl);
            publicOrderHandler = HandlerHelper.getPublicOrderHandler(publicXmlRpcUrl);
            publicPortfolioHandler = HandlerHelper.getPublicPortfolioHandler(publicXmlRpcUrl);
            publicSecurityHandler = HandlerHelper.getPublicSecurityHandler(publicXmlRpcUrl);
            publicTransactionHandler = HandlerHelper.getPublicTransactionHandler(publicXmlRpcUrl);
            publicUserHandler = HandlerHelper.getPublicUserHandler(publicXmlRpcUrl);

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
            return GetSecuritiesList("");
        }

        public List<ISecurity> GetSecuritiesList(string searchterm)
        {
            try
            {
                sysLog.Info("Request: 'GetSecuritiesList' - Filter: '" + searchterm + "'");

                List<ISecurity> securities = new List<ISecurity>();

                //De zoekterm zoekt zowel op isin, symbool, naam en exchange
                XmlRpcStruct[] querySecurities = null;
                if (searchterm == "")
                    querySecurities = publicSecurityHandler.List();
                else
                    querySecurities = publicSecurityHandler.List("ISIN =~ '" + searchterm + "'ri || NAME =~ '" + searchterm
                                                            + "'ri || EXCHANGE =~ '" + searchterm
                                                            + "'ri || SYMBOL =~ '" + searchterm + "'ri");

                XmlRpcStruct[] queryQuotes = publicSecurityHandler.LatestQuotes("");

                Dictionary<string, XmlRpcStruct> quoteDictionary = new Dictionary<string, XmlRpcStruct>();
                foreach (XmlRpcStruct quote in queryQuotes)
                    quoteDictionary.Add((string)quote["ISIN"], quote);

                foreach (XmlRpcStruct security in querySecurities)
                {
                    XmlRpcStruct quote = null;
                    if (quoteDictionary.ContainsKey((string)security["ISIN"]))
                        quote = quoteDictionary[(string)security["ISIN"]];
                    securities.Add(new Security(security, quote));
                }

                return securities;
            }
            catch (Exception e)
            {
                sysLog.Error("Error when requesting SecuritiesList", e);

                return null;
            }
        }

        public List<ISecurity> GetSecurityByIsin(params string[] isin)
        {
            try
            {
                //Filter opbouwen
                StringBuilder parameters = new StringBuilder();
                for (int i = 0; i < isin.Length - 1; i++)
                    parameters.Append("ISIN == '" + isin[i] + "' || ");
                parameters.Append("ISIN == '" + isin[isin.Length - 1] + "'");

                sysLog.Info("Request: 'GetSecurityByIsin' - Requested ISINs: '" + parameters.ToString() + "'");

                List<ISecurity> securities = new List<ISecurity>();

                //Securities ophalen via XML-RPC en omzetten naar objecten
                XmlRpcStruct[] query = publicSecurityHandler.List(parameters.ToString());
                foreach (XmlRpcStruct security in query)
                    securities.Add(new Security(security));

                return securities;
            }
            catch (Exception e)
            {
                sysLog.Error("Error when requesting SecurityByIsin", e);

                return null;
            }
        }

        public List<ISecurity> GetSecuritiesFromExchange(string id)
        {
            try
            {
                sysLog.Info("Request: 'GetSecuritiesFromExchange' - Requested Exchange: '" + id + "'");

                List<ISecurity> securities = new List<ISecurity>();

                XmlRpcStruct[] query = publicSecurityHandler.List("EXCHANGE == '" + id + "'");
                foreach (XmlRpcStruct security in query)
                    securities.Add(new Security(security));

                return securities;
            }
            catch (Exception e)
            {
                sysLog.Error("Error when requesting SecuritiesFromExchange", e);

                return null;
            }
        }

        /**
         * QUOTES
         */

        public IQuote GetLatestQuoteFromSecurity(string isin)
        {
            try
            {
                sysLog.Info("Request: 'GetLatestQuoteFromSecurity' - Requested Security: '" + isin + "'");

                IQuote quote = null;

                XmlRpcStruct[] query = publicSecurityHandler.LatestQuotes("ISIN == '" + isin + "'");
                if (query.Length > 0)
                    quote = new Quote(query[0]);
                else
                    sysLog.Debug("Security with ISIN '" + isin + "' does not have quotes");

                return quote;
            }
            catch (Exception e)
            {
                sysLog.Error("Error when requesting LatestQuoteFromSecurity", e);

                return null;
            }
        }

        public List<IQuote> GetLatestQuotesFromSecurities(List<ISecurity> securities)
        {
            try
            {
                List<IQuote> quotes = new List<IQuote>();

                List<string> isins = new List<string>();
                foreach (ISecurity security in securities)
                    isins.Add("ISIN == '" + security.Isin + "'");

                // TODO - deze info wordt niet doorgestuurd, wss te groot
                sysLog.Info("Request: 'GetLatestQuotesFromSecurities' - Requested Securities: '" + string.Join(" ", isins.ToArray()) + "'");

                XmlRpcStruct[] queries = publicSecurityHandler.LatestQuotes(string.Join(" || ", isins.ToArray()));
                foreach (XmlRpcStruct query in queries)
                {
                    quotes.Add(new Quote(query));
                }

                return quotes;
            }
            catch (Exception e)
            {
                sysLog.Error("Error when requesting LatestQuotesFromSecurity", e);

                return null;
            }
        }

        //TODO - Deze methode schrijven (of eventueel weglaten?) -- Dient om tabel van laatste quotes weer te geven op SecurityDetail pagina
        public List<IQuote> GetDailyQuotesFromSecurity(string isin, DateTime minDate, DateTime maxDate)
        {
            return new List<IQuote>();
        }

        public List<IQuote> GetQuotesFromSecurity(string isin, DateTime iFrom, DateTime iTo)
        {
            try
            {
                sysLog.Info("Request: 'GetQuotesFromSecurity' - Requested Security: '" + isin + "' from '" + iFrom.ToString() + "' to '" + iTo.ToString());

                List<IQuote> quotes = new List<IQuote>();
                string from = iFrom.Year + "-" + iFrom.Month + "-" + iFrom.Day + "T" + iFrom.Hour + ":" + iFrom.Minute + "Z";
                string to = iTo.Year + "-" + iTo.Month + "-" + iTo.Day + "T" + iTo.Hour + ":" + iTo.Minute + "Z";

                XmlRpcStruct[] query = publicSecurityHandler.Quotes("ISIN == '" + isin + "' && TIMESTAMP <= '" + to + "'d && TIMESTAMP > '" + from + "'d");
                foreach (XmlRpcStruct quote in query)
                    quotes.Add(new Quote(quote));

                return quotes;
            }
            catch (Exception e)
            {
                sysLog.Error("Error when requesting QuotesFromSecurity", e);

                return null;
            }

        }


        public DateTime GetLatestTime(string isin)
        {
            try
            {
                sysLog.Info("Request: 'GetLatestTime' - Requested Security: '" + isin + "'");

                DateTime time = publicSecurityHandler.getLatestTime(isin);
                return time;
            }
            catch(Exception e)
            {
                sysLog.Error("Error when requesting GetLatestTime", e);

                return DateTime.MinValue;
            }
        }

        public DateTime GetFirstTime(string isin)
        {
            try
            {
                sysLog.Info("Request: 'GetFirstTime' - Requested Security: '" + isin + "'");

                DateTime time = publicSecurityHandler.getFirstTime(isin);
                return time;
            }
            catch (Exception e)
            {
                sysLog.Error("Error when requesting GetFirstTime", e);

                return DateTime.MinValue;
            }
        }
        
        /**
         * EXCHANGE
         */

        public IExchange GetExchangeBySymbol(string symbol)
        {
            try
            {
                IExchange exchange = null;

                if (exchangeCache.ContainsKey(symbol))
                    exchange = exchangeCache[symbol];
                else
                {
                    sysLog.Info("Request: 'GetExchangeBySymbol' - Requested Exchange: '" + symbol + "'");

                    XmlRpcStruct[] query = publicExchangeHandler.List("SYMBOL == '" + symbol + "'");
                    exchange = new Exchange(query[0]);
                    exchangeCache.Add(symbol, exchange);
                }

                return exchange;
            }
            catch (Exception e)
            {
                sysLog.Error("Error when requesting ExchangeBySymbol", e);

                return null;
            }
        }

        public List<IExchange> GetExchanges()
        {
            try
            {
                sysLog.Info("Request: 'GetExchanges'");

                List<IExchange> exchanges = new List<IExchange>();

                XmlRpcStruct[] query = publicExchangeHandler.List();
                foreach (XmlRpcStruct exchange in query)
                    exchanges.Add(new Exchange(exchange));

                return exchanges;
            }
            catch (Exception e)
            {
                sysLog.Error("Error when requesting Exchanges", e);

                return null;
            }
        }

        /**
         * USERS
         */

        public void CreateUser(int id, string nickname, string password, string email, bool isAdmin, string lastname, string firstname, DateTime regTime,
                                long rrn, int points, double startAmount, double cash)
        {
            try
            {
                sysLog.Info("Request: CreateUser - Userdata: ID=" + id + " NICKNAME=" + nickname + " EMAIL= " + email
                            + " LASTNAME= " + lastname + " FIRSTNAME= " + firstname + " REGTIME= " + regTime + " RRN= " + rrn);

                User user = new User(id, nickname, password, email, isAdmin, lastname, firstname, regTime, rrn, points, startAmount, cash);
                XmlRpcStruct userStruct = user.toStruct();
                publicUserHandler.Create(userStruct);
            }
            catch (Exception e)
            {
                sysLog.Error("Eror when creating user: ", e);

                throw e; //Verderwerpen naar Membershipklasse
            }
        }

        public bool RemoveUser(string nickname, string sessionID)
        {
            try
            {
                sysLog.Info("Request: RemoveUser - Requested User: '" + nickname + "'");

                UserHandler privateUserHandler = HandlerHelper.getPrivateUserHandler(privateXmlRpcUrl, sessionID);
                privateUserHandler.Remove("NICKNAME == '" + nickname + "'");
            }
            catch (Exception e)
            {
                sysLog.Error("Error when removing user '" + nickname + "'", e);

                return false;
            }
            return false;
        }

        public bool UpdateUser(IUser user, string sessionID)
        {
            try
            {
                XmlRpcStruct userStruct = ((User)user).toStruct();

                sysLog.Info("Request: UpdateUser - Requested User: '" + user + "', Userdata: FIRSTNAME=" + user.Firstname
                                                                                   + " LASTNAME=" + user.Lastname
                                                                                   + " EMAIL=" + user.Email);

                UserHandler privateUserHandler = HandlerHelper.getPrivateUserHandler(privateXmlRpcUrl, sessionID);
                privateUserHandler.Modify("NICKNAME == '" + user.Nickname + "'", userStruct);
            }
            catch (Exception e)
            {
                sysLog.Error("Error when updating user '" + user.Nickname + "'", e);

                return false;
            }

            return true;
        }

        public IUser GetUserByNickname(string nickname, string sessionID)
        {
            try
            {
                sysLog.Info("Request: UserByNickname - Requested User: '" + nickname +"'");

                IUser user = null;

                UserHandler privateUserHandler = HandlerHelper.getPrivateUserHandler(privateXmlRpcUrl, sessionID);
                XmlRpcStruct[] userStruct = privateUserHandler.List("NICKNAME == '" + nickname + "'");
                if (userStruct.Length > 0)
                    user = new User(userStruct[0]);

                return user;
            }
            catch(Exception e)
            {
                sysLog.Error("Error when requesting user", e);

                return null;
            }
        }

        public string ValidateUser(string nickname, string password)
        {
            return publicUserHandler.Validate(nickname, password);
        }

        public List<IUserSecurity> GetUserSecurities(int id, string sessionID)
        {
            try
            {
                sysLog.Info("Request: UserSecurities - Requested UserID: '" + id + "'");

                List<IUserSecurity> userSecurities = new List<IUserSecurity>();

                PortfolioHandler privatePortfolioHandler = HandlerHelper.getPrivatePortfolioHandler(privateXmlRpcUrl, sessionID);
                XmlRpcStruct[] portfolioStruct = privatePortfolioHandler.List("USERID == '" + id + "'");

                foreach (XmlRpcStruct userSecurity in portfolioStruct)
                    userSecurities.Add(new UserSecurity(userSecurity));

                return userSecurities;
            }
            catch (Exception e)
            {
                sysLog.Error("Error when requesting UserSecurities", e);

                return null;
            }
        }

        public List<ITransaction> GetUserTransactions(int id, string sessionID)
        {
            try
            {
                sysLog.Info("Request: UserTransactions - Requested UserID: '" + id + "'");

                List<ITransaction> userTransactions = new List<ITransaction>();

                TransactionHandler privateTransactionHandler = HandlerHelper.getPrivateTransactionHandler(privateXmlRpcUrl, sessionID);
                XmlRpcStruct[] transactionStruct = privateTransactionHandler.List("USERID == '" + id + "'");

                foreach (XmlRpcStruct userTransaction in userTransactions)
                    userTransactions.Add(new Transaction(userTransaction));

                return userTransactions;
            }
            catch (Exception e)
            {
                sysLog.Error("Error when requesting UserTransactions", e);

                return null;
            }
        }

        /**
         * ORDERS
         */

        public void CreateOrder(int userId, string isin, int amount, double price, string type, string sessionID)
        {
            try
            {
                sysLog.Info("Request: CreateOrder - Created Order: USERID=" + userId + " ISIN=" + isin + " AMOUNT=" + amount
                                                                   + " PRICE=" + price + " TYPE=" + type);

                OrderHandler privateOrderHandler = HandlerHelper.getPrivateOrderHandler(privateXmlRpcUrl, sessionID);
                Order order = new Order(-1, userId, isin, amount, price, type, "ACCEPTED", DateTime.Now, DateTime.MinValue, DateTime.MinValue);
                privateOrderHandler.Create(order.toStruct());
            }
            catch (Exception e)
            {
                sysLog.Error("Error when creating Order", e);
            }
        }

        public void CancelOrder(int orderId, string sessionID)
        {
            try
            {
                sysLog.Info("Request: CancelOrder - Requested OrderId: " + orderId);

                OrderHandler privateOrderHandler = HandlerHelper.getPrivateOrderHandler(privateXmlRpcUrl, sessionID);
                privateOrderHandler.Cancel("ID == '" + orderId + "'");
            }
            catch (Exception e)
            {
                sysLog.Error("Error when cancelling Order", e);
            }
        }

        public List<IOrder> GetUserOrders(int id, string sessionID)
        {
            try
            {
                sysLog.Info("Request: UserOrders - Requested User: " + id);

                List<IOrder> userOrders = new List<IOrder>();

                OrderHandler privateOrderHandler = HandlerHelper.getPrivateOrderHandler(privateXmlRpcUrl, sessionID);
                XmlRpcStruct[] orderStruct = privateOrderHandler.List("USERID == '" + id + "'");

                foreach (XmlRpcStruct userOrder in orderStruct)
                    userOrders.Add(new Order(userOrder));

                return userOrders;
            }
            catch (Exception e)
            {
                sysLog.Error("Error when requesting UserOrders", e);

                return null;
            }
        }

        #endregion
    }
}