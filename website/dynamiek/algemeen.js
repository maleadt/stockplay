// Variabelen

var searchBox = $('#search');

// Standaard tekst in de zoekbalk

searchBox.focus(function(){  
	if($(this).attr('value') == searchDefault)
		$(this).attr('value', '');
}).blur(function(){  
	if($(this).attr('value') == '')
		$(this).attr("value", searchDefault);  
}).bind("keydown", function(e) {
	if (e.keyCode == 13){
		location.href="SecuritiesOverview.aspx?search="+searchBox.val();
		e.preventDefault();	
	}
});