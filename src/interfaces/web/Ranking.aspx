<%@ Page Language="C#" MasterPageFile="~/MasterPage.master" AutoEventWireup="true" CodeBehind="Ranking.aspx.cs" Inherits="StockPlay.Web.Ranking" Title="Ranking"
    UICulture="auto" Culture="auto" meta:resourcekey="Page" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" Runat="Server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder" Runat="Server">
    <h1 runat="server" meta:resourcekey="Title"></h1>

    <p class="RankingMenu">
        <a href="~/Ranking.aspx" meta:resourcekey="Global" runat="server" id="Global"></a> |
        <a href="~/Ranking.aspx?event=PROFIT" meta:resourcekey="Profitrank" runat="server" id="Profitrank"></a> |
        <a href="~/Ranking.aspx?event=CASH" meta:resourcekey="Cashrank" runat="server" id="Cashrank"></a>
    </p>

    <p runat="server" class="para" visible="false" id="SearchUserP">
        <asp:Literal runat="server" meta:resourcekey="SearchUser" /><br />
        <asp:TextBox runat="server" ID="txtSearchUser" />
        <asp:Button runat="server" meta:resourcekey="btnSearchUser" ID="btnSearchUser" OnClick="SearchUser" />
        <asp:RequiredFieldValidator ID="txtSearchUserRequired" runat="server" ValidationGroup="Test" ControlToValidate="txtSearchUser"></asp:RequiredFieldValidator>
    </p>

    
    <p runat="server" class="para" visible="false" id="SearchRankingP">
        <asp:Literal ID="Literal1" runat="server" meta:resourcekey="SearchRanking" /><br />
        <asp:TextBox runat="server" ID="txtSearchRanking" />
        <asp:Button runat="server" meta:resourcekey="btnSearchRanking" ID="btnSearchRanking" OnClick="SearchRanking" />
        <asp:CompareValidator ID="txtSearchRankingRequired" ValidationGroup="Test" runat="server" Type="Integer" Operator="GreaterThan" ValueToCompare="0" ControlToValidate="txtSearchRanking"></asp:CompareValidator>
    </p>

    <p runat="server" class="paraNotification" id="EmptyNotification" meta:resourcekey="EmptyNotification" visible="false"></p>

    <asp:GridView ID="RankingGridView" runat="server" AutoGenerateColumns="False" GridLines="None" CellSpacing="-1" Visible="false">
        <Columns>
            <asp:BoundField DataField="Username" meta:resourcekey="Username" />
            <asp:BoundField DataField="Total" meta:resourcekey="Total" />
            <asp:BoundField DataField="Rank" meta:resourcekey="Rank" />
        </Columns>
    </asp:GridView>

    <asp:GridView ID="PointsTransactionGridView" runat="server" AutoGenerateColumns="False" GridLines="None" CellSpacing="-1" Visible="false">
        <Columns>
            <asp:BoundField DataField="Username" meta:resourcekey="Username" />
            <asp:BoundField DataField="Points" meta:resourcekey="Total" />
            <asp:BoundField DataField="Comments" meta:resourcekey="Comments" />
        </Columns>
    </asp:GridView>
</asp:Content>

