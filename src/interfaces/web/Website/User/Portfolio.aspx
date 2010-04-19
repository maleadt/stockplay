<%@ Page Language="C#" MasterPageFile="~/MasterPage.master" AutoEventWireup="true" CodeFile="Portfolio.aspx.cs" Inherits="User_Portfolio" Title="Portfolio" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" Runat="Server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder" Runat="Server">

    <div id="SellMessage" runat="server" visible="false">
        <p>Are you sure you want to place an order to sell your stocks of security <b id="SecurityName" runat="server"></b> for <b id="Price" runat="server"></b>?</p>
        <p>
            <asp:Button ID="btnConfirm" Text="Confirm" runat="server" OnClick="btnConfirm_Click" />
            <asp:Button ID="btnCancel" Text="Cancel" runat="server" OnClick="btnCancel_Click" />
        </p>
    </div>

    <asp:GridView ID="PortfolioGridview" runat="server" AutoGenerateColumns="False" GridLines="None" CellSpacing="-1">
        <Columns>
            <asp:BoundField DataField="Isin" HeaderText="Isin" />
            <asp:BoundField DataField="Security" HeaderText="Security" />
            <asp:BoundField DataField="Quote" HeaderText="Latest Quote" DataFormatString="{0:0.00}" />
            <asp:BoundField DataField="Amount" HeaderText="Amount" />
            <asp:HyperLinkField Text="Place Sell Order" DataNavigateUrlFields="Isin,Amount" DataNavigateUrlFormatString="~/User/Portfolio.aspx?sell={0}&amount={1}" />
        </Columns>
    </asp:GridView>
</asp:Content>