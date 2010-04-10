<%@ Page Language="C#" MasterPageFile="~/MasterPage.master" AutoEventWireup="true" CodeFile="Login.aspx.cs" Inherits="Login" Title="Login" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" Runat="Server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder" Runat="Server">
    <p>Nickname: <asp:TextBox ID="txtUsername" runat="server"></asp:TextBox></p>
    <p>Password: <asp:TextBox ID="txtPassword" runat="server" TextMode="Password"></asp:TextBox></p>
    <p>
        <asp:CheckBox ID="chkRememberMe" runat="server" Text="Remember me" />
        <a href="ForgotPassword.aspx">Forgot password?</a>
    </p>
    <asp:Button ID="btnLogin" runat="server" Text="Login" onclick="btnLogin_Click" />
</asp:Content>

