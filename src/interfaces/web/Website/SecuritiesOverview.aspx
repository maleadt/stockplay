<%@ Page Language="C#" MasterPageFile="~/MasterPage.master" AutoEventWireup="true" CodeFile="SecuritiesOverview.aspx.cs" Inherits="SecuritiesOverview" Title="Securities Overview" %>

<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder" Runat="Server">
    
    <p>Gridview:</p>
    <!-- Gridlines op none en cellspacing op -1 zijn nodig om de html attributen uit de html tags te kunnen wegwerken! -->
    <asp:GridView ID="SecuritiesGridview" runat="server" 
        AutoGenerateColumns="False" AllowSorting="True" AllowPaging="True"
        OnSorting="SecuritiesGridview_Sorting" 
        OnPageIndexChanging="SecuritiesGridview_PageIndexChanging" GridLines="None" 
        CellSpacing="-1" PageSize="2">
        <Columns>
            <asp:BoundField DataField="Symbol" HeaderText="Symbool" SortExpression="Symbol" >
                <ItemStyle CssClass="name" />
            </asp:BoundField>
            <asp:BoundField DataField="Name" HeaderText="Naam" SortExpression="Name">
                <ItemStyle CssClass="action" />
            </asp:BoundField>
            <asp:BoundField DataField="Exchange" HeaderText="Index" SortExpression="Exchange">
                <ItemStyle CssClass="action" />
            </asp:BoundField>
            <asp:BoundField DataField="Quote" HeaderText="Koers" SortExpression="Quote">
                <ItemStyle CssClass="pos" />
            </asp:BoundField>
        </Columns>
    </asp:GridView>
    
</asp:Content>