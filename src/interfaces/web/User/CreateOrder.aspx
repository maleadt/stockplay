<%@ Page Language="C#" MasterPageFile="~/MasterPage.master" AutoEventWireup="true" CodeBehind="CreateOrder.aspx.cs" Inherits="StockPlay.Web.User_CreateOrder"
    UICulture="auto" Culture="auto" meta:resourcekey="Page"  %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" Runat="Server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder" Runat="Server">
    <h1 runat="server" meta:resourcekey="Title">Create order:</h1>
    <p><asp:Literal runat="server" Text="<%$ Resources:Para1 %>" /> <b id="Security" runat="server"></b>.</p>
    <p><asp:Literal runat="server" Text="<%$ Resources:Para2 %>" /> <b id="Quote" runat="server"></b>.</p>
    
    <p><asp:Literal runat="server" Text="<%$ Resources:Para3 %>" /> <b id="Cash" runat="server"></b></p>
    
    <p>
        <asp:Literal runat="server" Text="<%$ Resources:Para4 %>" /> 
        <asp:TextBox ID="txtAmount" runat="server" ValidationGroup="AmountValidation" 
            meta:resourcekey="txtAmountResource1"></asp:TextBox> x 
        <asp:TextBox ID="txtQuote" runat="server" meta:resourcekey="txtQuoteResource1"></asp:TextBox> = 
        <asp:Label ID="Total" runat="server" meta:resourcekey="TotalResource1"></asp:Label>
    </p>
    <p>
        <asp:RangeValidator ID="RangeValidator1" runat="server" meta:resourcekey="AmountError" ValidationGroup="AmountValidation"
            ControlToValidate="txtAmount" MinimumValue="1" MaximumValue="1000000" Type="Integer"></asp:RangeValidator>
        <asp:RangeValidator ID="RangeValidator2" runat="server" ValidationGroup="AmountValidation" ControlToValidate="txtQuote"
            Type="Double" MinimumValue="1" MaximumValue="1000000" meta:resourcekey="QuoteError"></asp:RangeValidator>
         <asp:Label ID="ErrorLabel" meta:resourcekey="LabelError" ForeColor="Red" 
            runat="server" Visible="False" />
    </p>
    <p id="Notification" runat="server" visible="false">
        <asp:Literal runat="server" Text="<%$ Resources:Para51 %>" /> <b id="NewBalance" runat="server"></b>. <asp:Literal runat="server" Text="<%$ Resources:Para52 %>" />
    </p>
    
    <p>
        <asp:Button ID="btnConfirm" meta:resourcekey="Confirm" runat="server" 
            OnClick="btnConfirm_Click" ValidationGroup="AmountValidation" Visible="False" />
        <asp:Button ID="btnContinue" meta:resourcekey="Continue" runat="server" OnClick="btnContinue_Click" ValidationGroup="AmountValidation" />
        <asp:Button ID="btnCancel" meta:resourcekey="Cancel" runat="server" onclick="btnCancel_Click" />
    </p>
</asp:Content>

