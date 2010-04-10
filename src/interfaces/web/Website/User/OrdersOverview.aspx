<%@ Page Language="C#" MasterPageFile="~/MasterPage.master" AutoEventWireup="true" CodeFile="OrdersOverview.aspx.cs" Inherits="User_OrdersOverview" Title="Orders Overview" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" Runat="Server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder" Runat="Server">
    <div id="DeleteMessage" runat="server" visible="false">
        <p>Are you sure you want to remove ordernr. <b id="OrderId" runat="server"></b> for security <b id="SecurityID" runat="server"></b>?</p>
        <p>
            <asp:Button ID="btnConfirm" Text="Confirm" runat="server" OnClick="btnConfirm_Click" />
            <asp:Button ID="btnCancel" Text="Cancel" runat="server" OnClick="btnCancel_Click" />
        </p>
    </div>

    <asp:GridView ID="OrdersGridview" runat="server" AutoGenerateColumns="False" GridLines="None" CellSpacing="-1">
        <Columns>
            <asp:BoundField DataField="ID" HeaderText="ID" />
            <asp:BoundField DataField="Isin" HeaderText="Isin" />
            <asp:BoundField DataField="Amount" HeaderText="Amount" />
            <asp:BoundField DataField="Price" HeaderText="Price" />
            <asp:BoundField DataField="Type" HeaderText="Type" />
            <asp:BoundField DataField="Status" HeaderText="Status" />
            <asp:HyperLinkField Text="Cancel order" DataNavigateUrlFields="ID" DataNavigateUrlFormatString="~/User/OrdersOverview.aspx?remove={0}" />
        </Columns>
    </asp:GridView>
</asp:Content>

