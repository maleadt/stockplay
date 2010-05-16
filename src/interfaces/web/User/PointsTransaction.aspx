<%@ Page Title="" Language="C#" MasterPageFile="~/MasterPage.master" AutoEventWireup="true" CodeBehind="PointsTransaction.aspx.cs" Inherits="web.User.PointsTransaction" %>
<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder" runat="server">

    <asp:GridView ID="NewPointsGridView" runat="server" AutoGenerateColumns="False" GridLines="None" CellSpacing="-1">
        <Columns>
            <asp:BoundField DataField="Type" meta:resourcekey="Type" />
            <asp:BoundField DataField="Points" meta:resourcekey="Points" />
            <asp:BoundField DataField="Date" meta:resourcekey="Date" />
            <asp:BoundField DataField="Comments" meta:resourcekey="Comment" />
        </Columns>
    </asp:GridView>

    <asp:GridView ID="PointsGridView" runat="server" AutoGenerateColumns="False" GridLines="None" CellSpacing="-1">
        <Columns>
            <asp:BoundField DataField="Type" meta:resourcekey="Type" />
            <asp:BoundField DataField="Points" meta:resourcekey="Points" />
            <asp:BoundField DataField="Date" meta:resourcekey="Date" />
            <asp:BoundField DataField="Comments" meta:resourcekey="Comment" />
        </Columns>
    </asp:GridView>

</asp:Content>
