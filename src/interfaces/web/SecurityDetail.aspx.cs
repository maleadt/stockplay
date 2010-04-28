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
	public partial class SecurityDetail : System.Web.UI.Page
	{
	
	    public string ISINcode;
	
	    protected void Page_Load(object sender, EventArgs e)
	    {
	        if(Request.Params["param"] != null) {
	            IDataAccess data = DataAccessFactory.GetDataAccess();
	
	            try
	            {
	                ISecurity security = data.GetSecurityByIsin(Request.Params["param"])[0];
	                GeneratePage(security);
	                Page.Title = security.Name + " " + Page.Title;
	            }
	            catch (Exception ex)
	            {
	                Response.Redirect("~/SecuritiesOverview.aspx");
	            }
	        }
	        else
	            Response.Redirect("~/SecuritiesOverview.aspx");
	    }
	
	    private void GeneratePage(ISecurity security)
	    {
	        IQuote latestQuote = security.GetLatestQuote();
	
	        BuyHyperlink.NavigateUrl = "~/User/CreateOrder.aspx?isin=" + security.Isin;
	
	        //General
	        Name.InnerText = security.Name;
	
	        double quoteChange = 0;
	
	        if (latestQuote != null)
	        {
	            Value.InnerText = String.Format("{0:0.00}", latestQuote.Price);
	            quoteChange = security.GetLatestQuote().Change;
	        }
	
	        Change.InnerText = (quoteChange>=0 ? "+" : "") + Convert.ToString(quoteChange) + "%";
	        if (quoteChange > 0)
	            Change.Attributes.Add("class", "pos");
	        else if (quoteChange < 0)
	            Change.Attributes.Add("class", "neg");
	        
	        
	        Exchange.InnerText = security.Exchange.Name;
	        ISIN.InnerText = security.Isin;
	        ISINcode = security.Isin;
	        Symbol.InnerText = security.Symbol;
	
	        //Data
	        if (latestQuote != null)
	        {
	            Open.InnerText = Convert.ToString(latestQuote.Open);
	            High.InnerText = Convert.ToString(latestQuote.High);
	            Low.InnerText = Convert.ToString(latestQuote.Low);
	        }
	
	        //History
	        DataTable historyTable = new DataTable();
	
	        historyTable.Columns.Add("Date");
	        historyTable.Columns["Date"].DataType = typeof(DateTime);
	        historyTable.Columns.Add("Change");
	        historyTable.Columns["Change"].DataType = typeof(Double);
	        historyTable.Columns.Add("Open");
	        historyTable.Columns["Open"].DataType = typeof(Double);
	        historyTable.Columns.Add("High");
	        historyTable.Columns["High"].DataType = typeof(Double);
	        historyTable.Columns.Add("Low");
	        historyTable.Columns["Low"].DataType = typeof(Double);
	
	        List<IQuote> quotes = security.GetDailyQuotes(DateTime.Now.Subtract(new TimeSpan(7, 0, 0, 0)), DateTime.Now);
	
	        foreach(IQuote quote in quotes)
	        {
	            DataRow row = historyTable.NewRow();
	            row[0] = quote.Time;
	            row[1] = quote.Change;
	            row[2] = quote.Open;
	            row[3] = quote.High;
	            row[4] = quote.Low;
	            historyTable.Rows.Add(row);
	        }
	
	        HistoryGridView.DataSource = historyTable;
	        HistoryGridView.DataBind();
	    }
	
	    //Zorgt ervoor dat de kleur van de tekst correct ingesteld wordt in de "Change" kolom
	    protected void HistoryGridView_RowDataBound(object sender, GridViewRowEventArgs e)
	    {
	        if (e.Row.RowType == DataControlRowType.DataRow)
	        {
	            if (Convert.ToDouble(e.Row.Cells[1].Text) >= 0)
	                e.Row.Cells[1].CssClass = "pos";
	            else
	                e.Row.Cells[1].CssClass = "neg";
	        }
	    }
	}
}