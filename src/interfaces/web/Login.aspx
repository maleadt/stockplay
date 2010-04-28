<%@ Page Language="C#" MasterPageFile="~/MasterPage.master" AutoEventWireup="true" CodeFile="Login.aspx.cs" Inherits="StockPlay.Web.Login"
    culture="auto" uiculture="auto" meta:resourcekey="Page"%>

<asp:Content ID="Content1" ContentPlaceHolderID="head" Runat="Server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder" Runat="Server">
    <p><asp:Literal runat="server" Text="<%$ Resources:Nickname %>" /> <asp:TextBox ID="txtUsername" runat="server"></asp:TextBox></p>
    <p><asp:Literal runat="server" Text="<%$ Resources:Password %>" /> <asp:TextBox ID="txtPassword" runat="server" TextMode="Password"></asp:TextBox></p>
    <p>
        <asp:CheckBox ID="chkRememberMe" runat="server" Text="<%$ Resources:RememberMe %>"/>
        <a href="ForgotPassword.aspx" meta:resourcekey="ForgotPassword" runat="server"></a>
    </p>
    <asp:Button ID="btnLogin" runat="server" Text="Login" onclick="btnLogin_Click" meta:resourcekey="LoginButton" />
</asp:Content>

