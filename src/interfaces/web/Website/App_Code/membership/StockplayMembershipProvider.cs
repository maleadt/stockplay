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

/// <summary>
/// Summary description for StockplayMembershipProvider
/// </summary>
public class StockplayMembershipProvider : System.Web.Security.MembershipProvider
{
    public override string ApplicationName
    {
        get
        {
            throw new NotImplementedException();
        }
        set
        {
            throw new NotImplementedException();
        }
    }

    // TODO - Methode maken in databank die het paswoord van de gebruiker verandert, zodat de website niet overal een paswoordveld
    // moet bijhouden dat voor de rest van de site onnodig is!
    public override bool ChangePassword(string nickname, string oldPassword, string newPassword)
    {
        IDataAccess data = DataAccessFactory.GetDataAccess();
        IUser user = data.GetUserByNickname(nickname);
        if(data.ValidateUser(nickname, oldPassword))
            user.Password = newPassword;
        else
            return false; //Indien oud paswoord incorrect

        try
        {
            user.Password = newPassword;
            data.UpdateUser(user);
        }
        catch(Exception e)
        {
            return false;
        }

        return true;
    }

    public override bool ChangePasswordQuestionAndAnswer(string nickname, string password, string newPasswordQuestion, string newPasswordAnswer)
    {
        throw new NotImplementedException();
    }

    public override MembershipUser CreateUser(string nickname, string password, string email, string passwordQuestion, string passwordAnswer, bool isApproved, object providerUserKey, out MembershipCreateStatus status)
    {
        throw new NotImplementedException();
    }

    public StockplayMembershipUser CreateUser(string nickname, string password, string email, string lastname, string firstname, long rrn, out MembershipCreateStatus status)
    {
        IDataAccess data = DataAccessFactory.GetDataAccess();
        try
        {
            data.CreateUser(-1, nickname, password, email, false, lastname, firstname, DateTime.Now, rrn, -1, -1, -1);
            status = MembershipCreateStatus.Success;
        }
        catch (Exception e)
        {
            status = MembershipCreateStatus.ProviderError;
        }

        return (StockplayMembershipUser) Membership.GetUser(nickname);
    }

    public override bool DeleteUser(string nickname, bool deleteAllRelatedData)
    {
        IDataAccess data = DataAccessFactory.GetDataAccess();
        return data.RemoveUser(nickname);
    }

    public override bool EnablePasswordReset
    {
        get { return true; }
    }

    public override bool EnablePasswordRetrieval
    {
        get { return false; }
    }

    public override MembershipUserCollection FindUsersByEmail(string emailToMatch, int pageIndex, int pageSize, out int totalRecords)
    {
        throw new NotImplementedException();
    }

    public override MembershipUserCollection FindUsersByName(string usernameToMatch, int pageIndex, int pageSize, out int totalRecords)
    {
        throw new NotImplementedException();
    }

    public override MembershipUserCollection GetAllUsers(int pageIndex, int pageSize, out int totalRecords)
    {
        throw new NotImplementedException();
    }

    public override int GetNumberOfUsersOnline()
    {
        throw new NotImplementedException();
    }

    public override string GetPassword(string username, string answer)
    {
        throw new NotImplementedException();
    }

    public override MembershipUser GetUser(string nickname, bool userIsOnline)
    {
        IDataAccess data = DataAccessFactory.GetDataAccess();
        IUser user = data.GetUserByNickname(nickname);

        StockplayMembershipUser membershipUser = null;
        if(user != null)
            membershipUser = new StockplayMembershipUser("StockplayMembershipProvider", nickname, null, user.Email, null, null, true, false, user.Regdate, DateTime.Now, DateTime.Now, DateTime.Now, DateTime.Now, user.ID, user.Lastname, user.Firstname, user.IsAdmin, user.Points, user.StartAmount, user.Cash, user.RRN);

        return membershipUser;
    }

    public override MembershipUser GetUser(object providerUserKey, bool userIsOnline)
    {
        throw new NotImplementedException();
    }

    public override string GetUserNameByEmail(string email)
    {
        throw new NotImplementedException();
    }

    public override int MaxInvalidPasswordAttempts
    {
        get { throw new NotImplementedException(); }
    }

    public override int MinRequiredNonAlphanumericCharacters
    {
        get { throw new NotImplementedException(); }
    }

    public override int MinRequiredPasswordLength
    {
        get { throw new NotImplementedException(); }
    }

    public override int PasswordAttemptWindow
    {
        get { throw new NotImplementedException(); }
    }

    public override MembershipPasswordFormat PasswordFormat
    {
        get { throw new NotImplementedException(); }
    }

    public override string PasswordStrengthRegularExpression
    {
        get { throw new NotImplementedException(); }
    }

    public override bool RequiresQuestionAndAnswer
    {
        get { return false; }
    }

    public override bool RequiresUniqueEmail
    {
        get { return true; }
    }

    // TODO Methode in backend voorzien om wachtwoord te wijzigen!
    public override string ResetPassword(string username, string answer)
    {
        IDataAccess data = DataAccessFactory.GetDataAccess();
        IUser user = data.GetUserByNickname(username);

        string newPassword = System.Guid.NewGuid().ToString();

        user.Password = newPassword;
        data.UpdateUser(user);

        return newPassword;
    }

    public override bool UnlockUser(string userName)
    {
        throw new NotImplementedException();
    }

    public override void UpdateUser(MembershipUser user)
    {
        IDataAccess data = DataAccessFactory.GetDataAccess();
        try
        {
            IUser userData = data.GetUserByNickname(user.UserName);
            StockplayMembershipUser userMembership = (StockplayMembershipUser) user;

            userData.Nickname = userMembership.UserName;
            userData.Lastname = userMembership.Lastname;
            userData.Firstname = userMembership.Firstname;
            userData.Email = userMembership.Email;
            userData.RRN = userMembership.RRN;

            data.UpdateUser(userData);
        }
        catch (Exception e)
        {
            //TODO Exceptie verderwerpen?
        }
    }

    public override bool ValidateUser(string nickname, string password)
    {
        IDataAccess data = DataAccessFactory.GetDataAccess();
        try
        {
            return data.ValidateUser(nickname, password);
        }
        catch (Exception e)
        {
            return false;
        } 
    }
}
