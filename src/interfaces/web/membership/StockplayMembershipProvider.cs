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

/// <summary>
/// Summary description for StockplayMembershipProvider
/// </summary>
namespace StockPlay
{
	public class StockplayMembershipProvider : System.Web.Security.MembershipProvider
	{
	
	    public override bool ChangePassword(string nickname, string oldPassword, string newPassword)
	    {
	        IDataAccess data = DataAccessFactory.GetDataAccess();
	
            string sessionID = data.ValidateUser(nickname, oldPassword);

            //Indien oud paswoord incorrect
	        if( ! sessionID.Equals(""))
	            return false;
	
	        try
	        {
                IUser user = data.GetUserByNickname(nickname, sessionID);
	            user.Password = newPassword;
	            data.UpdateUser(user, sessionID);
	        }
	        catch(Exception e)
	        {
	            return false;
	        }
	
	        return true;
	    }
	
	    public void CreateUser(string nickname, string password, string email, string lastname, string firstname, long rrn, out MembershipCreateStatus status)
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
	    }
	
        public bool DeleteUser(string nickname, string sessionID)
        {
            IDataAccess data = DataAccessFactory.GetDataAccess();
            return data.RemoveUser(nickname, sessionID);
        }
	
	    public override bool EnablePasswordReset
	    {
	        get { return true; }
	    }
	
	    public override bool EnablePasswordRetrieval
	    {
	        get { return false; }
	    }
	
	    public MembershipUser GetUser(string nickname, string sessionID)
	    {
	        IDataAccess data = DataAccessFactory.GetDataAccess();
	        IUser user = data.GetUserByNickname(nickname, sessionID);
	
	        StockplayMembershipUser membershipUser = null;
	        if(user != null)
	            membershipUser = new StockplayMembershipUser("StockplayMembershipProvider", nickname, null, user.Email,
	                                                            null, null, true, false, user.Regdate, DateTime.Now, DateTime.Now,
	                                                            DateTime.Now, DateTime.Now, user.ID, user.Lastname, user.Firstname,
	                                                            user.IsAdmin, user.Points, user.StartAmount, user.Cash, user.RRN);
	
	        return membershipUser;
	    }
	
	    public override bool RequiresQuestionAndAnswer
	    {
	        get { return false; }
	    }
	
	    public override bool RequiresUniqueEmail
	    {
	        get { return true; }
	    }
	
	    public override string ResetPassword(string username, string answer)
	    {
            //TODO - Deze functie aanmaken in de backend aangezien je nu eerst geauthenticeerd moet zijn
            throw new NotSupportedException();
            //IDataAccess data = DataAccessFactory.GetDataAccess();
            //IUser user = data.GetUserByNickname(username);
	
            //string newPassword = System.Guid.NewGuid().ToString();
	
            //user.Password = newPassword;
            //data.UpdateUser(user);
	
            //return newPassword;
	    }
	
	    public override bool UnlockUser(string userName)
	    {
	        throw new NotImplementedException();
	    }
	
	    public void UpdateUser(MembershipUser user, string sessionID)
	    {
	        IDataAccess data = DataAccessFactory.GetDataAccess();
	        try
	        {
	            IUser userData = data.GetUserByNickname(user.UserName, sessionID);
	            StockplayMembershipUser userMembership = (StockplayMembershipUser) user;
	
	            userData.Nickname = userMembership.UserName;
	            userData.Lastname = userMembership.Lastname;
	            userData.Firstname = userMembership.Firstname;
	            userData.Email = userMembership.Email;
	            userData.RRN = userMembership.RRN;
	
	            data.UpdateUser(userData, sessionID);
	        }
	        catch (Exception e)
	        {
	            //TODO Loggen
	        }
	    }


        
        //De gebruiker wordt ingelogd en krijgt een sessionID terug. Indien deze sessionID
        //een lege string is betekent dit dat het inloggen mislukt is. De sessionID wordt
        //voor de rest van de sessie bewaard en wordt bij iedere request die validatie vereist
        //meegestuurd met die request.
        public string ValidateUserSession(string nickname, string password)
        {
            IDataAccess data = DataAccessFactory.GetDataAccess();
            try
            {
                return data.ValidateUser(nickname, password);
            }
            catch (Exception e)
            {
                return "";
            }
        }


        /*
         * 
         * Al deze methodes zijn niet bruikbaar omdat de Membershipprovider niet toelaat om
         * op eenvoudige wijze extra parameters toelaat bij de verschillende methodes. Omdat
         * onze applicatie voor de meeste dingen een sessionID nodig heeft kunnen we dus geen
         * implementatie voorzien voor de meeste van deze methodes.         
         *  
         */

        public override bool ValidateUser(string nickname, string password)
        {
            throw new NotSupportedException();
        }

        public override MembershipUser GetUser(object providerUserKey, bool userIsOnline)
        {
            throw new NotImplementedException();
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

        public override MembershipUser GetUser(string nickname, bool userIsOnline)
        {
            throw new NotSupportedException();
        }

        public override int GetNumberOfUsersOnline()
        {
            throw new NotImplementedException();
        }

        public override string GetPassword(string username, string answer)
        {
            throw new NotImplementedException();
        }

        public override bool DeleteUser(string nickname, bool deleteAllRelatedData)
        {
            throw new NotImplementedException();
        }

        public override bool ChangePasswordQuestionAndAnswer(string nickname, string password, string newPasswordQuestion, string newPasswordAnswer)
        {
            throw new NotImplementedException();
        }

        public override MembershipUser CreateUser(string nickname, string password, string email, string passwordQuestion, string passwordAnswer, bool isApproved, object providerUserKey, out MembershipCreateStatus status)
        {
            throw new NotImplementedException();
        }

        public override void UpdateUser(MembershipUser user)
        {
            throw new NotSupportedException();
        }


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


        public override string PasswordStrengthRegularExpression
        {
            get { throw new NotImplementedException(); }
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
	}
}