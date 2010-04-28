<%@ Page Language="C#" MasterPageFile="~/MasterPage.master" AutoEventWireup="true" CodeFile="Portfolio.aspx.cs" Inherits="StockPlay.Web.User_Portfolio"
    UICulture="auto" Culture="auto" meta:resourcekey="Page" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" Runat="Server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder" Runat="Server">
    <h1 runat="server" meta:resourcekey="Title">Portfolio</h1>

    <p class="para" runat="server" meta:resourcekey="Para1"></p>

    <div id="SellMessage" runat="server" visible="false">
        <p class="para" runat="server" meta:resourcekey="Para2">Are you sure you want to place an order to sell stocks of security <b id="SecurityName" runat="server"></b> for <b id="Price" runat="server"></b>?</p>
        <p>
            <asp:Literal runat="server" Text="<%$ Resources:Para31 %>" /> <asp:TextBox ID="txtAmount" runat="server" /> / 
            <asp:TextBox ID="txtTotalAmount" runat="server" Enabled="False"></asp:TextBox><br />
            <asp:RangeValidator ID="txtAmountValidator" runat="server" ControlToValidate="txtAmount"
            MinimumValue="0" Type="Integer" meta:resourcekey="AmountError"></asp:RangeValidator>
            
            <asp:Literal runat="server" Text="<%$ Resources:Para32 %>" /> <asp:TextBox ID="txtPrice" runat="server"></asp:TextBox>
            <asp:RangeValidator ID="txtPriceValidator" runat="server" ControlToValidate="txtPrice"
            MinimumValue="1" MaximumValue="1000000" Type="Double" meta:resourcekey="PriceError"></asp:RangeValidator>
        </p>
        <p>
            <asp:Button ID="btnConfirm" runat="server" OnClick="btnConfirm_Click" meta:resourcekey="Confirm" />
            <asp:Button ID="btnCancel" runat="server" OnClick="btnCancel_Click" meta:resourcekey="Cancel" />
        </p>
    </div>

    <asp:GridView ID="PortfolioGridview" runat="server" AutoGenerateColumns="False" GridLines="None" CellSpacing="-1">
        <Columns>
            <asp:BoundField DataField="Isin" meta:resourcekey="Isin" />
            <asp:BoundField DataField="Security" meta:resourcekey="Security" />
            <asp:BoundField DataField="Quote" DataFormatString="{0:0.00}" meta:resourcekey="Quote" />
            <asp:BoundField DataField="Amount" meta:resourcekey="Amount" />
            <asp:HyperLinkField DataNavigateUrlFields="Isin" DataNavigateUrlFormatString="~/User/Portfolio.aspx?sell={0}" meta:resourcekey="Sell" />
        </Columns>
    </asp:GridView>
</asp:Content>