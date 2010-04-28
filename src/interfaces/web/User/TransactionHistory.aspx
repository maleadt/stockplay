<%@ Page Language="C#" MasterPageFile="~/MasterPage.master" AutoEventWireup="true" CodeFile="TransactionHistory.aspx.cs" Inherits="StockPlay.Web.User_TransactionHistory"
    UICulture="auto" Culture="auto" meta:resourcekey="Page" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" Runat="Server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder" Runat="Server">
    <h1 runat="server" meta:resourcekey="Title"></h1>

    <asp:GridView ID="TransactionsGridview" runat="server" AutoGenerateColumns="False" GridLines="None" CellSpacing="-1">
        <Columns>
            <asp:BoundField DataField="ID" meta:resourcekey="ID" />
            <asp:BoundField DataField="Isin" meta:resourcekey="Isin" />
            <asp:BoundField DataField="Timestamp" meta:resourcekey="Timestamp" />
            <asp:BoundField DataField="Type" meta:resourcekey="Type" />
            <asp:BoundField DataField="Amount" meta:resourcekey="Amount" />
        </Columns>
    </asp:GridView>
</asp:Content>

