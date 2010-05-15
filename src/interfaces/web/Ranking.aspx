<%@ Page Language="C#" MasterPageFile="~/MasterPage.master" AutoEventWireup="true" CodeBehind="Ranking.aspx.cs" Inherits="StockPlay.Web.Ranking" Title="Ranking"
    UICulture="auto" Culture="auto" meta:resourcekey="Page" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" Runat="Server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder" Runat="Server">
    <h1 runat="server" meta:resourcekey="Title"></h1>

    <a href="~/Ranking.aspx" meta:resourcekey="Global" runat="server"></a> |
    <a href="~/Ranking.aspx?event=Profit" meta:resourcekey="Profitrank" runat="server"></a> |
    <a href="~/Ranking.aspx?event=Cash" meta:resourcekey="Cashrank" runat="server"></a>

    <asp:GridView ID="RankingGridView" runat="server" AutoGenerateColumns="False" GridLines="None" CellSpacing="-1"
                OnRowDataBound="TransactionGridvie_RowDatabound">
        <Columns>
            <asp:BoundField DataField="Username" meta:resourcekey="Username" />
            <asp:BoundField DataField="Total" meta:resourcekey="Total" />
            <asp:BoundField DataField="Rank" meta:resourcekey="Rank" />
            <asp:BoundField DataField="Cash" meta:resourcekey="Cash" />
        </Columns>
    </asp:GridView>
</asp:Content>

