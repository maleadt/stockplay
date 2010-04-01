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

public partial class SecurityDetail : System.Web.UI.Page
{
    protected void Page_Load(object sender, EventArgs e)
    {
        if(Request.Params["param"] != null) {
            IDataAccess data = DataAccess.GetInstance();

            try
            {
                Security security = data.GetSecurityBySymbol(Request.Params["param"]);
                GeneratePage(security);
            }
            catch (Exception ex)
            {
                Response.Redirect("~/SecuritiesOverview.aspx");
            }
        }
        else
            Response.Redirect("~/SecuritiesOverview.aspx");
    }

    private void GeneratePage(Security security)
    {
        Quote latestQuote = security.GetLatestQuote();

        //General
        Name.InnerText = security.Name;
        if(latestQuote != null)
            Value.InnerText = Convert.ToString(latestQuote.Price);

        double quoteChange = security.GetChange();
        Change.InnerText = (quoteChange>=0 ? "+" : "") + Convert.ToString(quoteChange) + "%";
        if (quoteChange >= 0)
            Change.Attributes.Add("class", "pos");
        else
            Change.Attributes.Add("class", "neg");
        
        
        Exchange.InnerText = security.Exchange.Name;
        ISIN.InnerText = security.Isin;
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

        for (int i = 0; i < 30; i++)
        {
            DateTime date = DateTime.Now.Subtract(new TimeSpan(i+1, 0, 0, 0));
            Quote quote = security.GetQuote(date);

            DataRow row = historyTable.NewRow();
            if (quote != null && quote.Time.Day == date.Day) //We controleren ofdat er die dag wel een quote beschikbaar was
            {
                row[0] = quote.Time;
                row[1] = Math.Round((latestQuote.Price - quote.Open) / latestQuote.Price * 100, 2);
                row[2] = quote.Open;
                row[3] = quote.High;
                row[4] = quote.Low;
                historyTable.Rows.Add(row);
            }
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
