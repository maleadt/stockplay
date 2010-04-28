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
using log4net;
using log4net.Config;
using System.Text;
using StockPlay;

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


        //
        // Constructie
        //

        private DataAccess()
        {
            XmlConfigurator.Configure();
            sysLog.Info("DataAccess startup");

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
                    querySecurities = securityHandler.List();
                else
                    querySecurities = securityHandler.List("ISIN =~ '" + searchterm + "'ri || NAME =~ '" + searchterm
                                                            + "'ri || EXCHANGE =~ '" + searchterm
                                                            + "'ri || SYMBOL =~ '" + searchterm + "'ri");

                XmlRpcStruct[] queryQuotes = securityHandler.LatestQuotes("");

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
                XmlRpcStruct[] query = securityHandler.List(parameters.ToString());
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

                XmlRpcStruct[] query = securityHandler.List("EXCHANGE == '" + id + "'");
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

                XmlRpcStruct[] query = securityHandler.LatestQuotes("ISIN == '" + isin + "'");
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

                sysLog.Info("Request: 'GetLatestQuotesFromSecurities' - Requested Securities: '" + isins.ToArray() +"'");

                XmlRpcStruct[] queries = securityHandler.LatestQuotes(string.Join(" || ", isins.ToArray()));
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

                XmlRpcStruct[] query = securityHandler.Quotes("ISIN == '" + isin + "' && TIMESTAMP <= '" + to + "'d && TIMESTAMP > '" + from + "'d");
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

                DateTime time = securityHandler.getLatestTime(isin);
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

                DateTime time = securityHandler.getFirstTime(isin);
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
                sysLog.Info("Request: 'GetExchangeBySymbol' - Requested Exchange: '" + symbol + "'");

                IExchange exchange = null;

                if (exchangeCache.ContainsKey(symbol))
                    exchange = exchangeCache[symbol];
                else
                {
                    XmlRpcStruct[] query = exchangeHandler.List("SYMBOL == '" + symbol + "'");
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

                XmlRpcStruct[] query = exchangeHandler.List();
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
                userHandler.Create(userStruct);
            }
            catch (Exception e)
            {
                sysLog.Error("Eror when creating user: ", e);

                throw e; //Verderwerpen naar Membershipklasse
            }
        }

        public bool RemoveUser(string nickname)
        {
            try
            {
                sysLog.Info("Request: RemoveUser - Requested User: '" + nickname + "'");

                userHandler.Remove("NICKNAME == '" + nickname + "'");
            }
            catch (Exception e)
            {
                sysLog.Error("Error when removing user '" + nickname + "'", e);

                return false;
            }
            return false;
        }

        public bool UpdateUser(IUser user)
        {
            try
            {
                XmlRpcStruct userStruct = ((User)user).toStruct();

                sysLog.Info("Request: UpdateUser - Requested User: '" + user + "', Userdata: FIRSTNAME=" + user.Firstname
                                                                                   + " LASTNAME=" + user.Lastname
                                                                                   + " EMAIL=" + user.Email);

                userHandler.Modify("NICKNAME == '" + user.Nickname + "'", userStruct);
            }
            catch (Exception e)
            {
                sysLog.Error("Error when updating user '" + user.Nickname + "'", e);

                return false;
            }

            return true;
        }

        public IUser GetUserByNickname(string nickname)
        {
            try
            {
                sysLog.Info("Request: UserByNickname - Requested User: '" + nickname +"'");

                IUser user = null;

                XmlRpcStruct[] userStruct = userHandler.List("NICKNAME == '" + nickname + "'");
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

        public bool ValidateUser(string nickname, string password)
        {
            return userHandler.Validate(nickname, password);
        }

        public List<IUserSecurity> GetUserSecurities(int id)
        {
            try
            {
                sysLog.Info("Request: UserSecurities - Requested UserID: '" + id + "'");

                List<IUserSecurity> userSecurities = new List<IUserSecurity>();

                XmlRpcStruct[] portfolioStruct = portfolioHandler.List("USERID == '" + id + "'");

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

        public List<ITransaction> GetUserTransactions(int id)
        {
            try
            {
                sysLog.Info("Request: UserTransactions - Requested UserID: '" + id + "'");

                List<ITransaction> userTransactions = new List<ITransaction>();

                XmlRpcStruct[] transactionStruct = transactionHandler.List("USERID == '" + id + "'");

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

        public void CreateOrder(int userId, string isin, int amount, double price, string type)
        {
            try
            {
                sysLog.Info("Request: CreateOrder - Created Order: USERID=" + userId + " ISIN=" + isin + " AMOUNT=" + amount
                                                                   + " PRICE=" + price + " TYPE=" + type);

                Order order = new Order(-1, userId, isin, amount, price, type, "ACCEPTED", DateTime.Now, DateTime.MinValue, DateTime.MinValue);
                orderHandler.Create(order.toStruct());
            }
            catch (Exception e)
            {
                sysLog.Error("Error when creating Order", e);
            }
        }

        public List<IOrder> GetUserOrders(int id)
        {
            try
            {
                sysLog.Info("Request: UserOrders - Requested User: " + id);

                List<IOrder> userOrders = new List<IOrder>();

                XmlRpcStruct[] orderStruct = orderHandler.List("USERID == '" + id + "'");

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

        public void CancelOrder(int orderId)
        {
            try
            {
                sysLog.Info("Request: CancelOrder - Requested OrderId: " + orderId);

                orderHandler.Cancel("ID == '" + orderId + "'");
            }
            catch (Exception e)
            {
                sysLog.Error("Error when cancelling Order", e);
            }
        }

        #endregion
    }
}