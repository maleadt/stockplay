<%@ Page Language="C#" MasterPageFile="~/MasterPage.master" AutoEventWireup="true" CodeFile="Portfolio.aspx.cs" Inherits="User_Portfolio" Title="Portfolio" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" Runat="Server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder" Runat="Server">
    <h1>Portfolio</h1>

    <p class="para">Here you can view all of the securities you currently have in your portfolio. If you want to sell one of
    your securities, you can click the 'Place sell order' link next to the security.</p>

    <div id="SellMessage" runat="server" visible="false">
        <p>Are you sure you want to place an order to sell stocks of security <b id="SecurityName" runat="server"></b> for <b id="Price" runat="server"></b>?</p>
        <p>
            How many stocks do you want to sell? <asp:TextBox ID="txtAmount" runat="server" /> / 
            <asp:TextBox ID="txtTotalAmount" runat="server" Enabled="False"></asp:TextBox><br />
            <asp:RangeValidator ID="txtAmountValidator" runat="server" ControlToValidate="txtAmount"
            ErrorMessage="Please enter an amount smaller than or equal your total amount of stocks
             for this security." MinimumValue="0" Type="Integer"></asp:RangeValidator>
            
            Asking price per stock? <asp:TextBox ID="txtPrice" runat="server"></asp:TextBox>
            <asp:RangeValidator ID="txtPriceValidator" runat="server" ControlToValidate="txtPrice"
            ErrorMessage="Please enter a positive number." MinimumValue="1" MaximumValue="1000000" Type=Double></asp:RangeValidator>
        </p>
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
            <asp:HyperLinkField Text="Place Sell Order" DataNavigateUrlFields="Isin" DataNavigateUrlFormatString="~/User/Portfolio.aspx?sell={0}" />
        </Columns>
    </asp:GridView>
</asp:Content>