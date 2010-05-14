<%@ Page Language="C#" MasterPageFile="~/MasterPage.master" AutoEventWireup="true" CodeBehind="TransactionHistory.aspx.cs" Inherits="StockPlay.Web.User_TransactionHistory"
    UICulture="auto" Culture="auto" meta:resourcekey="Page" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" Runat="Server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder" Runat="Server">
    <h1 runat="server" meta:resourcekey="Title"></h1>

    <asp:GridView ID="TransactionsGridview" runat="server" AutoGenerateColumns="False" GridLines="None" CellSpacing="-1"
                    OnRowDataBound="TransactionsGridview_RowDataBound">
        <Columns>
            <asp:BoundField DataField="Isin" />
            <asp:BoundField DataField="ID" meta:resourcekey="ID" />
            <asp:HyperLinkField DataTextField="Security" DataNavigateUrlFields="Isin"
            DataNavigateUrlFormatString="~/SecurityDetail.aspx?param={0}" meta:resourcekey="Security">
                <ItemStyle CssClass="name" />
            </asp:HyperLinkField>
            <asp:BoundField DataField="Timestamp" meta:resourcekey="Timestamp" />
            <asp:BoundField DataField="Type" meta:resourcekey="Type" />
            <asp:BoundField DataField="Amount" meta:resourcekey="Amount" />
        </Columns>
    </asp:GridView>
</asp:Content>

