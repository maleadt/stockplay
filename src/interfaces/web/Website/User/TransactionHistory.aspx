<%@ Page Language="C#" MasterPageFile="~/MasterPage.master" AutoEventWireup="true" CodeFile="TransactionHistory.aspx.cs" Inherits="User_TransactionHistory" Title="Transaction History" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" Runat="Server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder" Runat="Server">
    <asp:GridView ID="TransactionsGridview" runat="server" AutoGenerateColumns="False" GridLines="None" CellSpacing="-1">
        <Columns>
            <asp:BoundField DataField="ID" HeaderText="TransactionID" />
            <asp:BoundField DataField="Isin" HeaderText="Isin" />
            <asp:BoundField DataField="Timestamp" HeaderText="Time" />
            <asp:BoundField DataField="Type" HeaderText="Type" />
            <asp:BoundField DataField="Amount" HeaderText="Amount" />
        </Columns>
    </asp:GridView>
</asp:Content>

