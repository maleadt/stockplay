using System;
using System.Collections.ObjectModel;
using System.Collections.Generic;
public interface IDataAccess
{
    //Securities
    List<ISecurity> GetSecuritiesList();
    ISecurity GetSecurityByIsin(string isin);
    List<ISecurity> GetSecuritiesFromExchange(string id);

    //Quotes
    IQuote GetLatestQuoteFromSecurity(string isin);
    IQuote GetQuoteFromSecurity(string isin, DateTime time);
    List<IQuote> GetDailyQuotesFromSecurity(string isin, DateTime minDate, DateTime maxDate);

    //Exhanges
    IExchange GetExchangeBySymbol(string symbol);
    List<IExchange> GetExchanges();

    //Users
    void CreateUser(int id, string nickname, string password, string email, bool isAdmin, string lastname, string firstname, DateTime regDate,
                    long rrn, int points, double startAmount, double cash);
    bool RemoveUser(string nickname);
    bool UpdateUser(IUser user);
    IUser GetUserByNickname(string nickname);
    bool ValidateUser(string nickname, string password);
    List<IUserSecurity> GetUserSecurities(int id);

    //Transactions
    List<ITransaction> GetUserTransactions(int id);

    //Orders
    void CreateOrder(int userId, string isin, int amount, double price, string type);
    void CancelOrder(int orderId);
    List<IOrder> GetUserOrders(int id);
}
