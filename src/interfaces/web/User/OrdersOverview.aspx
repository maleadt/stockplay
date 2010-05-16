<%@ Page Language="C#" MasterPageFile="~/MasterPage.master" AutoEventWireup="true" CodeBehind="OrdersOverview.aspx.cs" Inherits="StockPlay.Web.User_OrdersOverview"
    UICulture="auto" Culture="auto" meta:resourcekey="Page"%>

<asp:Content ID="Content1" ContentPlaceHolderID="head" Runat="Server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder" Runat="Server">
    <h1 runat="server" meta:resourcekey="Title"></h1>
    
    <p class="para" runat="server" meta:resourcekey="Para1"></p>

    <div id="DeleteMessage" runat="server" visible="false">
        <p class="para">
            <asp:Literal runat="server" Text="<%$ Resources:Para21 %>" /> <b id="OrderId" runat="server"></b> <asp:Literal ID="Literal1" runat="server" Text="<%$ Resources:Para22 %>" /> <b id="SecurityID" runat="server"></b>?
        </p>
        <p class="para">
            <asp:Button ID="btnConfirm" class="button" meta:resourcekey="Confirm" runat="server" OnClick="btnConfirm_Click" />
            <asp:Button ID="btnCancel" class="button" meta:resourcekey="Cancel" runat="server" OnClick="btnCancel_Click" />
        </p>
    </div>

    <asp:GridView ID="OrdersGridview" runat="server" AutoGenerateColumns="False" GridLines="None" CellSpacing="-1" OnRowDataBound="OrdersGridViewRowDataBound">
        <Columns>
            <asp:BoundField DataField="Isin" />
            <asp:BoundField DataField="ID" meta:resourcekey="OrderID" /> 
            <asp:HyperLinkField DataTextField="Security" DataNavigateUrlFields="Isin" DataNavigateUrlFormatString="~/SecurityDetail.aspx?param={0}" meta:resourcekey="Name" />      
            <asp:BoundField DataField="Amount" meta:resourcekey="Amount" />
            <asp:BoundField DataField="Price" HtmlEncode="false" DataFormatString="{0:0.00}" meta:resourcekey="Price" />
            <asp:BoundField DataField="secondairyLimit" HtmlEncode="false" DataFormatString="{0:0.00}" meta:resourcekey="secondairyLimit" />
            <asp:BoundField DataField="Type" meta:resourcekey="Type" />
            <asp:BoundField DataField="Status" meta:resourcekey="Status" />
            <asp:HyperLinkField DataNavigateUrlFields="ID" DataNavigateUrlFormatString="~/User/OrdersOverview.aspx?remove={0}" meta:resourcekey="CancelOrder" />
        </Columns>
    </asp:GridView>
</asp:Content>

