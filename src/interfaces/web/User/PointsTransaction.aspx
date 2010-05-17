<%@ Page meta:resourcekey="Page" Language="C#" MasterPageFile="~/MasterPage.master" AutoEventWireup="true" CodeBehind="PointsTransaction.aspx.cs" Inherits="web.User.PointsTransaction" %>
<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder" runat="server">

    <h1 runat="server" meta:resourcekey="Title"></h1>

    <p runat="server" class="para">
        <asp:Literal ID="Literal1" meta:resourcekey="Para1" runat="server" />
        <asp:Literal ID="TotalPoints" runat="server" />
        <asp:Literal ID="Literal3" meta:resourcekey="Para2" runat="server" />
    </p>

    <p runat="server" class="para" meta:resourcekey="NewPoints" id="NewPoints" visible="false"></p>
    <asp:GridView ID="NewPointsGridView" runat="server" AutoGenerateColumns="False" GridLines="None" CellSpacing="-1">
        <Columns>
            <asp:BoundField DataField="Type" meta:resourcekey="Type" />
            <asp:BoundField DataField="Points" meta:resourcekey="Points" />
            <asp:BoundField DataField="Date"  HtmlEncode="false" DataFormatString="{0:dd/MM, HH:MM:ss}" meta:resourcekey="Date" />
            <asp:BoundField DataField="Comments" meta:resourcekey="Comment" />
        </Columns>
    </asp:GridView>

    <p runat="server" class="para" meta:resourcekey="OldPoints"></p>
    
    <p runat="server" class="paraNotification" id="EmptyNotification" meta:resourcekey="EmptyNotification" visible="false"></p>

    <asp:GridView ID="PointsGridView" runat="server" AutoGenerateColumns="False" GridLines="None" CellSpacing="-1">
        <Columns>
            <asp:BoundField DataField="Type" meta:resourcekey="Type" />
            <asp:BoundField DataField="Points" meta:resourcekey="Points" />
            <asp:BoundField DataField="Date"  HtmlEncode="false" DataFormatString="{0:dd/MM, HH:MM:ss}" meta:resourcekey="Date" />
            <asp:BoundField DataField="Comments" meta:resourcekey="Comment" />
        </Columns>
    </asp:GridView>

</asp:Content>
