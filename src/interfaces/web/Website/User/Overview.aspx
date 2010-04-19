<%@ Page Language="C#" MasterPageFile="~/MasterPage.master" AutoEventWireup="true" CodeFile="Overview.aspx.cs" Inherits="User_Overview" Title="Overview" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" Runat="Server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder" Runat="Server">

    <p>Here you can view your general account information. If you want to change your account data, you can fill in the form below.</p>

    <table class="table" id="account">
        <tr>
            <td class="col">Username</td>
            <td><p ID="Username" runat="server"></p></td>
        </tr>
        <tr>
            <td class="col">Lastname</td>
            <td><p ID="Lastname" runat="server"></p></td>
        </tr>
        <tr>
            <td class="col">Firstname</td>
            <td><p ID="Firstname" runat="server"></p></td>
        </tr>
        <tr>
            <td class="col">Email</td>
            <td><p id="Email" runat="server"></p></td>
        </tr>
    </table>
    
    <table class="table" id="status">
        <tr>
            <td class="col">Account balance</td>
            <td><p ID="Balance" runat="server"></p></td>
        </tr>
        <tr>
            <td class="col">Orders</td>
            <td><a runat="server" href="~/User/OrdersOverview.aspx">Go to your orders...</a></td>
        </tr>
        <tr>
            <td class="col">Portfolio</td>
            <td><a runat="server" href="~/User/Portfolio.aspx">Go to your portfolio...</a></td>
        </tr>
    </table>
    
    <p>Fill out the following form if you want to update your account information:</p>
    <table id="AccountUpdate" runat="server">
        <tr>
            <td>Old password:</td>
            <td>
                <asp:TextBox ID="OldPassword" runat="server" TextMode="Password"></asp:TextBox>
                <asp:RequiredFieldValidator ID="PasswordRequired" runat="server" ValidationGroup="UpdateUser"
                ControlToValidate="OldPassword" ErrorMessage="Please enter your old password">*</asp:RequiredFieldValidator>
            </td>
        </tr>
        <tr>
            <td>New Password:</td>
            <td>
                <asp:TextBox ID="NewPassword" runat="server" TextMode="Password"></asp:TextBox>
                <asp:RequiredFieldValidator ID="NewPasswordRequired" runat="server" ValidationGroup="UpdateUser"
                ControlToValidate="OldPassword" ErrorMessage="Please enter your old password">*</asp:RequiredFieldValidator>
            </td>
        </tr>
        <tr>
            <td>Confirm Password:</td>
            <td>
                <asp:TextBox ID="ConfirmPassword" runat="server" TextMode="Password"></asp:TextBox>
                <asp:RequiredFieldValidator ID="ConfirmPasswordRequired" runat="server" ValidationGroup="UpdateUser"
                ControlToValidate="OldPassword" ErrorMessage="Please enter your old password">*</asp:RequiredFieldValidator>
                <asp:CompareValidator runat="server" ID="CompareValidator" ControlToValidate="ConfirmPassword" ValidationGroup="UpdateUser"
                ControlToCompare="NewPassword" Text="Please enter a new password">*</asp:CompareValidator>
            </td>
        </tr>
        <tr>
            <td>Last name:</td>
            <td>
                <asp:TextBox ID="txtLastname" runat="server"></asp:TextBox>
            </td>
        </tr>
        <tr>
            <td>First name:</td>
            <td>
                <asp:TextBox ID="txtFirstname" runat="server"></asp:TextBox>
            </td>
        </tr>
        <tr>
            <td>E-mail adress:</td>
            <td>
                <asp:TextBox ID="txtEmail" runat="server"></asp:TextBox>
                <asp:RegularExpressionValidator ID="RegularExpressionValidator1" runat="server" 
                    ControlToValidate="txtEmail" ValidationGroup="UpdateUser" Display="Dynamic"
                    ErrorMessage="You have to enter a valid e-mail address" 
                    ValidationExpression="\w+([-+.']\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*"></asp:RegularExpressionValidator>
            </td>
        </tr>
        <tr>
            <td></td>
            <td>
                <asp:Button ID="btnUpdate" Text="Update" runat="server" onclick="btnUpdate_Click" />
                <asp:Label ID="ErrorLabel" Text="Old password is not correct" runat="server" ForeColor="Red" Visible="false" />
            </td>
        </tr>
    </table>
    
</asp:Content>

