using System;
using System.Collections.Generic;

public interface IUserSecurity
{
    int Amount { get; }
    string Isin { get; }
    int UserID { get; }
}

