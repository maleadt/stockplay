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

/// <summary>
/// De DataAccess klasse is verantwoordelijk voor het verbinden met de databank (ADO.NET) en de gewenste gegevens op te
/// halen en om te zetten naar business objects in de C#-applicatie.
/// Deze klasse gebruikt het singleton pattern
/// </summary>
public class DataAccess : IDataAccess
{

    private static DataAccess instance = new DataAccess();

    private DbProviderFactory factory;

    private Dictionary<string, Exchange> exchangeCache;

	private DataAccess()
	{
        factory = DbProviderFactories.GetFactory(ConfigurationManager.ConnectionStrings["OracleDatabaseKapti"].ProviderName);

        exchangeCache = new Dictionary<string, Exchange>();
	}

    public static DataAccess GetInstance()
    {
        return instance;
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

    public List<Security> GetSecuritiesList()
    {
        DbConnection conn = GetConnection();
        List<Security> securitiesList = new List<Security>();
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

    public Security GetSecurityBySymbol(string isin)
    {
        DbConnection conn = GetConnection();
        Security security = null;
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
                security = GenerateSecurity(securityReader);
        }
        catch (Exception e)
        {
            //TODO loggen
            Console.WriteLine(e.Message);
        }
        finally
        {
            if(securityReader != null)
                securityReader.Close();
            conn.Close();
        }

        return security;
    }

    public List<Security> GetSecuritiesFromExchange(string id)
    {
        throw new NotImplementedException();
    }

    public Quote GetLatestQuoteFromSecurity(string isin)
    {
        DbConnection conn = GetConnection();
        Quote quote= null;
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

    public Quote GetQuoteFromSecurity(string isin, DateTime date)
    {
        DbConnection conn = GetConnection();
        Quote quote = null;
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
            if(quoteReader != null)
                quoteReader.Close();
            conn.Close();
        }

        return quote;
    }

    public Exchange getExchangeBySymbol(string symbol)
    {
        Exchange exchange = null;

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
                if(exchangeReader != null)
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

    public List<Exchange> getExchanges()
    {
        DbConnection conn = GetConnection();
        List<Exchange> exchangesList = new List<Exchange>();
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
            if(exchangesReader != null)
                exchangesReader.Close();
            conn.Close();
        }

        return exchangesList;
    }

    private Security GenerateSecurity(DbDataReader securityData)
    {
        string isin, symbol, name, type, exchangeSymbol;
        Exchange exchange;

        isin = securityData.GetString(securityData.GetOrdinal("isin"));
        symbol = securityData.GetString(securityData.GetOrdinal("symbol"));
        name = securityData.GetString(securityData.GetOrdinal("name"));
        //type = securityData.GetString(securityData.GetOrdinal("type")); //type zit nog niet in de databank
        type = "";

        //Exchange object aanmaken
        exchangeSymbol = securityData.GetString(securityData.GetOrdinal("exchange"));
        exchange = getExchangeBySymbol(exchangeSymbol);

        return new Security(isin, symbol, name, type, exchange);
    }

    private Quote GenerateQuote(DbDataReader quoteData)
    {
        DateTime time;
        double price, open, buy, sell, low, high;
        int volume;

        time = quoteData.GetDateTime(1);
        price = quoteData.GetDouble(2);
        volume = quoteData.GetInt32(3);
        open = quoteData.GetDouble(4);
        buy = quoteData.GetDouble(5);
        sell = quoteData.GetDouble(6);
        low = quoteData.GetDouble(7);
        high = quoteData.GetDouble(8);

        return new Quote(time, price, open, volume, buy, sell, low, high);
    }

    private Exchange GenerateExchange(DbDataReader exchangeData)
    {
        string symbol, name, location;

        symbol = exchangeData.GetString(exchangeData.GetOrdinal("symbol"));
        name = exchangeData.GetString(exchangeData.GetOrdinal("name"));
        location = exchangeData.GetString(exchangeData.GetOrdinal("location"));

        return new Exchange(symbol, name, location);
    }

    #endregion
}
