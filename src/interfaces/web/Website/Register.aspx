﻿<%@ Page Language="C#" MasterPageFile="~/MasterPage.master" AutoEventWireup="true" CodeFile="Register.aspx.cs" Inherits="Register" Title="Register" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" Runat="Server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder" Runat="Server">

    <asp:CreateUserWizard ID="CreateUser1" runat="server" 
        oncreateduser="CreateUser1_CreatedUser">
        <WizardSteps>
            <asp:CreateUserWizardStep runat="server">
                <ContentTemplate>
                    <table>
                        <tr>
                            <td colspan="2">Register a new account</td>
                        </tr>
                        <tr>
                            <td>Nickname:</td>
                            <td>
                                <asp:TextBox ID="UserName" runat="server"></asp:TextBox>
                                <asp:RequiredFieldValidator ID="NicknameRequired" runat="server" ValidationGroup="CreateUser1"
                                ControlToValidate="UserName" ErrorMessage="You have to enter a nickname">*</asp:RequiredFieldValidator>
                            </td>
                        </tr>
                        <tr>
                            <td>Password:</td>
                            <td>
                                <asp:TextBox ID="Password" runat="server" TextMode="Password"></asp:TextBox>
                                <asp:RequiredFieldValidator ID="PasswordRequired" runat="server" ValidationGroup="CreateUser1"
                                ControlToValidate="Password" ErrorMessage="Please enter a password">*</asp:RequiredFieldValidator>
                            </td>
                        </tr>
                        <tr>
                            <td>Confirm Password:</td>
                            <td>
                                <asp:TextBox ID="txtPasswordConfirm" runat="server" TextMode="Password"></asp:TextBox>
                                <asp:CompareValidator runat="server" ID="PasswordCompare" ControlToValidate="txtPasswordConfirm" ValidationGroup="CreateUser1"
                                ControlToCompare="Password" Text="The entered password does not match">*</asp:CompareValidator>
                            </td>
                        </tr>
                        <tr>
                            <td>Last name:</td>
                            <td>
                                <asp:TextBox ID="txtLastname" runat="server"></asp:TextBox>
                                <asp:RequiredFieldValidator ID="LastnameRequired" runat="server" ValidationGroup="CreateUser1"
                                ControlToValidate="txtLastname" ErrorMessage="You need to enter a last name">*</asp:RequiredFieldValidator>
                            </td>
                        </tr>
                        <tr>
                            <td>First name:</td>
                            <td>
                                <asp:TextBox ID="txtFirstname" runat="server"></asp:TextBox>
                                <asp:RequiredFieldValidator ID="FirstnameRequired" runat="server" ValidationGroup="CreateUser1"
                                ControlToValidate="txtFirstname" ErrorMessage="You need to enter a first name">*</asp:RequiredFieldValidator>
                            </td>
                        </tr>
                        <tr>
                            <td>E-mail adress:</td>
                            <td>
                                <asp:TextBox ID="Email" runat="server"></asp:TextBox>
                                <asp:RequiredFieldValidator ID="EmailRequired" runat="server" ValidationGroup="CreateUser1"
                                ControlToValidate="Email" ErrorMessage="You need to your e-mail adress">*</asp:RequiredFieldValidator>
                                <asp:RegularExpressionValidator ID="RegularExpressionValidator1" runat="server" 
                                    ControlToValidate="Email" ValidationGroup="CreateUser1" Display="Dynamic"
                                    ErrorMessage="You have to enter a valid e-mail address" 
                                    ValidationExpression="\w+([-+.']\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*"></asp:RegularExpressionValidator>
                            </td>
                        </tr>
                    </table>
                </ContentTemplate>
            </asp:CreateUserWizardStep>
            <asp:CompleteWizardStep Title="Registration successful" runat="server" />
        </WizardSteps>
    </asp:CreateUserWizard>

</asp:Content>
