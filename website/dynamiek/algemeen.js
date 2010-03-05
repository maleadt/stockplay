// Variabelen

var searchBox = $('#search');

// Standaard tekst in de zoekbalk

searchBox.focus(function(){  
	if($(this).attr('value') == searchDefault)
		$(this).attr('value', '');
});

searchBox.blur(function(){  
	if($(this).attr('value') == '')
		$(this).attr("value", searchDefault);  
});