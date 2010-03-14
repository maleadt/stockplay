<%@ Page Language="C#" MasterPageFile="~/MasterPage.master" AutoEventWireup="true" CodeFile="SecuritiesOverview.aspx.cs" Inherits="SecuritiesOverview" Title="Untitled Page" %>

<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder" Runat="Server">
    
    <asp:GridView ID="SecuritiesGridview" runat="server" 
        AutoGenerateColumns="False" AllowSorting="True" 
        onsorting="SecuritiesGridview_Sorting">
        <Columns>
            <asp:BoundField DataField="Symbol" HeaderText="Symbol" 
                SortExpression="Symbol" />
            <asp:BoundField DataField="Name" HeaderText="Name" SortExpression="Name" />
            <asp:BoundField DataField="Location" HeaderText="Location" 
                SortExpression="Location" />
        </Columns>
    </asp:GridView>
    
</asp:Content>