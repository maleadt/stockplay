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

public partial class User_OrdersOverview : System.Web.UI.Page
{
    protected void Page_Load(object sender, EventArgs e)
    {
        if (!IsPostBack)
        {
            IDataAccess data = DataAccessFactory.GetDataAccess();

            List<IOrder> orders = data.GetUserOrders(((StockplayMembershipUser)Membership.GetUser(User.Identity.Name)).ID);
            OrdersGridview.DataSource = GenerateDataTable(orders);
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
    private DataTable GenerateDataTable(List<IOrder> orders)
    {
        DataTable ordersTable = new DataTable("Orders");

        ordersTable.Columns.Add("ID");
        ordersTable.Columns["ID"].DataType = typeof(int);
        ordersTable.Columns.Add("Isin");
        ordersTable.Columns["Isin"].DataType = typeof(string);
        ordersTable.Columns.Add("Amount");
        ordersTable.Columns["Amount"].DataType = typeof(int);
        ordersTable.Columns.Add("Price");
        ordersTable.Columns["Price"].DataType = typeof(double);
        ordersTable.Columns.Add("Type");
        ordersTable.Columns["Type"].DataType = typeof(string);
        ordersTable.Columns.Add("Status");
        ordersTable.Columns["Status"].DataType = typeof(string);


        foreach (IOrder order in orders)
        {
            DataRow row = ordersTable.NewRow();
            row[0] = order.ID;
            row[1] = order.Isin;
            row[2] = order.Amount;
            row[3] = order.Price;
            row[4] = order.Type;
            row[5] = order.Status;

            ordersTable.Rows.Add(row);
        }

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
}
