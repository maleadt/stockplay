<%@ Page Language="C#" MasterPageFile="~/MasterPage.master" AutoEventWireup="true" CodeBehind="Restart.aspx.cs" Inherits="StockPlay.Web.Restart" Title="Untitled Page" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" Runat="Server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder" Runat="Server">
    <asp:Button ID="RestartButton" class="button" Text="Restart ASP.NET website" runat="server" 
        onclick="RestartButton_Click" />
</asp:Content>

