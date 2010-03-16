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
/// </summary>
public class DataAccess : IDataAccess
{

    private static DataAccess instance = new DataAccess();

    private DbProviderFactory factory;

	private DataAccess()
	{
        factory = DbProviderFactories.GetFactory(ConfigurationManager.ConnectionStrings["OracleDatabaseKapti"].ProviderName);
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
        conn.Open();

        List<Security> securitiesList = new List<Security>();

        try
        {
            DbCommand command = CreateCommand(ConfigurationManager.AppSettings["SELECT_SECURITIES"], conn);
            DbDataReader securities = command.ExecuteReader();

            string symbol, name, type, exchangeSymbol;
            Exchange exchange;

            while (securities.Read())
            {
                symbol = securities.GetString(0);
                name = securities.GetString(1);
                //type = securities.GetString(2);   //Type zit momenteel nog niet in de databank

                //Exchange object aanmaken
                exchangeSymbol = securities.GetString(2);
                exchange = getExchangeBySymbol(exchangeSymbol);

                securitiesList.Add(new Security(symbol, name, "", exchange));
            }
        }
        catch (Exception e)
        {
            //TODO loggen
            Console.WriteLine(e.Message);
        }
        finally
        {
            conn.Close();
        }

        return securitiesList;
    }

    public Security GetSecurityBySymbol(string symbol)
    {
        throw new NotImplementedException();
    }

    public List<Security> GetSecuritiesFromExchange(string id)
    {
        throw new NotImplementedException();
    }

    public Quote GetLatestQuoteFromSecurity(string symbol)
    {
        DbConnection conn = GetConnection();
        conn.Open();

        Quote quote= null;

        try
        {
            DbCommand command = CreateCommand(ConfigurationManager.AppSettings["SELECT_LATEST_QUOTE_FROM_SECURITY"], conn);

            DbParameter paramSymbol = factory.CreateParameter();
            paramSymbol.ParameterName = ":symbol";
            paramSymbol.DbType = DbType.String;
            paramSymbol.Value = symbol;

            command.Parameters.Add(paramSymbol);


            DbDataReader quoteReader = command.ExecuteReader();

            DateTime time;
            double price, open, buy, sell, low, high;
            int volume;

            quoteReader.Read();

            time = quoteReader.GetDateTime(1);
            price = quoteReader.GetDouble(2);
            open = quoteReader.GetDouble(3);
            volume = quoteReader.GetInt32(4);
            buy = quoteReader.GetDouble(5);
            sell = quoteReader.GetDouble(6);
            low = quoteReader.GetDouble(7);
            high = quoteReader.GetDouble(8);

            quote = new Quote(time, price, open, volume, buy, sell, low, high);
        }
        catch (Exception e)
        {
            //TODO: Loggen
            Console.WriteLine(e.Message);
        }
        finally
        {
            conn.Close();
        }

        return quote;
    }

    public Quote GetQuoteFromSecurity(string symbol, DateTime time)
    {
        throw new NotImplementedException();
    }

    public List<Quote> GetQuotesFromSecurity(string symbol)
    {
        throw new NotImplementedException();
    }

    public List<Quote> GetQuotesIntervalFromSecurity(string symbol, DateTime start, DateTime end)
    {
        throw new NotImplementedException();
    }

    public Exchange getExchangeBySymbol(string symbol)
    {
        DbConnection conn = GetConnection();
        conn.Open();

        Exchange exchange = null;

        try
        {
            DbCommand command = CreateCommand(ConfigurationManager.AppSettings["SELECT_EXCHANGE"], conn);
            
            DbParameter paramSymbol = factory.CreateParameter();
            paramSymbol.ParameterName = ":symbol";
            paramSymbol.DbType = DbType.String;
            paramSymbol.Value = symbol;

            command.Parameters.Add(paramSymbol);

            DbDataReader exchangeReader = command.ExecuteReader();

            string name, location;

            exchangeReader.Read();

            name = exchangeReader.GetString(1);
            location = exchangeReader.GetString(2);

            exchange = new Exchange(symbol, name, location);
        }
        catch (Exception e)
        {
            //TODO: Loggen
            Console.WriteLine(e.Message);
        }
        finally
        {
            conn.Close();
        }

        return exchange;
    }

    public List<Exchange> getExchanges()
    {
        DbConnection conn = GetConnection();
        conn.Open();

        List<Exchange> exchangesList = new List<Exchange>();

        try
        {
            DbCommand command = CreateCommand(ConfigurationManager.AppSettings["SELECT_EXCHANGES"], conn);
            DbDataReader exchanges = command.ExecuteReader();

            string symbol, name, location;

            while (exchanges.Read())
            {
                symbol = exchanges.GetString(0);
                name = exchanges.GetString(1);
                location = exchanges.GetString(2);

                exchangesList.Add(new Exchange(symbol, name, location));
            }
        }
        catch (Exception e)
        {
            //TODO: Loggen
        }
        finally
        {
            conn.Close();
        }

        return exchangesList;
    }

    #endregion
}
