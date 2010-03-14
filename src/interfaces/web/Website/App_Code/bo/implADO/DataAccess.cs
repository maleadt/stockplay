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

    private DbProviderFactory factory;

	public DataAccess()
	{

        factory = DbProviderFactories.GetFactory(ConfigurationManager.ConnectionStrings["OracleDatabase"].ProviderName);
	}

    #region IDataAccess Members

    private DbConnection GetConnection()
    {
        DbConnection conn = factory.CreateConnection();
        conn.ConnectionString = ConfigurationManager.ConnectionStrings["OracleDatabase"].ConnectionString;
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
        return null;
    }

    public Security GetSecurityBySymbol(string symbol)
    {
        throw new NotImplementedException();
    }

    public List<Security> GetSecuritiesFromExchange(string id)
    {
        throw new NotImplementedException();
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

    public Exchange getExchangeById(string id)
    {
        throw new NotImplementedException();
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
