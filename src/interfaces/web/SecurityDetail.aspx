<%@ Page Language="C#" MasterPageFile="~/MasterPage.master" AutoEventWireup="true" CodeBehind="SecurityDetail.aspx.cs" Inherits="StockPlay.Web.SecurityDetail"
    UICulture="auto" Culture="auto" meta:resourcekey="Page" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" Runat="Server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder" Runat="Server">
    <h1 ID="Name" runat="server"></h1>
    <h2 ID="Value" runat="server"></h2>
    <h2 ID="Change" runat="server"></h2>
    
    <ul class="actions"> 
        <asp:HyperLink ID="BuyHyperlink" runat="server"><li class="action buy" runat="server" meta:resourcekey="Buy"></li></asp:HyperLink>
    </ul> 
    
    <table class="table" id="general">
        <tr>
            <td class="col" runat="server" meta:resourcekey="Exchange"></td>
            <td><p ID="Exchange" runat="server"></p></td>
        </tr>
        <tr>
            <td class="col" runat="server" meta:resourcekey="ISIN"></td>
            <td><p ID="ISIN" runat="server"></p></td>
        </tr>
        <tr>
            <td class="col" runat="server" meta:resourcekey="Symbol"></td>
            <td><p ID="Symbol" runat="server"></p></td>
        </tr>
    </table>
    
    <table class="table" id="data">
        <tr>
            <td class="col" runat="server" meta:resourcekey="Open"></td>
            <td><p ID="Open" runat="server"></p></td>
        </tr>
        <tr>
            <td class="col" runat="server" meta:resourcekey="High"></td>
            <td><p ID="High" runat="server"></p></td>
        </tr>
        <tr>
            <td class="col" runat="server" meta:resourcekey="Low"></td>
            <td><p ID="Low" runat="server"></p></td>
        </tr>
    </table>
    
    <asp:GridView ID="HistoryGridView" runat="server" AutoGenerateColumns="False" 
    onrowdatabound="HistoryGridView_RowDataBound" GridLines="None"  CellSpacing="-1">
        <Columns>
            <asp:BoundField DataField="Date" HtmlEncode="false" DataFormatString="{0:dd/MM/yyyy}" meta:resourcekey="DateField" />
            <asp:BoundField DataField="Change" meta:resourcekey="ChangeField" />
            <asp:BoundField DataField="Open" meta:resourcekey="OpenField" />
            <asp:BoundField DataField="Low" meta:resourcekey="LowField" />
            <asp:BoundField DataField="High" meta:resourcekey="HighField" />
        </Columns>
    </asp:GridView>

    <div style="height:100px">
    </div>

	<div id="plotTestContainer" class="plot">
		<div id="plotTest">
			<div class="draw"></div>
			<ul>
				<li class="pan hand"></li>
				<li class="selection hand"></li>
				<li class="last hand" onselectstart="return false"></li>
				<li class="zoomIn hand" onselectstart="return false"></li>
				<li class="zoomOut hand" onselectstart="return false"></li>
				<li class="add hand"></li>
				<li class="reset hand"></li>
			</ul>
		</div>
		<div class="overlay">
			<p>ISIN: <input name="code" type="text" value="BE0389555039"/> <a href="" class="add"></a> <a href="" class="cancel"></a></p>
		</div>
		<div id="volumes" class="subPlot">
		</div>
	</div>

    <!-- JAVASCRIPT -->
    <!--[if IE]><script language="javascript" type="text/javascript" src="App_Themes/StockPlay/grafiek/flot/excanvas.min.js"></script><![endif]-->
    <script type="text/javascript" src="App_Themes/StockPlay/grafiek/flot/jquery.flot.min.js"></script>
    <script type="text/javascript" src="App_Themes/StockPlay/grafiek/flot/jquery.flot.selection.min.js"></script>
    <script type="text/javascript" src="App_Themes/StockPlay/grafiek/flot/jquery.flot.navigate.min.js"></script>
    <script type="text/javascript" src="App_Themes/StockPlay/grafiek/options.js"></script>
    <script type="text/javascript" src="App_Themes/StockPlay/grafiek/grafiek.js"></script>

    <script type="text/javascript">
        $(function() {
        var prim = new PrimaryPlot('plotTest', 2, 11, '<%=ISINcode%>');
        var sec = new SubPlot('volumes', 2, 11, '<%=ISINcode%>');
            prim.addPlotListener(sec);
        });
    </script>

</asp:Content>

