<%@ Page Language="C#" MasterPageFile="~/MasterPage.master" AutoEventWireup="true" CodeBehind="Overview.aspx.cs" Inherits="StockPlay.Web.User_Overview"
    UICulture="auto" Culture="auto" meta:resourcekey="Page" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" Runat="Server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder" Runat="Server">
    <h1 runat="server" meta:resourcekey="Title"></h1>

    <p runat="server" class="para" meta:resourcekey="Para1"></p>

    <table class="table" id="account">
        <tr>
            <td class="col" runat="server" meta:resourcekey="Username"></td>
            <td><p ID="Username" runat="server"></p></td>
        </tr>
        <tr>
            <td class="col" runat="server" meta:resourcekey="Lastname"></td>
            <td><p ID="Lastname" runat="server"></p></td>
        </tr>
        <tr>
            <td class="col" runat="server" meta:resourcekey="Firstname"></td>
            <td><p ID="Firstname" runat="server"></p></td>
        </tr>
        <tr>
            <td class="col" runat="server" meta:resourcekey="Email"></td>
            <td><p id="Email" runat="server"></p></td>
        </tr>
    </table>
    
    <table class="table" id="status">
        <tr>
            <td class="col" runat="server" meta:resourcekey="Balance"></td>
            <td><p ID="Balance" runat="server"></p></td>
        </tr>
        <tr>
            <td class="col" runat="server" meta:resourcekey="Orders"></td>
            <td><a runat="server" href="~/User/OrdersOverview.aspx" meta:resourcekey="GotoOrders"></a></td>
        </tr>
        <tr>
            <td class="col" runat="server" meta:resourcekey="Portfolio"></td>
            <td><a runat="server" href="~/User/Portfolio.aspx" meta:resourcekey="GotoPortfolio"></a></td>
        </tr>
    </table>
    
    <hr />

    <h1 id="H1" runat="server" meta:resourcekey="Title2"></h1>

    <p runat="server" class="para" meta:resourcekey="Para2"></p>
    <table class="table" id="AccountUpdate" runat="server">
        <tr>
            <td runat="server" meta:resourcekey="OldPassword"></td>
            <td>
                <asp:TextBox ID="OldPassword" CssClass="textbox" runat="server" TextMode="Password"></asp:TextBox>
                <asp:RequiredFieldValidator ID="PasswordRequired" runat="server" ValidationGroup="UpdateUser"
                ControlToValidate="OldPassword" ErrorMessage="Please enter your old password">*</asp:RequiredFieldValidator>
            </td>
        </tr>
        <tr>
            <td runat="server" meta:resourcekey="NewPassword"></td>
            <td>
                <asp:TextBox ID="NewPassword" CssClass="textbox" runat="server" TextMode="Password"></asp:TextBox>
                <asp:RequiredFieldValidator ID="NewPasswordRequired" runat="server" ValidationGroup="UpdateUser"
                ControlToValidate="OldPassword" meta:resourckey="NewPasswordError">*</asp:RequiredFieldValidator>
            </td>
        </tr>
        <tr>
            <td runat="server" meta:resourcekey="ConfirmPassword"></td>
            <td>
                <asp:TextBox ID="ConfirmPassword" CssClass="textbox" runat="server" TextMode="Password"></asp:TextBox>
                <asp:RequiredFieldValidator ID="ConfirmPasswordRequired" runat="server" ValidationGroup="UpdateUser"
                ControlToValidate="OldPassword" meta:resourcekey="OldPasswordError">*</asp:RequiredFieldValidator>
                <asp:CompareValidator runat="server" ID="CompareValidator" ControlToValidate="ConfirmPassword" ValidationGroup="UpdateUser"
                ControlToCompare="NewPassword" meta:resourcekey="NewPasswordMissing">*</asp:CompareValidator>
            </td>
        </tr>
        <tr>
            <td runat="server" meta:resourcekey="NewLastname"></td>
            <td>
                <asp:TextBox ID="txtLastname" CssClass="textbox" runat="server"></asp:TextBox>
            </td>
        </tr>
        <tr>
            <td runat="server" meta:resourcekey="NewFirstname"></td>
            <td>
                <asp:TextBox ID="txtFirstname" CssClass="textbox" runat="server"></asp:TextBox>
            </td>
        </tr>
        <tr>
            <td runat="server" meta:resourcekey="NewEmail"></td>
            <td>
                <asp:TextBox ID="txtEmail" CssClass="textbox" runat="server"></asp:TextBox>
                <asp:RegularExpressionValidator ID="RegularExpressionValidator1" runat="server" 
                    ControlToValidate="txtEmail" ValidationGroup="UpdateUser" Display="Dynamic" meta:resourcekey="EmailError"
                    ValidationExpression="\w+([-+.']\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*"></asp:RegularExpressionValidator>
            </td>
        </tr>
        <tr>
            <td></td>
            <td>
                <asp:Button ID="btnUpdate" class="button" runat="server" onclick="btnUpdate_Click" meta:resourcekey="Update" />
                <asp:Label ID="ErrorLabel" runat="server" ForeColor="Red" Visible="false" meta:resourcekey="UpdateError" />
                <asp:Label ID="SuccessLabel" runat="server" ForeColor="Green" Visible="false" meta:resourcekey="UpdateSuccess" />
            </td>
        </tr>
    </table>
    
</asp:Content>

