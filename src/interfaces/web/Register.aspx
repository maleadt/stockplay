<%@ Page Language="C#" MasterPageFile="~/MasterPage.master" AutoEventWireup="true" CodeBehind="Register.aspx.cs" Inherits="StockPlay.Web.Register" Title="Register"
    UICulture="auto" Culture="auto" meta:resourcekey="Page" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" Runat="Server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder" Runat="Server">

    <!-- Wordt getoond na registratie -->
    <p class="para" id="FinishRegistration" visible="false" runat="server">
        <asp:Literal runat="server" meta:resourcekey="FinishRegistration" />
    </p>

    <h1 id="H1" runat="server" meta:resourcekey="TableHeader"></h1>
    <table id="TableRegister" runat="server">
        <tr>
            <td runat="server" class="col" meta:resourcekey="Nickname"></td>
            <td>
                <asp:TextBox ID="UserName" CssClass="textbox" runat="server"></asp:TextBox>
                <asp:RequiredFieldValidator ID="NicknameRequired" runat="server" ValidationGroup="CreateUser1"
                ControlToValidate="UserName" meta:resourcekey="UsernameError">*</asp:RequiredFieldValidator>
            </td>
        </tr>
        <tr>
            <td  runat="server" class="col" meta:resourcekey="Password"></td>
            <td>
                <asp:TextBox ID="Password" CssClass="passbox" runat="server" TextMode="Password"></asp:TextBox>
                <asp:RequiredFieldValidator ID="PasswordRequired" runat="server" ValidationGroup="CreateUser1"
                ControlToValidate="Password" meta:resourcekey="PasswordError">*</asp:RequiredFieldValidator>
            </td>
        </tr>
        <tr>
            <td runat="server" class="col" meta:resourcekey="ConfirmPassword"></td>
            <td>
                <asp:TextBox ID="txtPasswordConfirm" CssClass="passbox" runat="server" TextMode="Password"></asp:TextBox>
                <asp:CompareValidator runat="server" ID="PasswordCompare" ControlToValidate="txtPasswordConfirm" ValidationGroup="CreateUser1"
                ControlToCompare="Password" meta:resourcekey="ConfirmPasswordError">*</asp:CompareValidator>
            </td>
        </tr>
        <tr>
            <td runat="server" class="col" meta:resourcekey="Lastname"></td>
            <td>
                <asp:TextBox ID="txtLastname" CssClass="textbox" runat="server"></asp:TextBox>
                <asp:RequiredFieldValidator ID="LastnameRequired" runat="server" ValidationGroup="CreateUser1"
                ControlToValidate="txtLastname" meta:resourcekey="LastnameError">*</asp:RequiredFieldValidator>
            </td>
        </tr>
        <tr>
            <td runat="server" class="col" meta:resourcekey="Firstname"></td>
            <td>
                <asp:TextBox ID="txtFirstname" CssClass="textbox" runat="server"></asp:TextBox>
                <asp:RequiredFieldValidator ID="FirstnameRequired" runat="server" ValidationGroup="CreateUser1"
                ControlToValidate="txtFirstname" meta:resourcekey="FirstnameError">*</asp:RequiredFieldValidator>
            </td>
        </tr>
        <tr>
            <td runat="server" class="col" meta:resourcekey="Email"></td>
            <td>
                <asp:TextBox ID="Email" CssClass="textbox" runat="server"></asp:TextBox>
                <asp:RequiredFieldValidator ID="EmailRequired" runat="server" ValidationGroup="CreateUser1"
                ControlToValidate="Email" ErrorMessage="You need to your e-mail adress" meta:resourcekey="EmailMissing">*</asp:RequiredFieldValidator>
                <br />
                <asp:RegularExpressionValidator ID="RegularExpressionValidator1" runat="server" 
                    ControlToValidate="Email" ValidationGroup="CreateUser1" Display="Dynamic" meta:resourcekey="EmailInvalid"
                    ValidationExpression="\w+([-+.']\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*"></asp:RegularExpressionValidator>
            </td>
        </tr>
        <tr>
            <td></td>
            <td id="buttons" runat="server">
                <asp:Button ID="btnRegister" class="button" runat="server" meta:resourcekey="RegisterButton" /> 
                <asp:Button ID="btnCancel" class="button" runat="server" meta:resourcekey="CancelButton" onclick="btnCancel_Click" />
                <asp:Label ID="ErrorLabel" runat="server" ForeColor="Red" Visible="False" meta:resourcekey="ErrorLabel"></asp:Label>
            </td>
        </tr>
    </table>
</asp:Content>

