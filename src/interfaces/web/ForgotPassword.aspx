<%@ Page Language="C#"  MasterPageFile="~/MasterPage.master" AutoEventWireup="true" CodeBehind="ForgotPassword.aspx.cs" Inherits="StockPlay.Web.ForgotPassword"
    Culture="auto" UICulture="auto" meta:resourcekey="Page"%>

<asp:Content ID="Content1" ContentPlaceHolderID="head" Runat="Server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder" Runat="Server">
    <h1 id="H1" runat="server" meta:resourcekey="Title"></h1>

    <div id="Recovery" runat="server">
        <p runat="server" class="para" meta:resourcekey="Para1"></p>
        <p runat="server" class="para" meta:resourcekey="Para2"></p>
        <p runat="server" class="para">
            <asp:Literal meta:resourcekey="Para3" runat="server"></asp:Literal>
            <asp:TextBox ID="Username" runat="server"></asp:TextBox>
            <asp:Button ID="Submit" meta:resourcekey="Submit" runat="server" 
            onclick="Submit_Click" />
        </p>
        <asp:Label ID="ErrorLabel" meta:resourcekey="Error" runat="server" Visible="false" ForeColor="Red"></asp:Label>
    </div>
    <div id="Success" runat="server" visible="false">
       <p runat="server" class="para" meta:resourcekey="SuccessPara"></p>
    </div>
</asp:Content>

