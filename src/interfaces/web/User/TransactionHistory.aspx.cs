using System;
using System.Collections;
using System.Configuration;
using System.Data;
using System.Linq;
using System.Web;
using System.Web.Security;
using System.Web.UI;
using System.Web.UI.HtmlControls;
using System.Web.UI.WebControls;
using System.Web.UI.WebControls.WebParts;
using System.Xml.Linq;
using System.Collections.Generic;
using StockPlay;

namespace StockPlay.Web
	{
	public partial class User_TransactionHistory : System.Web.UI.Page
	{
	    protected void Page_Load(object sender, EventArgs e)
	    {
	        if (!IsPostBack)
	        {
	            IDataAccess data = DataAccessFactory.GetDataAccess();
	            TransactionsGridview.DataSource = GenerateDataTable(data.GetUserTransactions(((StockplayMembershipUser)Membership.GetUser(User.Identity.Name)).ID));
	        }
	    }
	
	    private DataTable GenerateDataTable(List<ITransaction> transactions)
	    {
	        DataTable transactionsTable = new DataTable("Securities");
	
	        transactionsTable.Columns.Add("ID");
	        transactionsTable.Columns["ID"].DataType = typeof(int);
	        transactionsTable.Columns.Add("Isin");
	        transactionsTable.Columns["Isin"].DataType = typeof(string);
	        transactionsTable.Columns.Add("Timestamp");
	        transactionsTable.Columns["Timestamp"].DataType = typeof(DateTime);
	        transactionsTable.Columns.Add("Type");
	        transactionsTable.Columns["Type"].DataType = typeof(string);
	        transactionsTable.Columns.Add("Amount");
	        transactionsTable.Columns["Amount"].DataType = typeof(double);
	
	        foreach (ITransaction transaction in transactions)
	        {
	            DataRow row = transactionsTable.NewRow();
	            row[0] = transaction.ID;
	            row[1] = transaction.Isin;
	            row[2] = transaction.Timestamp;
	            row[3] = transaction.Type;
	            row[4] = transaction.Amount;
	
	            transactionsTable.Rows.Add(row);
	        }
	
	        return transactionsTable;
	    }
	}
}