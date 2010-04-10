<%@ Page Language="C#" MasterPageFile="~/MasterPage.master" AutoEventWireup="true" CodeFile="CreateOrder.aspx.cs" Inherits="User_CreateOrder" Title="Create order" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" Runat="Server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder" Runat="Server">
    <h1>Create order:</h1>
    <p>You are placing an order on <b id="Security" runat="server"></b>.</p>
    <p>The quote for this security is currently <b id="Quote" runat="server"></b>.</p>
    
    <p>Account balance: <b id="Cash" runat="server"></b></p>
    
    <p>
        Amount: <asp:TextBox ID="txtAmount" runat="server" ValidationGroup="AmountValidation"></asp:TextBox> x 
        <asp:TextBox ID="txtQuote" runat="server" ReadOnly="True" Enabled="false"></asp:TextBox> = <asp:Label ID="Total" runat="server"></asp:Label>
    </p>
    <p>
        <asp:RangeValidator ID="RangeValidator1" runat="server" 
            ErrorMessage="You need to enter an integer larger than 0" ValidationGroup="AmountValidation"
            ControlToValidate="txtAmount" MinimumValue="1" MaximumValue="100000" Type="Integer"></asp:RangeValidator>
    </p>
    <p id="Notification" runat="server" visible="false">
        After this order your new account balance will be <b id="NewBalance" runat="server"></b>. Are you sure?
    </p>
    
    <p>
        <asp:Button ID="btnConfirm" Text="Confirm" runat="server" OnClick="btnConfirm_Click" ValidationGroup="AmountValidation" Visible="false" />
        <asp:Button ID="btnContinue" Text="Continue" runat="server" OnClick="btnContinue_Click" ValidationGroup="AmountValidation" />
        <asp:Button ID="btnCancel" Text="Cancel" runat="server" onclick="btnCancel_Click" />
    </p>
</asp:Content>

