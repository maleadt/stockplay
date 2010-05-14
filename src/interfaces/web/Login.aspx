<%@ Page Language="C#" MasterPageFile="~/MasterPage.master" AutoEventWireup="true" CodeBehind="Login.aspx.cs" Inherits="StockPlay.Web.Login"
    culture="auto" uiculture="auto" meta:resourcekey="Page"%>

<asp:Content ID="Content1" ContentPlaceHolderID="head" Runat="Server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder" Runat="Server">
    <h1 runat="server" meta:resourcekey="Title"></h1>

    <p runat="server" id="Notification" class="para" meta:resourcekey="Notification" visible="false"></p>

    <table id="TableLogin" runat="server">
        <tr>
            <td class="col">
                <asp:Literal ID="Literal1" runat="server" Text="<%$ Resources:Nickname %>" />
            </td>
            <td>
                <asp:TextBox ID="txtUsername" CssClass="textbox" runat="server"></asp:TextBox>
            </td>
        </tr>

        <tr>
            <td class="col">
                <asp:Literal ID="Literal2" runat="server" Text="<%$ Resources:Password %>" />
            </td>
            <td>
                <asp:TextBox ID="txtPassword" CssClass="textbox" runat="server" TextMode="Password"></asp:TextBox>
            </td>
        </tr>

        <tr>
            <td>
                <asp:CheckBox ID="chkRememberMe" runat="server" Text="<%$ Resources:RememberMe %>"/>
            </td>
            <td>
                <asp:Button ID="btnLogin" class="button" runat="server" Text="Login" onclick="btnLogin_Click" meta:resourcekey="LoginButton" />
            </td>
        </tr>
        <tr>
            <td>
                <a id="A1" href="ForgotPassword.aspx" meta:resourcekey="ForgotPassword" runat="server"></a>
            </td>
            <td>
                <asp:Label ID="ErrorLabel" Visible="false" runat="server" meta:resourcekey="Error" ForeColor="Red"></asp:Label>
            </td>
        </tr>
    </table>

</asp:Content>

