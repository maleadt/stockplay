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
	public partial class User_OrdersOverview : System.Web.UI.Page
	{
	    protected void Page_Load(object sender, EventArgs e)
	    {
	        if (!IsPostBack)
	        {
	            IDataAccess data = DataAccessFactory.GetDataAccess();
	
	            List<IOrder> orders = data.GetUserOrders(((StockplayMembershipUser)Membership.GetUser(User.Identity.Name)).ID);
	
	            string[] isins = new string[orders.Count];
	            for (int i = 0; i < orders.Count; i++)
	                isins[i] = orders[i].Isin;
	
	
	            List<ISecurity> securities = null;
	            if (isins.Length > 0)
	                securities = data.GetSecurityByIsin(isins);
	
	            OrdersGridview.DataSource = GenerateDataTable(orders, securities);
	            OrdersGridview.DataBind();
	
	
	            if (Request.Params["remove"] != null)
	            {
	                int orderId = Convert.ToInt32(Request.Params["remove"]);
	                bool validOrder = false;
	                IOrder selectedOrder = null;
	                foreach(IOrder order in orders) {
	                    if(order.ID == orderId) {
	                        validOrder = true;
	                        selectedOrder = order;
	                    }
	                }
	
	                if(!validOrder)
	                    Response.Redirect("~/User/OrdersOverview.aspx");
	
	                DeleteMessage.Visible = true;
	                OrderId.InnerText = Convert.ToString(selectedOrder.ID);
	                SecurityID.InnerText = selectedOrder.Isin;
	            }
	        }
	    }
	
	    // TODO De price en status kolommen worden nog niet doorgegeven via de back-end
	    private DataTable GenerateDataTable(List<IOrder> orders, List<ISecurity> securities)
	    {
	        DataTable ordersTable = new DataTable("Orders");
	
	        ordersTable.Columns.Add("Isin");
	        ordersTable.Columns["Isin"].DataType = typeof(string);
	        ordersTable.Columns.Add("ID");
	        ordersTable.Columns["ID"].DataType = typeof(int);
	        ordersTable.Columns.Add("Security");
	        ordersTable.Columns["Security"].DataType = typeof(string);
	        ordersTable.Columns.Add("Amount");
	        ordersTable.Columns["Amount"].DataType = typeof(int);
	        ordersTable.Columns.Add("Price");
	        ordersTable.Columns["Price"].DataType = typeof(double);
	        ordersTable.Columns.Add("Type");
	        ordersTable.Columns["Type"].DataType = typeof(string);
	        ordersTable.Columns.Add("Status");
	        ordersTable.Columns["Status"].DataType = typeof(string);
	
	        for(int i=0 ; i<orders.Count ; i++)
	        {
	            DataRow row = ordersTable.NewRow();
	            row[0] = orders[i].Isin;
	            row[1] = orders[i].ID;
	            // TODO proper maken...
	            for(int j=0 ; j<securities.Count ; j++)
	                if(securities[j].Isin.Equals(orders[i].Isin))
	                    row[2] = securities[j].Name;
	            row[3] = orders[i].Amount;
	            row[4] = orders[i].Price;
	            row[5] = orders[i].Type;
	            row[6] = orders[i].Status;
	
	            ordersTable.Rows.Add(row);
	        }
	
	        //foreach (IOrder order in orders)
	        //{
	        //    DataRow row = ordersTable.NewRow();
	        //    row[0] = order.ID;
	        //    row[1] = order.Isin;
	        //    row[2] = order.Amount;
	        //    row[3] = order.Price;
	        //    row[4] = order.Type;
	        //    row[5] = order.Status;
	
	        //    ordersTable.Rows.Add(row);
	        //}
	
	        return ordersTable;
	    }
	
	    protected void btnConfirm_Click(object sender, EventArgs e)
	    {
	        IDataAccess data = DataAccessFactory.GetDataAccess();
	
	        int orderId = Convert.ToInt32(OrderId.InnerText);
	        data.CancelOrder(orderId);
	
	        Response.Redirect("~/User/OrdersOverview.aspx");
	    }
	
	    protected void btnCancel_Click(object sender, EventArgs e)
	    {
	        Response.Redirect("~/User/OrdersOverview.aspx");
	    }
	
	    protected void OrdersGridViewRowDataBound(object sender, GridViewRowEventArgs e)
	    {
	        if (e.Row.RowType == DataControlRowType.DataRow)
	        {
	            if (e.Row.Cells[6].Text.Equals("CANCELLED"))
	                e.Row.Cells[7].Text = "-";
	
	            e.Row.Cells[0].Visible = false; //Isin verbergen
	        }
	        if (e.Row.RowType == DataControlRowType.Header)
	            e.Row.Cells[0].Visible = false; //Header van Isin verbergen
	    }
	}
}