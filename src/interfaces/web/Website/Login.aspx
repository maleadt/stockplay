<%@ Page Language="C#" MasterPageFile="~/MasterPage.master" AutoEventWireup="true" CodeFile="Login.aspx.cs" Inherits="Login" Title="Login" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" Runat="Server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder" Runat="Server">
    <asp:TextBox ID="txtUsername" runat="server"></asp:TextBox>
    <asp:TextBox ID="txtPassword" runat="server" TextMode="Password"></asp:TextBox>
    <asp:CheckBox ID="chkRememberMe" runat="server" Text="Remember me" />
    <asp:Button ID="btnLogin" runat="server" Text="Login" onclick="btnLogin_Click" />
</asp:Content>

