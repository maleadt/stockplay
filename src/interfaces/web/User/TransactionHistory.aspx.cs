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
using System.Collections.Generic;
using StockPlay;

namespace StockPlay.Web
	{
	public partial class User_TransactionHistory : MulticulturalPage
	{
	    protected void Page_Load(object sender, EventArgs e)
	    {
	        if (!IsPostBack)
	        {
	            IDataAccess data = DataAccessFactory.GetDataAccess();
	            TransactionsGridview.DataSource =
                    GenerateDataTable(data.GetUserTransactions((int)Session["userID"], (string)Session["sessionID"], (ISession)this.Master));
                TransactionsGridview.DataBind();
	        }
	    }
	
	    private DataTable GenerateDataTable(List<ITransaction> transactions)
	    {
            //Bijbehorende securities opvragen
            IDataAccess data = DataAccessFactory.GetDataAccess();

            List<string> isins = new List<string>();
            foreach (ITransaction transaction in transactions)
                isins.Add(transaction.Isin);

            List<ISecurity> securities = data.GetSecurityByIsin(isins.ToArray());

            //Securities in een woordenboek steken om snel te kunnen opvragen
            Dictionary<string, ISecurity> securityDictionary = new Dictionary<string, ISecurity>();

            foreach (ISecurity security in securities)
                securityDictionary.Add(security.Isin, security);

	        DataTable transactionsTable = new DataTable("TransactionHistory");

            transactionsTable.Columns.Add("Isin");
            transactionsTable.Columns["Isin"].DataType = typeof(string);
	        transactionsTable.Columns.Add("ID");
	        transactionsTable.Columns["ID"].DataType = typeof(int);
            transactionsTable.Columns.Add("Security");
            transactionsTable.Columns["Security"].DataType = typeof(string);
	        transactionsTable.Columns.Add("Timestamp");
	        transactionsTable.Columns["Timestamp"].DataType = typeof(DateTime);
	        transactionsTable.Columns.Add("Type");
	        transactionsTable.Columns["Type"].DataType = typeof(string);
	        transactionsTable.Columns.Add("Amount");
	        transactionsTable.Columns["Amount"].DataType = typeof(double);
	
	        foreach (ITransaction transaction in transactions)
	        {
	            DataRow row = transactionsTable.NewRow();
                row[0] = transaction.Isin;
	            row[1] = transaction.ID;
                row[2] = securityDictionary[transaction.Isin].Name;
	            row[3] = transaction.Timestamp;
	            row[4] = transaction.Type;
	            row[5] = transaction.Amount;
	
	            transactionsTable.Rows.Add(row);
	        }
	
	        return transactionsTable;
	    }

        //De kolom met de Isin nummers onzichtbaar maken
        protected void TransactionsGridview_RowDataBound(object sender, GridViewRowEventArgs e)
        {
            if (e.Row.RowType == DataControlRowType.DataRow)
                e.Row.Cells[0].Visible = false;
            else if (e.Row.RowType == DataControlRowType.Header)
                e.Row.Cells[0].Visible = false;
        }
	}
}