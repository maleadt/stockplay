<%@ Page Language="C#" MasterPageFile="~/MasterPage.master" AutoEventWireup="true" CodeFile="SecuritiesOverview.aspx.cs" Inherits="SecuritiesOverview" Title="Untitled Page" %>

<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder" Runat="Server">
    
    <asp:GridView ID="SecuritiesGridview" runat="server" AutoGenerateColumns="False" AllowSorting="True" 
        onsorting="SecuritiesGridview_Sorting">
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