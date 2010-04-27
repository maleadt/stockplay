<%@ Page Language="C#" MasterPageFile="~/MasterPage.master" AutoEventWireup="true" CodeFile="Register.aspx.cs" Inherits="Register" Title="Register"
    UICulture="auto" Culture="auto" meta:resourcekey="Page" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" Runat="Server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder" Runat="Server">

    <p class="para" id="FinishRegistration" runat="server" visible="false" meta:resourcekey="FinishRegistration"></p>
    <table id="TableRegister" runat="server">
        <tr>
            <td colspan="2" meta:resourcekey="TableHeader"></td>
        </tr>
        <tr>
            <td runat="server" meta:resourcekey="Nickname"></td>
            <td>
                <asp:TextBox ID="UserName" runat="server"></asp:TextBox>
                <asp:RequiredFieldValidator ID="NicknameRequired" runat="server" ValidationGroup="CreateUser1"
                ControlToValidate="UserName" meta:resourcekey="UsernameError">*</asp:RequiredFieldValidator>
            </td>
        </tr>
        <tr>
            <td  runat="server" meta:resourcekey="Password"></td>
            <td>
                <asp:TextBox ID="Password" runat="server" TextMode="Password"></asp:TextBox>
                <asp:RequiredFieldValidator ID="PasswordRequired" runat="server" ValidationGroup="CreateUser1"
                ControlToValidate="Password" meta:resourcekey="PasswordError">*</asp:RequiredFieldValidator>
            </td>
        </tr>
        <tr>
            <td runat="server" meta:resourcekey="ConfirmPassword"></td>
            <td>
                <asp:TextBox ID="txtPasswordConfirm" runat="server" TextMode="Password"></asp:TextBox>
                <asp:CompareValidator runat="server" ID="PasswordCompare" ControlToValidate="txtPasswordConfirm" ValidationGroup="CreateUser1"
                ControlToCompare="Password" meta:resourcekey="ConfirmPasswordError">*</asp:CompareValidator>
            </td>
        </tr>
        <tr>
            <td runat="server" meta:resourcekey="Lastname"></td>
            <td>
                <asp:TextBox ID="txtLastname" runat="server"></asp:TextBox>
                <asp:RequiredFieldValidator ID="LastnameRequired" runat="server" ValidationGroup="CreateUser1"
                ControlToValidate="txtLastname" meta:resourcekey="LastnameError">*</asp:RequiredFieldValidator>
            </td>
        </tr>
        <tr>
            <td runat="server" meta:resourcekey="Firstname"></td>
            <td>
                <asp:TextBox ID="txtFirstname" runat="server"></asp:TextBox>
                <asp:RequiredFieldValidator ID="FirstnameRequired" runat="server" ValidationGroup="CreateUser1"
                ControlToValidate="txtFirstname" meta:resourcekey="FirstnameError">*</asp:RequiredFieldValidator>
            </td>
        </tr>
        <tr>
            <td runat="server" meta:resourcekey="Email"></td>
            <td>
                <asp:TextBox ID="Email" runat="server"></asp:TextBox>
                <asp:RequiredFieldValidator ID="EmailRequired" runat="server" ValidationGroup="CreateUser1"
                ControlToValidate="Email" ErrorMessage="You need to your e-mail adress" meta:resourcekey="EmailMissing">*</asp:RequiredFieldValidator>
                <asp:RegularExpressionValidator ID="RegularExpressionValidator1" runat="server" 
                    ControlToValidate="Email" ValidationGroup="CreateUser1" Display="Dynamic" meta:resourcekey="EmailInvalid"
                    ValidationExpression="\w+([-+.']\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*"></asp:RegularExpressionValidator>
            </td>
        </tr>
        <tr>
            <td></td>
            <td><asp:Button ID="btnRegister" runat="server" meta:resourcekey="RegisterButton" /> 
                <asp:Button ID="btnCancel" Text="Cancel" runat="server" meta:resourcekey="CancelButton" onclick="btnCancel_Click" />
                <asp:Label ID="ErrorLabel" runat="server" ForeColor="Red" Visible="False" meta:resourcekey="ErrorLabel"></asp:Label>
            </td>
        </tr>
    </table>
</asp:Content>

