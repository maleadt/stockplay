<%@ Page Language="C#" MasterPageFile="~/MasterPage.master" AutoEventWireup="true" CodeFile="SecuritiesOverview.aspx.cs" Inherits="SecuritiesOverview" Title="Securities Overview" %>

<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder" Runat="Server">

    <h1>Securities Overview</h1>
    <!-- Gridlines op none en cellspacing op -1 zijn nodig om de html attributen uit de html tags te kunnen wegwerken! -->
    <asp:GridView ID="SecuritiesGridview" runat="server" AutoGenerateColumns="False" AllowSorting="True" AllowPaging="True"
        OnSorting="SecuritiesGridview_Sorting" OnPageIndexChanging="SecuritiesGridview_PageIndexChanging" GridLines="None" 
        onrowdatabound="SecuritiesGridview_RowDataBound" CellSpacing="-1" PageSize="20" >
        <Columns>
            <asp:BoundField DataField="Isin" />
            <asp:BoundField DataField="Symbol" HeaderText="Symbol" SortExpression="symbol">
                <ItemStyle CssClass="action" />
            </asp:BoundField>
            <asp:HyperLinkField DataTextField="Name" DataNavigateUrlFields="Isin" SortExpression="Name"
            DataNavigateUrlFormatString="SecurityDetail.aspx?param={0}" HeaderText="Name">
                <ItemStyle CssClass="name" />
            </asp:HyperLinkField>
            <asp:BoundField DataField="Exchange" HeaderText="Exchange" SortExpression="Exchange">
                <ItemStyle CssClass="action" />
            </asp:BoundField>
            <asp:BoundField DataField="Change" HeaderText="Change" SortExpression="Change" HtmlEncode="false" DataFormatString="{0:0.00}" />
            <asp:BoundField DataField="Quote" HeaderText="Quote" SortExpression="Quote" HtmlEncode="false" DataFormatString="{0:0.00}" />
            <asp:BoundField DataField="Date" HeaderText="Date" SortExpression="Date" HtmlEncode="false" DataFormatString="{0:dd/MM, HH:MM:ss}" />
            <asp:HyperLinkField HeaderText="Buy" DataNavigateUrlFields="Isin" DataNavigateUrlFormatString="~/User/CreateOrder.aspx?isin={0}"
            Text="<img src='App_Themes/StockPlay/afbeeldingen/money_add.png' alt='Buy' border='0' />" />
        </Columns>
    </asp:GridView>
</asp:Content>