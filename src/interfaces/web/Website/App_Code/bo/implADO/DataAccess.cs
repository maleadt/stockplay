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
using System.Data.Common;
using System.Collections.ObjectModel;
using System.Collections.Generic;
using System.Collections;

namespace implADO
{

    /// <summary>
    /// De DataAccess klasse is verantwoordelijk voor het verbinden met de databank (ADO.NET) en de gewenste gegevens op te
    /// halen en om te zetten naar business objects in de C#-applicatie.
    /// Deze klasse gebruikt het singleton pattern en is beschermd tegen gelijktijdige toegang
    /// </summary>
    public class DataAccess : IDataAccess
    {

        private static DataAccess instance;
        private DbProviderFactory factory;

        private Dictionary<string, IExchange> exchangeCache;

        private static object lockSingleton = new object();

        private DataAccess()
        {
            factory = DbProviderFactories.GetFactory(ConfigurationManager.ConnectionStrings["OracleDatabaseKapti"].ProviderName);

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

        public List<IQuote> GetLatestQuotesFromSecurities(List<ISecurity> securities)
        {
            return new List<IQuote>();
        }

        #region IDataAccess Members

        private DbConnection GetConnection()
        {
            DbConnection conn = factory.CreateConnection();
            conn.ConnectionString = ConfigurationManager.ConnectionStrings["OracleDatabaseKapti"].ConnectionString;
            return conn;
        }

        private DbCommand CreateCommand(string query, DbConnection connection)
        {
            DbCommand command = factory.CreateCommand();
            command.CommandText = query;
            command.Connection = connection;

            return command;
        }

        public List<ISecurity> GetSecuritiesList()
        {
            DbConnection conn = GetConnection();
            List<ISecurity> securitiesList = new List<ISecurity>();
            DbDataReader securitiesReader = null;

            try
            {
                conn.Open();
                DbCommand command = CreateCommand(ConfigurationManager.AppSettings["SELECT_SECURITIES"], conn);
                securitiesReader = command.ExecuteReader();

                while (securitiesReader.Read())
                    securitiesList.Add(GenerateSecurity(securitiesReader));
            }
            catch (Exception e)
            {
                //TODO loggen
                Console.WriteLine(e.Message);
            }
            finally
            {
                if (securitiesReader != null)
                    securitiesReader.Close();
                conn.Close();
            }

            return securitiesList;
        }

        public List<ISecurity> GetSecuritiesList(string searchterm)
        {
            throw new NotImplementedException();
        }

        public List<IQuote> GetQuotesFromSecurity(string isin, DateTime iFrom, DateTime iTo)
        {
            return null;
        }

        public DateTime GetLatestTime(string isin)
        {
            return new System.DateTime();
        }

        public DateTime GetFirstTime(string isin)
        {
            return new System.DateTime();
        }

        public List<ISecurity> GetSecurityByIsin(params string[] isin)
        {
            DbConnection conn = GetConnection();
            List<ISecurity> security = new List<ISecurity>();
            DbDataReader securityReader = null;

            try
            {
                conn.Open();
                DbCommand command = CreateCommand(ConfigurationManager.AppSettings["SELECT_SECURITY"], conn);

                DbParameter paramIsin = factory.CreateParameter();
                paramIsin.ParameterName = ":isin";
                paramIsin.DbType = DbType.String;
                paramIsin.Value = isin;
                command.Parameters.Add(paramIsin);

                securityReader = command.ExecuteReader();

                if (securityReader.Read())
                    security.Add(GenerateSecurity(securityReader));
            }
            catch (Exception e)
            {
                //TODO loggen
                Console.WriteLine(e.Message);
            }
            finally
            {
                if (securityReader != null)
                    securityReader.Close();
                conn.Close();
            }

            return security;
        }

        public List<ISecurity> GetSecuritiesFromExchange(string id)
        {
            throw new NotImplementedException();
        }

        public IQuote GetLatestQuoteFromSecurity(string isin)
        {
            DbConnection conn = GetConnection();
            IQuote quote = null;
            DbDataReader quoteReader = null;

            try
            {
                conn.Open();
                DbCommand command = CreateCommand(ConfigurationManager.AppSettings["SELECT_LATEST_QUOTE_FROM_SECURITY"], conn);

                DbParameter paramSymbol = factory.CreateParameter();
                paramSymbol.ParameterName = ":isin";
                paramSymbol.DbType = DbType.String;
                paramSymbol.Value = isin;
                command.Parameters.Add(paramSymbol);

                quoteReader = command.ExecuteReader();

                if (quoteReader.Read())
                    quote = GenerateQuote(quoteReader);
            }
            catch (Exception e)
            {
                //TODO: Loggen
                Console.WriteLine(e.Message);
            }
            finally
            {
                if (quoteReader != null)
                    quoteReader.Close();
                conn.Close();
            }

            return quote;
        }

        public List<IQuote> GetDailyQuotesFromSecurity(string isin, DateTime minDate, DateTime maxDate)
        {
            DbConnection conn = GetConnection();
            List<IQuote> quotes = new List<IQuote>();
            DbDataReader quoteReader = null;

            try
            {
                conn.Open();
                DbCommand command = CreateCommand(ConfigurationManager.AppSettings["SELECT_DAILY_QUOTES_FROM_SECURITY"], conn);

                DbParameter paramIsin = factory.CreateParameter();
                paramIsin.ParameterName = ":isin";
                paramIsin.DbType = DbType.String;
                paramIsin.Value = isin;
                DbParameter paramMinDate = factory.CreateParameter();
                paramMinDate.ParameterName = ":minDate";
                paramMinDate.DbType = DbType.DateTime;
                paramMinDate.Value = minDate;
                DbParameter paramMaxDate = factory.CreateParameter();
                paramMaxDate.ParameterName = ":maxDate";
                paramMaxDate.DbType = DbType.DateTime;
                paramMaxDate.Value = maxDate;

                command.Parameters.Add(paramIsin);
                command.Parameters.Add(paramMinDate);
                command.Parameters.Add(paramMaxDate);

                quoteReader = command.ExecuteReader();

                while (quoteReader.Read())
                    quotes.Add(GenerateQuote(quoteReader));
            }
            catch (Exception e)
            {
                //TODO: Loggen
                Console.WriteLine(e.Message);
            }
            finally
            {
                if (quoteReader != null)
                    quoteReader.Close();
                conn.Close();
            }

            return quotes;
        }

        public IQuote GetQuoteFromSecurity(string isin, DateTime date)
        {
            DbConnection conn = GetConnection();
            IQuote quote = null;
            DbDataReader quoteReader = null;

            try
            {
                conn.Open();
                DbCommand command = CreateCommand(ConfigurationManager.AppSettings["SELECT_QUOTE_FROM_SECURITY"], conn);

                DbParameter paramIsin = factory.CreateParameter();
                paramIsin.ParameterName = ":isin";
                paramIsin.DbType = DbType.String;
                paramIsin.Value = isin;
                DbParameter paramDate = factory.CreateParameter();
                paramDate.ParameterName = ":quotedate";
                paramDate.DbType = DbType.DateTime;
                paramDate.Value = date;

                command.Parameters.Add(paramIsin);
                command.Parameters.Add(paramDate);

                quoteReader = command.ExecuteReader();

                if (quoteReader.Read())
                    quote = GenerateQuote(quoteReader);
            }
            catch (Exception e)
            {
                //TODO: Loggen
                Console.WriteLine(e.Message);
            }
            finally
            {
                if (quoteReader != null)
                    quoteReader.Close();
                conn.Close();
            }

            return quote;
        }

        public IExchange GetExchangeBySymbol(string symbol)
        {
            IExchange exchange = null;

            //Indien de exchange nog niet in de cache zit halen we hem op
            if (!exchangeCache.ContainsKey(symbol))
            {
                DbConnection conn = GetConnection();
                DbDataReader exchangeReader = null;

                try
                {
                    conn.Open();
                    DbCommand command = CreateCommand(ConfigurationManager.AppSettings["SELECT_EXCHANGE"], conn);

                    DbParameter paramSymbol = factory.CreateParameter();
                    paramSymbol.ParameterName = ":symbol";
                    paramSymbol.DbType = DbType.String;
                    paramSymbol.Value = symbol;
                    command.Parameters.Add(paramSymbol);

                    exchangeReader = command.ExecuteReader();
                    if (exchangeReader.Read())
                        exchange = GenerateExchange(exchangeReader);

                    exchangeCache[symbol] = exchange;
                }
                catch (Exception e)
                {
                    //TODO: Loggen
                    Console.WriteLine(e.Message);
                }
                finally
                {
                    if (exchangeReader != null)
                        exchangeReader.Close();
                    conn.Close();
                }
            }
            else
            {
                exchange = exchangeCache[symbol];
            }

            return exchange;
        }

        public List<IExchange> GetExchanges()
        {
            DbConnection conn = GetConnection();
            List<IExchange> exchangesList = new List<IExchange>();
            DbDataReader exchangesReader = null;

            try
            {
                conn.Open();
                DbCommand command = CreateCommand(ConfigurationManager.AppSettings["SELECT_EXCHANGES"], conn);
                exchangesReader = command.ExecuteReader();

                while (exchangesReader.Read())
                    exchangesList.Add(GenerateExchange(exchangesReader));
            }
            catch (Exception e)
            {
                //TODO: Loggen
            }
            finally
            {
                if (exchangesReader != null)
                    exchangesReader.Close();
                conn.Close();
            }

            return exchangesList;
        }

        public void CreateUser(int id, string nickname, string password, string email, bool isAdmin, string lastname, string firstname, DateTime regTime,
                        long rrn, int points, double startAmount, double cash)
        {
            throw new NotSupportedException();
        }

        public bool RemoveUser(string nickname)
        {
            throw new NotSupportedException();
        }

        public bool UpdateUser(IUser user)
        {
            throw new NotSupportedException();
        }

        public IUser GetUserByNickname(string nickname)
        {
            throw new NotSupportedException();
        }

        public bool ValidateUser(string nickname, string password)
        {
            throw new NotSupportedException();
        }

        public List<IUserSecurity> GetUserSecurities(int id)
        {
            throw new NotSupportedException();
        }

        public List<ITransaction> GetUserTransactions(int id)
        {
            throw new NotSupportedException();
        }

        public void CreateOrder(int userId, string isin, int amount, double price, string type)
        {
            throw new NotSupportedException();
        }

        public List<IOrder> GetUserOrders(int id)
        {
            throw new NotSupportedException();
        }

        public void CancelOrder(int orderId)
        {
            throw new NotSupportedException();
        }

        private ISecurity GenerateSecurity(DbDataReader securityData)
        {
            string isin, symbol, name, exchangeSymbol;
            bool visible, suspended;
            IExchange exchange;

            isin = securityData.GetString(securityData.GetOrdinal("isin"));
            symbol = securityData.GetString(securityData.GetOrdinal("symbol"));
            name = securityData.GetString(securityData.GetOrdinal("name"));
            visible = securityData.GetInt32(securityData.GetOrdinal("visible")) == 1 ? true : false;
            suspended = securityData.GetInt32(securityData.GetOrdinal("suspended")) == 1 ? true : false;

            //Exchange object aanmaken
            exchangeSymbol = securityData.GetString(securityData.GetOrdinal("exchange"));
            exchange = GetExchangeBySymbol(exchangeSymbol);

            //Bij de Overview pagina wordt ook nog de informatie van de laatste quote toegevoegd
            if (securityData.FieldCount > 7)
            {
                IQuote quote = GenerateQuote(securityData);
                return new Security(isin, symbol, name, visible, suspended, exchange, quote);
            }

            return new Security(isin, symbol, name, visible, suspended, exchange);
        }

        private IQuote GenerateQuote(DbDataReader quoteData)
        {
            DateTime time;
            double price, open, buy, sell, low, high;
            int volume;

            time = quoteData.GetDateTime(quoteData.GetOrdinal("timestamp"));
            price = quoteData.GetDouble(quoteData.GetOrdinal("price"));
            volume = quoteData.GetInt32(quoteData.GetOrdinal("volume"));
            open = quoteData.GetDouble(quoteData.GetOrdinal("open"));
            buy = quoteData.GetDouble(quoteData.GetOrdinal("bid"));
            sell = quoteData.GetDouble(quoteData.GetOrdinal("ask"));
            low = quoteData.GetDouble(quoteData.GetOrdinal("low"));
            high = quoteData.GetDouble(quoteData.GetOrdinal("high"));

            return new Quote(time, price, open, volume, buy, sell, low, high);
        }

        private IExchange GenerateExchange(DbDataReader exchangeData)
        {
            string symbol, name, location;

            symbol = exchangeData.GetString(exchangeData.GetOrdinal("symbol"));
            name = exchangeData.GetString(exchangeData.GetOrdinal("name"));
            location = exchangeData.GetString(exchangeData.GetOrdinal("location"));

            return new Exchange(symbol, name, location);
        }

        #endregion
    }

}
