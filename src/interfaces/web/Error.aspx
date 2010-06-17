<%@ Page Title="" Language="C#" MasterPageFile="~/MasterPage.master" AutoEventWireup="true" CodeBehind="Error.aspx.cs" Inherits="web.Error"
    UICulture="auto" Culture="auto" meta:resourcekey="Page" %>
<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder" runat="server">
    <h1 runat="server" meta:resourcekey="Title" class="warning"></h1>

    <p runat="server" meta:resourcekey="Message" class="para"></p>
</asp:Content>
