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
/// Summary description for StockplayMembershipUser
/// </summary>
namespace StockPlay
{
	public class StockplayMembershipUser : MembershipUser
	{
	    private int id;
	    private string lastname;
	    private string firstname;
	    private bool isAdmin;
	    private int points;
	    private double startAmount;
	    private double cash;
	    private long rrn;
	
	    public StockplayMembershipUser(string providername, string username, object providerUserKey, string email, string passwordQuestion,
	                              string comment, bool isApproved, bool isLockedOut, DateTime creationDate, DateTime lastLoginDate,
	                              DateTime lastActivityDate, DateTime lastPasswordChangedDate, DateTime lastLockedOutDate, int id, string lastname, string firstname,
	                              bool isAdmin, int points, double startAmount, double cash, long rrn)
	         : base(providername, username, providerUserKey, email, passwordQuestion, comment, isApproved, isLockedOut,
	                creationDate, lastLoginDate, lastActivityDate, lastPasswordChangedDate,lastLockedOutDate)
	    {
	        this.id = id;
	        this.lastname = lastname;
	        this.firstname = firstname;
	        this.isAdmin = isAdmin;
	        this.points = points;
	        this.startAmount = startAmount;
	        this.cash = cash;
	        this.rrn = rrn;
	    }
	
	    public int ID
	    {
	        get { return id; }
	    }
	
	    public string Lastname
	    {
	        get { return lastname; }
	        set { lastname = value; }
	    }
	
	    public string Firstname
	    {
	        get { return firstname; }
	        set { firstname = value; }
	    }
	
	    public bool IsAdmin
	    {
	        get { return isAdmin; }
	    }
	
	    public int Points
	    {
	        get { return points; }
	    }
	
	    public double StartAmount
	    {
	        get { return startAmount; }
	    }
	
	    public double Cash
	    {
	        get { return cash; }
	    }
	
	    public long RRN
	    {
	        get { return rrn; }
	    }
	}
}