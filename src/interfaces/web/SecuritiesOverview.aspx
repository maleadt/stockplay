<%@ Page Language="C#" MasterPageFile="~/MasterPage.master" AutoEventWireup="true" CodeFile="SecuritiesOverview.aspx.cs" Inherits="StockPlay.Web.SecuritiesOverview"
    UICulture="auto" Culture="auto" meta:resourcekey="Page"%>

<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder" Runat="Server">

    <h1 runat="server" meta:resourcekey="Title"></h1>
    <!-- Gridlines op none en cellspacing op -1 zijn nodig om de html attributen uit de html tags te kunnen wegwerken! -->
    <asp:GridView ID="SecuritiesGridview" runat="server" AutoGenerateColumns="False" AllowSorting="True" AllowPaging="True"
        OnSorting="SecuritiesGridview_Sorting" OnPageIndexChanging="SecuritiesGridview_PageIndexChanging" GridLines="None" 
        onrowdatabound="SecuritiesGridview_RowDataBound" CellSpacing="-1" PageSize="20" >
        <Columns>
            <asp:BoundField DataField="Isin" />
            <asp:BoundField DataField="Symbol" SortExpression="symbol" meta:resourcekey="Symbol">
                <ItemStyle CssClass="action" />
            </asp:BoundField>
            <asp:HyperLinkField DataTextField="Name" DataNavigateUrlFields="Isin" SortExpression="Name"
            DataNavigateUrlFormatString="SecurityDetail.aspx?param={0}" meta:resourcekey="Name">
                <ItemStyle CssClass="name" />
            </asp:HyperLinkField>
            <asp:BoundField DataField="Exchange" SortExpression="Exchange" meta:resourcekey="Exchange">
                <ItemStyle CssClass="action" />
            </asp:BoundField>
            <asp:BoundField DataField="Change" SortExpression="Change" HtmlEncode="false" DataFormatString="{0:0.00}" meta:resourcekey="Change" />
            <asp:BoundField DataField="Quote" SortExpression="Quote" HtmlEncode="false" DataFormatString="{0:0.00}" meta:resourcekey="Quote" />
            <asp:BoundField DataField="Date" SortExpression="Date" HtmlEncode="false" DataFormatString="{0:dd/MM, HH:MM:ss}" meta:resourcekey="Date" />
            <asp:HyperLinkField DataNavigateUrlFields="Isin" DataNavigateUrlFormatString="~/User/CreateOrder.aspx?isin={0}" meta:resourcekey="Buy"
            Text="<img src='App_Themes/StockPlay/afbeeldingen/money_add.png' alt='Buy' border='0' />" />
        </Columns>
    </asp:GridView>
</asp:Content>