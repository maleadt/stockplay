<%@ Page Language="C#" MasterPageFile="~/MasterPage.master" AutoEventWireup="true" CodeFile="Default.aspx.cs" Inherits="_Default" Title="Home" %>

<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder" Runat="Server">
    <h1>Welcome to Stockplay!</h1>
    
    <p class="para">Stockplay is an online browser game where you trade stocks and other securities. It works with live data
    straight from the most popular exchanges around the world.</p>
    <p class="para">Visit the <a runat="server" href="~/Register.aspx">registration page</a> to register a free account
    and start playing immediately! Currently the game is still in beta, so not all functionality is ready
    yet, and you may encounter some problems. If you want more information on how to play the game and
    the current status of the project, you can visit the <a runat="server" href="~/Help.aspx">help page</a>.</p>
</asp:Content>