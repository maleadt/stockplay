<%@ Page Language="C#" MasterPageFile="~/MasterPage.master" AutoEventWireup="true" CodeFile="Help.aspx.cs" Inherits="StockPlay.Web.Help"
    uiculture="auto" culture="auto" meta:resourcekey="Page" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" Runat="Server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder" Runat="Server">
    <h1 meta:resourcekey="Title" runat="server"></h1>
    
    <p meta:resourcekey="ParaIntro" runat="server"></p>
    
    <h2 meta:resourcekey="Subtitle1" runat="server"></h2>
    <p class="para" meta:resourcekey="Para11" runat="server"></p>
    <p class="para" runat="server"><asp:Literal meta:resourcekey="Para12" runat="server" /></p>
    <p class="para" meta:resourcekey="Para13" runat="server"></p>
    
    <h2 meta:resourcekey="Subtitle2" runat="server"></h2>
    <p class="para" meta:resourcekey="Para21" runat="server"></p>
    <ul>
        <li><asp:Literal meta:resourcekey="Item1" runat="server" /></li>
        <li><asp:Literal meta:resourcekey="Item2" runat="server" /></li>
        <li><asp:Literal meta:resourcekey="Item3" runat="server" /></li>
    </ul>
</asp:Content>

