<%@ Page Language="C#" MasterPageFile="~/MasterPage.master" AutoEventWireup="true" CodeFile="SecurityDetail.aspx.cs" Inherits="SecurityDetail" Title="Untitled Page" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" Runat="Server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder" Runat="Server">
    <h1 ID="Name" runat="server"></h1>
    <h2 ID="Value" runat="server"></h2>
    <h2 ID="Change" runat="server"></h2>
    
    <table class="table" id="general">
        <tr>
            <td class="col">Exchange</td>
            <td><p ID="Exchange" runat="server"></p></td>
        </tr>
        <tr>
            <td class="col">ISIN</td>
            <td><p ID="ISIN" runat="server"></p></td>
        </tr>
        <tr>
            <td class="col">Symbol</td>
            <td><p ID="Symbol" runat="server"></p></td>
        </tr>
    </table>
    
    <table class="table" id="data">
        <tr>
            <td class="col">Open</td>
            <td><p ID="Open" runat="server"></p></td>
        </tr>
        <tr>
            <td class="col">High</td>
            <td><p ID="High" runat="server"></p></td>
        </tr>
        <tr>
            <td class="col">Low</td>
            <td><p ID="Low" runat="server"></p></td>
        </tr>
    </table>
    
    <asp:GridView ID="HistoryGridView" runat="server" AutoGenerateColumns="False" 
    onrowdatabound="HistoryGridView_RowDataBound" GridLines="None"  CellSpacing="-1">
        <Columns>
            <asp:BoundField DataField="Date" HeaderText="Date" HtmlEncode="false" DataFormatString="{0:dd/MM/yyyy}" />
            <asp:BoundField DataField="Change" HeaderText="Change" />
            <asp:BoundField DataField="Open" HeaderText="Open" />
            <asp:BoundField DataField="Low" HeaderText="Low" />
            <asp:BoundField DataField="High" HeaderText="High" />
        </Columns>
    </asp:GridView>

    <div id="plotTest" class="plot">
        <div class="draw">
            <ul>
                <li class="zoomOut hand">Uitzoomen</li>
                <li class="reset hand">Herstel</li>
            </ul>
        </div>
    </div>

    <!-- JAVASCRIPT -->
    <script type="text/javascript" src="http://code.jquery.com/jquery-latest.js"></script>
    <script type="text/javascript" src="App_Themes/StockPlay/dynamiek/taalbestanden/nl-be.js"></script>
    <script type="text/javascript" src="App_Themes/StockPlay/dynamiek/algemeen.js"></script>
    <!--[if IE]><script language="javascript" type="text/javascript" src="App_Themes/StockPlay/grafiek/flot/excanvas.min.js"></script><![endif]-->
    <script type="text/javascript" src="App_Themes/StockPlay/grafiek/flot/jquery.flot.min.js"></script>
    <script type="text/javascript" src="App_Themes/StockPlay/grafiek/flot/jquery.flot.selection.min.js"></script>
    <script type="text/javascript" src="App_Themes/StockPlay/grafiek/flot/jquery.flot.navigate.min.js"></script>
    <script type="text/javascript" src="App_Themes/StockPlay/grafiek/grafiek.js"></script>

</asp:Content>

