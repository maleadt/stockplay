<%@ Page Language="C#" MasterPageFile="~/MasterPage.master" AutoEventWireup="true" CodeBehind="Portfolio.aspx.cs" Inherits="StockPlay.Web.User_Portfolio"
    UICulture="auto" Culture="auto" meta:resourcekey="Page" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" Runat="Server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder" Runat="Server">
    <h1 runat="server" meta:resourcekey="Title">Portfolio</h1>

    <p class="para" runat="server" meta:resourcekey="Para1"></p>

    <div id="SellMessage" runat="server" visible="false">
        <p class="para"><asp:Literal runat="server" Text="<%$ Resources:Para21 %>" /> <b id="SecurityName" runat="server"></b> <asp:Literal runat="server" Text="<%$ Resources:Para22 %>" /> <b id="Price" runat="server"></b>?</p>
        <p><asp:Literal ID="Literal1" runat="server" Text="<%$ Resources:Para5 %>" />
    
            <asp:Literal ID="SoortOrder" runat="server" Text=""></asp:Literal>
            <asp:DropDownList ID="OrderType" runat="server" AutoPostBack="True">
                <asp:ListItem Value="direct" meta:resourcekey="direct"></asp:ListItem>
                <asp:ListItem Value="limit" meta:resourcekey="limit"></asp:ListItem>
                <asp:ListItem Value="bracket" meta:resourcekey="bracket"></asp:ListItem>
                <asp:ListItem Value="stoploss" meta:resourcekey="stoploss"></asp:ListItem>
                <asp:ListItem Value="trailing" meta:resourcekey="trailing"></asp:ListItem>
            </asp:DropDownList>
        </p>
        <p>
            <asp:Literal runat="server" Text="<%$ Resources:Para31 %>" ID="lblAantal" /> <asp:TextBox ID="txtAmount" runat="server" />
            <asp:Literal runat="server" Text=" / " ID="lblStreep" />
            <asp:TextBox ID="txtTotalAmount" runat="server" Enabled="False"></asp:TextBox><br />
            <asp:RangeValidator ID="txtAmountValidator" runat="server" ControlToValidate="txtAmount"
            MinimumValue="0" Type="Integer" meta:resourcekey="AmountError"></asp:RangeValidator>
            
            <asp:Literal runat="server" Text="<%$ Resources:Para32 %>" id="lblVraagprijs" Visible="false"/> <asp:TextBox visible="false" ID="txtPrice" runat="server"></asp:TextBox>
            <asp:RangeValidator ID="txtPriceValidator" runat="server" ControlToValidate="txtPrice"
            MinimumValue="1" MaximumValue="1000000" Type="Double" meta:resourcekey="PriceError"></asp:RangeValidator>
        </p>
        <p>
            <asp:Literal ID="lblOnderLimiet" runat="server" Text="<%$ Resources:Para6 %>" Visible="false"/> 
            <asp:TextBox ID="txtOnderLimiet" runat="server" Visible="false"></asp:TextBox>
        </p>
        <p>
            <asp:Literal ID="lblBovenLimiet" runat="server" Text="<%$ Resources:Para7 %>" Visible="false" /> 
            <asp:TextBox ID="txtBovenLimiet" runat="server" Visible="false"></asp:TextBox>
        </p>
        <p>
            <asp:Literal ID="lblBonuspunten" runat="server" Visible="false" Text="<%$ Resources:Para8 %>" />
            <asp:TextBox ID="txtBonuspunten" runat="server" Visible="false"></asp:TextBox>
        </p>
        <p>
            <asp:Button ID="btnConfirm" runat="server" OnClick="btnConfirm_Click" meta:resourcekey="Confirm" />
            <asp:Button ID="btnCancel" runat="server" OnClick="btnCancel_Click" meta:resourcekey="Cancel" />
        </p>
    </div>

    <asp:GridView ID="PortfolioGridview" runat="server" AutoGenerateColumns="False" GridLines="None" CellSpacing="-1">
        <Columns>
            <asp:BoundField DataField="Isin" meta:resourcekey="Isin" />
            <asp:BoundField DataField="Security" meta:resourcekey="Security" />
            <asp:BoundField DataField="Quote" DataFormatString="{0:0.00}" meta:resourcekey="Quote" />
            <asp:BoundField DataField="Amount" meta:resourcekey="Amount" />
            <asp:HyperLinkField DataNavigateUrlFields="Isin" DataNavigateUrlFormatString="~/User/Portfolio.aspx?sell={0}" meta:resourcekey="Sell" />
        </Columns>
    </asp:GridView>
</asp:Content>